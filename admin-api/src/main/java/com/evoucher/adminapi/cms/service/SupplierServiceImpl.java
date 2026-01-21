package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.StoreRepository;
import com.evoucher.adminapi.cms.dao.models.*;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.SupplierApproveHistoryRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.mapper.SupplierMapper;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.SupplierRequest;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl extends EntityService<Supplier, String, SupplierDTO> implements SupplierService {

    private final SupplierRepository repository;
    private final SupplierApproveHistoryRepository supplierApproveHistoryRepository;
    private final BrandRepository brandRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    private static final SupplierMapper MAPPER = SupplierMapper.INSTANT;
    private static final String NOT_FOUND_ERROR_KEY = "evoucher.supplier.not.found";
    private final SupplierRepository supplierRepository;

    @Override
    public JpaRepository<Supplier, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "supplier";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage(NOT_FOUND_ERROR_KEY)
        );
    }

    @Override
    public SupplierDTO findDtoById(String id) {
        Supplier supplier = repository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND_ERROR_KEY),
                        HttpStatus.BAD_REQUEST
                ));

        return MAPPER.toSupplierDTO(supplier);
    }

    @Override
    public Supplier toEntity(SupplierDTO dto) {
        return MAPPER.toSupplier(dto);
    }

    @Override
    public SupplierDTO toDto(Supplier entity) {
        return MAPPER.toSupplierDTO(entity);
    }

    @Override
    public SupplierDTO createSupplier(SupplierRequest supplierRequest) {
        validateSupplierRequest(supplierRequest);

        log.info("Create supplier: {}", supplierRequest);
        if (Objects.isNull(supplierRequest.getId()) || supplierRequest.getId().isEmpty()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("SupplierId is missing"),
                    HttpStatus.BAD_REQUEST
            );
        }
        boolean supplierExists = repository.existsById(supplierRequest.getId());
        if (supplierExists) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("Supplier with given id already exists"),
                    HttpStatus.BAD_REQUEST
            );
        }
        Supplier supplier = MAPPER.toSupplier(supplierRequest);
        supplier.setValidYn(EnumValidYn.Y);

        // save to database
        log.info("Save supplier with supplierId: {}", supplier.getId());
        supplier = this.save(supplier);

        // case approveStatus not null => create record SupplierApproveHistory is REQ
        createSupplierApproveHistoryWithRequest(supplier.getId(), supplier.getApproveStatusCode());

        return MAPPER.toSupplierDTO(supplier);
    }

    @Override
    public SupplierDTO updateSupplier(String id, SupplierRequest supplierRequest) {
        validateSupplierRequest(supplierRequest);

        // check supplier exists
        log.info("Find supplier with supplierId: {}", id);
        Supplier supplierOld = repository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND_ERROR_KEY),
                        HttpStatus.BAD_REQUEST));

        if (ApproveStatus.APPRV.equals(supplierOld.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.approved"),
                    HttpStatus.BAD_REQUEST);
        }
        // convert to supplier entity
        Supplier supplier = MAPPER.toSupplier(supplierRequest);

        // save to database
        log.info("Update supplier with supplierId: {}", id);
        supplier.setId(id);
        supplier.setValidYn(EnumValidYn.Y);
        supplier = this.save(supplier);

        // case approveStatus not null => create record SupplierApproveHistory is REQ
        createSupplierApproveHistoryWithRequest(id, supplier.getApproveStatusCode());
        return MAPPER.toSupplierDTO(supplier);
    }

    @Override
    @Transactional
    public String deleteSupplierById(String id) {
        log.info("Find Supplier with supplierId: {}", id);
        Supplier supplier = repository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND_ERROR_KEY),
                        HttpStatus.BAD_REQUEST
                ));

        log.info("Delete Supplier with supplierId: {}", id);
        supplier.setValidYn(EnumValidYn.N);
        this.save(supplier);

        // Delete Brand belong Supplier
        log.info("Find list Brand with supplierId: {}", id);
        List<Brand> brands = brandRepository.findAllBySupplierIdAndValidYn(id, EnumValidYn.Y);
        if (!CollectionUtils.isEmpty(brands)) {
            brands.replaceAll(b -> {
                b.setValidYn(EnumValidYn.N);
                return b;
            });
            log.info("Delete all Brand belong supplierId: {}", id);
            brandRepository.saveAll(brands);


            // Delete Store belong Supplier and Synchronize to FE
            log.info("Find list Store with supplierId: {}", id);
            List<Store> stores = storeRepository.findAllBySupplierIdAndValidYn(id, EnumValidYn.Y.toString());
            if (!CollectionUtils.isEmpty(stores)) {
                stores.replaceAll(s -> {
                    s.setValidYn(EnumValidYn.N);
                    return s;
                });
                log.info("Delete all Store belong supplierId: {}", id);
                storeRepository.saveAll(stores);
            }
        }

        return id;
    }

    @Override
    @Transactional
    public String updateStatusSupplier(String id, ApproveRequest approveRequest) {
        // validate approve status request must be APPRV or REJECT
        ApproveStatus approveStatusCodeRequest = approveRequest.getApproveStatusCode();
        if (!ApproveStatus.APPRV.equals(approveStatusCodeRequest)
                && !ApproveStatus.REJCT.equals(approveStatusCodeRequest)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.approve.status.code.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Find Supplier with supplierId: {}", id);
        Supplier supplier = repository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND_ERROR_KEY),
                        HttpStatus.BAD_REQUEST
                ));
        // validate supplier not Approved yet
        if (ApproveStatus.APPRV.equals(supplier.getApproveStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.supplier.approved"),
                    HttpStatus.BAD_REQUEST);
        }
        // validate supplier has been Request
        if (!ApproveStatus.REQ.equals(supplier.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.was.request.approve"),
                    HttpStatus.BAD_REQUEST);
        }

        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

        supplier.setApproveStatusCode(approveStatusCodeRequest);
        supplier.setApproveId(ApproveStatus.APPRV.equals(approveStatusCodeRequest) ? userPrincipal.getId() : null);
        log.info("Save approve status Supplier with id: {}", id);
        this.save(supplier);

        log.info("Save SupplierApproveHistory with supplierId: {} and status: {}", id, approveStatusCodeRequest);
        supplierApproveHistoryRepository.save(SupplierApproveHistory.builder()
                .approveStatusCode(approveStatusCodeRequest)
                .supplierId(id)
                .rejectReason(ApproveStatus.REJCT.equals(approveStatusCodeRequest) ? approveRequest.getRejectReason() : null)
                .build());

        return id;
    }

    @Override
    public Page<SupplierDTO> searchSupplierDTO(FilterSearchCms filterSearchCms) {
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SupplierDTO> supplierDTOS = repository.searchSupplier(filterSearchCms, pageable);
        long supplierNumber = 0;
        if (!CollectionUtils.isEmpty(supplierDTOS)) {
            supplierNumber = repository.countSupplier(filterSearchCms);
        }

        return new PageImpl<>(supplierDTOS, pageable, supplierNumber);
    }

    private void validateSupplierRequest(SupplierRequest supplierRequest) {
        if (Objects.nonNull(supplierRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(supplierRequest.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.approve.status.code.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void createSupplierApproveHistoryWithRequest(String id, ApproveStatus approveStatus) {
        if (Objects.nonNull(approveStatus)) {
            log.info("Create record REQ in supplierApproveHistory with supplierId: {}", id);
            supplierApproveHistoryRepository.save(SupplierApproveHistory.builder()
                    .supplierId(id)
                    .approveStatusCode(ApproveStatus.REQ)
                    .build());
        }
    }
}
