package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.CustomerContractApproveHistoryRepository;
import com.evoucher.adminapi.admin.dao.CustomerContractRepository;
import com.evoucher.adminapi.admin.dao.models.CustomerContract;
import com.evoucher.adminapi.admin.dao.models.CustomerContractApproveHistory;
import com.evoucher.adminapi.admin.mapper.CustomerContractMapper;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.cms.mapper.CustomerMapper;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.CustomerRepository;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.CustomerType;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.enums.EnumValidYn;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerContractServiceImpl implements CustomerContractService {

    public static final String EVOUCHER_CONTRACT_NOT_FOUND = "evoucher.contract.not.found";
    private final CustomerContractRepository customerContractRepository;
    private final CustomerRepository customerRepository;
    private final CustomerContractApproveHistoryRepository customerContractApproveHistoryRepository;

    private final CustomerContractMapper customerContractMapper;
    private final CustomerMapper customerMapper;


    @Override
    public CustomerContractDTO findById(Integer id) {
        log.info("Find Contract with id: {}", id);
        CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        // validate permission
        validatePermission(customerContract);

        CustomerDTO customerDTO = customerRepository.findByIdAndValidYn(customerContract.getCustomerId(), EnumValidYn.Y)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        CustomerContractDTO customerContractDTO = customerContractMapper.toContractDTO(customerContract);
        customerContractDTO.setCustomer(customerDTO);

        return customerContractDTO;
    }

    @Override
    @Transactional
    public CustomerContractDTO createContract(CustomerContractRequest customerContractRequest) {
        // validate contract request
        validateCustomerContractRequest(customerContractRequest);

        Customer customer = getCustomerInfo(customerContractRequest.getCustomerId());

        if (CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            // Only create one campaign for customer type CHANNEL
            log.info("Check CustomerContract already exist for customerId: {}",
                    customerContractRequest.getCustomerId());
            if (customerContractRepository.existsByCustomerId(customerContractRequest.getCustomerId())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.contract.only.one.for.customer"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // convert to entity
        CustomerContract customerContract = customerContractMapper.toContract(customerContractRequest);
//        customerContract.setEndDate(DateUtils.atEndOfDay(customerContractRequest.getEndDate()));
//        customerContract.setStartDate(DateUtils.atStartOfDay(customerContractRequest.getStartDate()));

        // save to database
        customerContract.setValidYn(EnumValidYn.Y);
        customerContract.setApproveStatusInfo(customerContractRequest.getApproveStatusCode(), null);

        log.info("Save Contract with customerId: {}", customer.getId());
        customerContract = customerContractRepository.save(customerContract);


        // case approveStatus not null => create CustomerContractApproveHistory
        saveCustomerContractApproveHistory(customerContract.getId(), customerContractRequest.getApproveStatusCode());

        CustomerContractDTO customerContractDTO = customerContractMapper.toContractDTO(customerContract);
        customerContractDTO.setCustomer(customerMapper.toDTO(customer));

        return customerContractDTO;
    }

    @Override
    @Transactional
    public CustomerContractDTO updateContract(Integer id, CustomerContractRequest customerContractRequest) {
        // validate contract request
        validateCustomerContractRequest(customerContractRequest);

        log.info("Find Contract with id: {}", id);
        CustomerContract customerContractOld = customerContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        // validate status contractOld
        validateStatusContractForUpdateContract(customerContractOld);

        Customer customer = getCustomerInfo(customerContractRequest.getCustomerId());

        if (CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            // Only create one campaign for customer type CHANNEL
            log.info("Check CustomerContract already exist for customerId: {}",
                    customerContractRequest.getCustomerId());
            if (!customerContractRequest.getCustomerId().equals(customerContractOld.getCustomerId())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.contract.only.one.for.customer"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // convert to entity
        CustomerContract customerContract = customerContractMapper.toContract(customerContractRequest);

        // save to database
        customerContract.setId(id);
        customerContract.setValidYn(EnumValidYn.Y);
        customerContract.setRejectId(customerContractOld.getRejectId());
        customerContract.setRejectDate(customerContractOld.getRejectDate());
        customerContract.setRejectReason(customerContractOld.getRejectReason());
        if (Objects.nonNull(customerContractRequest.getApproveStatusCode())) {
            customerContract.setApproveStatusInfo(customerContractRequest.getApproveStatusCode(), null);
        }

        log.info("Save Contract with id: {} and customerId: {}", id, customer.getId());
        customerContract = customerContractRepository.save(customerContract);

        // case approveStatus not null => create CustomerContractApproveHistory
        saveCustomerContractApproveHistory(customerContract.getId(), customerContractRequest.getApproveStatusCode());

        CustomerContractDTO customerContractDTO = customerContractMapper.toContractDTO(customerContract);
        customerContractDTO.setCustomer(customerMapper.toDTO(customer));

        return customerContractDTO;
    }

    private Customer getCustomerInfo(String customerId) {
        log.info("Find Customer with customerId: {}", customerId);
        Customer customer = customerRepository.findByIdAndValidYn(customerId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));
        log.info("Check customer has been approved with customerId: {}", customerId);
        if (!ApproveStatus.APPRV.equals(customer.getApproveStatusCode()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.approved"), HttpStatus.BAD_REQUEST);

        return customer;
    }

    @Override
    public Integer deleteContractById(Integer id) {
        log.info("Find Contract with id: {}", id);
        CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));
        // validate status contractOld
        validateStatusContractForUpdateContract(customerContract);

        customerContract.setValidYn(EnumValidYn.N);
        log.info("Delete Contract with id: {}", id);
        customerContractRepository.save(customerContract);
        return id;
    }

    @Override
    public Page<FilterSearchCustomerContract> searchContract(FilterSearchAdmin filterSearchAdmin) {
        int page = ObjectUtils.isEmpty(filterSearchAdmin.getPage()) ? 0 : filterSearchAdmin.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchAdmin.getPageSize()) ? 10 : filterSearchAdmin.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("Search list contract");
        List<FilterSearchCustomerContract> customerContractDTOS = customerContractRepository.searchContract(filterSearchAdmin, pageable);
        long countContract = 0;
        if (!CollectionUtils.isEmpty(customerContractDTOS)) {
            log.info("Count total contract");
            countContract = customerContractRepository.countContract(filterSearchAdmin, pageable);
        }

        return new PageImpl<>(customerContractDTOS, pageable, countContract);
    }

    @Override
    @Transactional
    public Integer approveStatusContract(Integer id, ApproveRequest approveRequest) {
        log.info("Find Contract with id: {}", id);
        CustomerContract customerContract = customerContractRepository
                .findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_CONTRACT_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        // validate status contractOld
        validateStatusContractForUpdateContract(customerContract);
        // validate before approve or reject
        validateBeforeApproveOrReject(customerContract);

        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

        ApproveStatus approveStatusCode = approveRequest.getApproveStatusCode();
        switch (approveStatusCode) {
            case APPRV: {
                customerContract.setApproveId(userPrincipal.getId());
                customerContract.setApproveDate(new Date());
                break;
            }
            case REJCT: {
                customerContract.setRejectId(userPrincipal.getId());
                customerContract.setRejectDate(new Date());
                customerContract.setRejectReason(approveRequest.getRejectReason());
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
        customerContract.setApproveStatusCode(approveStatusCode);
        customerContractRepository.save(customerContract);

        log.info("Save ContractApproveHistory with contractId: {} and status: {}", id, approveStatusCode);
        customerContractApproveHistoryRepository.save(CustomerContractApproveHistory.builder()
                .customerContractId(id)
                .approveStatusCode(approveStatusCode)
                .rejectReason(customerContract.getRejectReason())
                .build());

        return id;
    }

    private void validatePermission(CustomerContract customerContract) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminType = user.getAdminType();
        EnumRole role = Enum.valueOf(EnumRole.class, adminType);
        String adminCorpId = user.getAdminCorpId();

        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_CUSTOMER: {
                if (!adminCorpId.equals(customerContract.getCustomerId())) {
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

    private void validateBeforeApproveOrReject(CustomerContract customerContract) {
        if (!ApproveStatus.REQ.equals(customerContract.getApproveStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.contract.not.was.request"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateCustomerContractRequest(CustomerContractRequest customerContractRequest) {
        if (Objects.nonNull(customerContractRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(customerContractRequest.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.approve.status.code.not.valid"), HttpStatus.BAD_REQUEST);
        }
        Date startDate = customerContractRequest.getStartDate();
        Date endDate = customerContractRequest.getEndDate();
        // validate StartDate and EndDate of the goods
        if (endDate.before(startDate)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.end.date.before.start.date"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateStatusContractForUpdateContract(CustomerContract customerContractOld) {
        ApproveStatus approveStatus = customerContractOld.getApproveStatusCode();
        if (ApproveStatus.APPRV.equals(approveStatus)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.is.approved"), HttpStatus.BAD_REQUEST);
        }
    }

    private void saveCustomerContractApproveHistory(Integer supplierContractId, ApproveStatus approveStatus) {
        if (Objects.nonNull(approveStatus)) {
            log.info("Create record SupplierContractApproveHistory by supplierContractId: {}", supplierContractId);
            customerContractApproveHistoryRepository.save(CustomerContractApproveHistory.builder()
                    .customerContractId(supplierContractId)
                    .approveStatusCode(approveStatus)
                    .build());
        }
    }
}
