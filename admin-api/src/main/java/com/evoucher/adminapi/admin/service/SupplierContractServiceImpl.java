package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.SupplierContractApproveHistoryRepository;
import com.evoucher.adminapi.admin.dao.SupplierContractRepository;
import com.evoucher.adminapi.admin.dao.models.SupplierContract;
import com.evoucher.adminapi.admin.dao.models.SupplierContractApproveHistory;
import com.evoucher.adminapi.admin.mapper.SupplierContractMapper;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.mapper.SupplierMapper;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierContractServiceImpl implements SupplierContractService {

    public static final String EVOUCHER_CONTRACT_NOT_FOUND = "evoucher.contract.not.found";
    private final SupplierContractRepository supplierContractRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierContractApproveHistoryRepository supplierContractApproveHistoryRepository;

    private final SupplierContractMapper supplierContractMapper;
    private final SupplierMapper supplierMapper;


    @Override
    public SupplierContractDTO findById(Integer id) {
        log.info("Find Contract with id: {}", id);
        SupplierContract supplierContract = supplierContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        // validate permission
        validatePermission(supplierContract);

        SupplierDTO supplierDTO = supplierRepository.findByIdAndValidYn(supplierContract.getSupplierId(), EnumValidYn.Y)
                .map(supplierMapper::toSupplierDTO)
                .orElseThrow(() -> new CustomCodeException("evoucher.supplier.not.found",
                        HttpStatus.BAD_REQUEST));

        SupplierContractDTO supplierContractDTO = supplierContractMapper.toContractDTO(supplierContract);
        supplierContractDTO.setSupplier(supplierDTO);

        return supplierContractDTO;
    }

    @Override
    @Transactional
    public SupplierContractDTO createContract(SupplierContractRequest supplierContractRequest) {
        // validate contract request
        validateSupplierContractRequest(supplierContractRequest);

        Supplier supplier = getSupplierInfo(supplierContractRequest.getSupplierId());
        // convert to entity
        SupplierContract supplierContract = supplierContractMapper.toContract(supplierContractRequest);

        // save to database
        supplierContract.setValidYn(EnumValidYn.Y);
        supplierContract.setApproveStatusInfo(supplierContractRequest.getApproveStatusCode(), null);

        log.info("Save Contract with supplierId: {}", supplier.getId());
        supplierContract = supplierContractRepository.save(supplierContract);

        // case approveStatus not null => create supplierContractApproveHistory
        saveSupplierContractApproveHistory(supplierContract.getId(), supplierContractRequest.getApproveStatusCode());

        SupplierContractDTO supplierContractDTO = supplierContractMapper.toContractDTO(supplierContract);
        supplierContractDTO.setSupplier(supplierMapper.toSupplierDTO(supplier));

        return supplierContractDTO;
    }

    @Override
    @Transactional
    public SupplierContractDTO updateContract(Integer id, SupplierContractRequest supplierContractRequest) {
        // validate contract request
        validateSupplierContractRequest(supplierContractRequest);

        log.info("Find Contract with id: {}", id);
        SupplierContract supplierContractOld = supplierContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        // validate status contractOld
        validateStatusContractForUpdateContract(supplierContractOld);

        Supplier supplier = getSupplierInfo(supplierContractRequest.getSupplierId());

        // convert to entity
        SupplierContract supplierContract = supplierContractMapper.toContract(supplierContractRequest);

        // save to database
        supplierContract.setId(id);
        supplierContract.setValidYn(EnumValidYn.Y);
        supplierContract.setRejectId(supplierContractOld.getRejectId());
        supplierContract.setRejectDate(supplierContractOld.getRejectDate());
        supplierContract.setRejectReason(supplierContractOld.getRejectReason());
        if (Objects.nonNull(supplierContractRequest.getApproveStatusCode())) {
            supplierContract.setApproveStatusInfo(supplierContractRequest.getApproveStatusCode(), null);
        }

        log.info("Save Contract with id: {} and supplierId: {}", id, supplier.getId());
        supplierContract = supplierContractRepository.save(supplierContract);

        // case approveStatus not null => create supplierContractApproveHistory
        saveSupplierContractApproveHistory(supplierContract.getId(), supplierContractRequest.getApproveStatusCode());

        SupplierContractDTO supplierContractDTO = supplierContractMapper.toContractDTO(supplierContract);
        supplierContractDTO.setSupplier(supplierMapper.toSupplierDTO(supplier));

        return supplierContractDTO;
    }

    private Supplier getSupplierInfo(String supplierId) {
        log.info("Find Supplier with SupplierId: {}", supplierId);
        Supplier supplier = supplierRepository.findByIdAndValidYn(supplierId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));
        log.info("Check supplier has been approved with customerId: {}", supplierId);
        if (!ApproveStatus.APPRV.equals(supplier.getApproveStatusCode()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.approved"), HttpStatus.BAD_REQUEST);

        return supplier;
    }

    @Override
    public Integer deleteContractById(Integer id) {
        log.info("Find Contract with id: {}", id);
        SupplierContract supplierContract = supplierContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));
        // validate status contractOld
        validateStatusContractForUpdateContract(supplierContract);

        supplierContract.setValidYn(EnumValidYn.N);
        log.info("Delete Contract with id: {}", id);
        supplierContractRepository.save(supplierContract);
        return id;
    }

    @Override
    public Page<FilterSearchSupplierContract> searchContract(FilterSearchAdmin filterSearchAdmin) {
        int page = ObjectUtils.isEmpty(filterSearchAdmin.getPage()) ? 0 : filterSearchAdmin.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchAdmin.getPageSize()) ? 10 : filterSearchAdmin.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("Search list contract");
        List<FilterSearchSupplierContract> supplierContractDTOS = supplierContractRepository.searchContract(filterSearchAdmin, pageable);
        long countContract = 0;
        if (!CollectionUtils.isEmpty(supplierContractDTOS)) {
            log.info("Count total contract");
            countContract = supplierContractRepository.countContract(filterSearchAdmin, pageable);
        }

        return new PageImpl<>(supplierContractDTOS, pageable, countContract);
    }

    @Override
    @Transactional
    public Integer approveStatusContract(Integer id, ApproveRequest approveRequest) {
        log.info("Find Contract with id: {}", id);
        SupplierContract supplierContract = supplierContractRepository
                .findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));


        // validate status contractOld
        validateStatusContractForUpdateContract(supplierContract);
        // validate before approve or reject
        validateBeforeApproveOrReject(supplierContract);

        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

        ApproveStatus approveStatusCode = approveRequest.getApproveStatusCode();
        switch (approveStatusCode) {
            case APPRV: {
                supplierContract.setApproveId(userPrincipal.getId());
                supplierContract.setApproveDate(new Date());
                break;
            }
            case REJCT: {
                supplierContract.setRejectId(userPrincipal.getId());
                supplierContract.setRejectDate(new Date());
                supplierContract.setRejectReason(approveRequest.getRejectReason());
                break;
            }
            default: {
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.contract.approve.status.code.not.valid"),
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        log.info("Save update approve status contract with id: {}", id);
        supplierContract.setApproveStatusCode(approveStatusCode);
        supplierContractRepository.save(supplierContract);

        log.info("Save ContractApproveHistory with contractId: {} and status: {}", id, approveStatusCode);
        supplierContractApproveHistoryRepository.save(SupplierContractApproveHistory.builder()
                .supplierContractId(id)
                .approveStatusCode(approveStatusCode)
                .rejectReason(ApproveStatus.REJCT.equals(approveRequest.getApproveStatusCode())
                        ? supplierContract.getRejectReason() : null)
                .build());

        return id;
    }

    private void validatePermission(SupplierContract supplierContract) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminType = user.getAdminType();
        EnumRole role = Enum.valueOf(EnumRole.class, adminType);
        String adminCorpId = user.getAdminCorpId();

        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_SUPPLIER: {
                if (!adminCorpId.equals(supplierContract.getSupplierId())) {
                    log.info("AdminCorpId {} does not have permission!", adminCorpId);
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.FORBIDDEN);
                }
                break;
            }
            default:
                log.info("Account {} does not have permission!", user.getId());
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                        HttpStatus.FORBIDDEN);
        }
    }

    private void validateBeforeApproveOrReject(SupplierContract supplierContract) {
        if (!ApproveStatus.REQ.equals(supplierContract.getApproveStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.contract.not.was.request"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSupplierContractRequest(SupplierContractRequest supplierContractRequest) {
        if (Objects.nonNull(supplierContractRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(supplierContractRequest.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.approve.status.code.not.valid"), HttpStatus.BAD_REQUEST);
        }
        Date startDate = DateUtils.atStartOfDay(supplierContractRequest.getStartDate());
        Date endDate = DateUtils.atEndOfDay(supplierContractRequest.getEndDate());
        // validate StartDate and EndDate of the goods
        if (endDate.before(startDate)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.end.date.before.start.date"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateStatusContractForUpdateContract(SupplierContract supplierContract) {
        ApproveStatus approveStatus = supplierContract.getApproveStatusCode();
        if (ApproveStatus.APPRV.equals(approveStatus)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.is.approved"), HttpStatus.BAD_REQUEST);
        }
    }

    private void saveSupplierContractApproveHistory(Integer supplierContractId, ApproveStatus approveStatus) {
        if (Objects.nonNull(approveStatus)) {
            log.info("Create record SupplierContractApproveHistory by supplierContractId: {}", supplierContractId);
            supplierContractApproveHistoryRepository.save(SupplierContractApproveHistory.builder()
                    .supplierContractId(supplierContractId)
                    .approveStatusCode(approveStatus)
                    .build());
        }
    }
}
