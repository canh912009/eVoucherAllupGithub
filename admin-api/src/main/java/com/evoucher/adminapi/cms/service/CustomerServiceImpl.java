package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.CustomerApprvHistoryRepository;
import com.evoucher.adminapi.cms.dao.CustomerRepository;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.dao.models.CustomerApprvHistory;
import com.evoucher.adminapi.cms.mapper.CustomerMapper;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.CustomerRequest;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
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
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    protected static final String NOT_FOUND_ERROR_CODE = "evoucher.customer.not.found";

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final CustomerApprvHistoryRepository apprvHistoryRepository;

    @Override
    public CustomerDTO findById(String id) {
        log.info("Find Customer with id: {}", id);
        Customer customer = customerRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND_ERROR_CODE),
                        HttpStatus.BAD_REQUEST));
        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CustomerRequest customerRequest) {
        validateCustomerRequest(customerRequest);

        log.info("Create customer: {}", customerRequest);
        if (Objects.isNull(customerRequest.getId()) || customerRequest.getId().isEmpty()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("Customer id is missing"),
                    HttpStatus.BAD_REQUEST
            );
        }
        boolean supplierExists = customerRepository.existsById(customerRequest.getId());
        if (supplierExists) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("Customer with given id already exists"),
                    HttpStatus.BAD_REQUEST
            );
        }
        Customer customer = customerMapper.toCustomer(customerRequest);
        customer.setValidYn(EnumValidYn.Y);

        log.info("Create Customer with id: {}", customer.getId());
        customer = customerRepository.save(customer);

        //-- Save ApprvHistory
        saveApprvHistory(customer.getId(), customer.getApproveStatusCode(), null);

        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(String id, CustomerRequest customerRequest) {
        validateCustomerRequest(customerRequest);

        log.info("Find Customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));
        //-- Valid status code
        if (ApproveStatus.APPRV.equals(customer.getApproveStatusCode()))
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.customer.approved"),
                    HttpStatus.BAD_REQUEST);

        Customer customerNew = customerMapper.toCustomer(customerRequest);
        customerNew.setId(customer.getId());
        customerNew.setValidYn(EnumValidYn.Y);

        log.info("Update customer with customerId: {}", id);
        customerNew = customerRepository.save(customerNew);

        //-- Save ApprvHistory
        saveApprvHistory(id, customerNew.getApproveStatusCode(), null);

        return customerMapper.toDTO(customerNew);
    }

    @Override
    public String delete(String id) {
        log.info("Find Customer with id: {}", id);
        Customer customer = customerRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        customer.setValidYn(EnumValidYn.N);
        log.info("Delete Customer ValidYn NO with id: {}", id);
        customerRepository.save(customer);
        return id;
    }

    @Override
    @Transactional
    public String updateStatusCustomer(String id, ApproveRequest approveRequest) {
        // validate approve status request must be APPRV or REJECT
        ApproveStatus approveStatusCodeRequest = approveRequest.getApproveStatusCode();
        if (!ApproveStatus.APPRV.equals(approveStatusCodeRequest)
                && !ApproveStatus.REJCT.equals(approveStatusCodeRequest)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.approve.status.code.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Find Customer with customerId: {}", id);
        Customer customer = customerRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST
                ));
        // validate customer not Approved yet
        if (ApproveStatus.APPRV.equals(customer.getApproveStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.customer.approved"),
                    HttpStatus.BAD_REQUEST);
        }
        // validate supplier has been Request
        if (!ApproveStatus.REQ.equals(customer.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.was.request.approve"),
                    HttpStatus.BAD_REQUEST);
        }

        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

        customer.setApproveStatusCode(approveStatusCodeRequest);
        customer.setApproveId(ApproveStatus.APPRV.equals(approveStatusCodeRequest) ? userPrincipal.getId() : null);
        log.info("Save approve status Customer with id: {}", id);
        customerRepository.save(customer);

        // save customer approve history
        saveApprvHistory(id, approveStatusCodeRequest, approveRequest.getRejectReason());

        return id;
    }

    @Override
    public Page<CustomerDTO> searchCustomer(FilterSearchCms filterSearchCms) {
        log.info("Start Search Customer");
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<CustomerDTO> customerDTOS = customerRepository.searchCustomer(filterSearchCms, pageable);
        Long countContractDTOS = 0L;
        if (!CollectionUtils.isEmpty(customerDTOS)) {
            countContractDTOS = customerRepository.countCustomer(filterSearchCms, pageable);
        }

        return new PageImpl<>(customerDTOS, pageable, countContractDTOS);
    }

    private void validateCustomerRequest(CustomerRequest customerRequest) {
        if (Objects.nonNull(customerRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(customerRequest.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.approve.status.code.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void saveApprvHistory(String customerId, ApproveStatus approveStatusCode, String rejectReason) {
        if (Objects.nonNull(approveStatusCode)) {
            CustomerApprvHistory apprvHistory = new CustomerApprvHistory();
            apprvHistory.setCustomerId(customerId);
            apprvHistory.setApproveStatusCode(approveStatusCode);
            apprvHistory.setRejectReason(ApproveStatus.REJCT.equals(approveStatusCode) ? rejectReason : null);

            log.info("Save CustomerApproveHistory with customerId: {} and status: {}", customerId, approveStatusCode);
            apprvHistoryRepository.save(apprvHistory);
        }
    }
}
