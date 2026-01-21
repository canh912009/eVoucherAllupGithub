package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.admin.dao.models.Publish;
import com.evoucher.adminapi.auth.dao.AdminRepository;
import com.evoucher.adminapi.auth.dao.RoleRepository;
import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.dao.models.Role;
import com.evoucher.adminapi.auth.mapper.AdminMapper;
import com.evoucher.adminapi.auth.service.models.*;
import com.evoucher.adminapi.auth.utils.AuthDataUtils;
import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.CustomerRepository;
import com.evoucher.adminapi.cms.dao.StoreRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final SupplierRepository supplierRepository;
    private final BrandRepository brandRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private static final AdminMapper adminMapper = AdminMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO findById(String id) {
        log.info("Find Admin by id: {}", id);
        Admin admin = adminRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.admin.not.found"),
                        HttpStatus.BAD_REQUEST));

        AdminDTO adminDTO = adminMapper.toAdminDTO(admin);
        String adminCorporationName = getAdminCorporationName(admin);
        adminDTO.setAdminCorporationName(adminCorporationName);
        return adminDTO;
    }

    private String getAdminCorporationName(Admin admin) {
        if (Objects.isNull(admin.getRoleCode())) return null;

        String adminCorpId = admin.getAdminCorporationId();
        String adminCorpName = null;
        EnumRole role = EnumRole.valueOf(admin.getRoleCode());
        switch (role) {
            case ROLE_SUPPLIER:
                Supplier supplier = supplierRepository.findById(adminCorpId)
                        .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                adminCorpName = supplier.getSupplierName();
                break;
            case ROLE_BRAND:
                Brand brand = brandRepository.findById(adminCorpId)
                        .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                adminCorpName = brand.getBrandName();
                break;
            case ROLE_STORE:
                Store store = storeRepository.findById(adminCorpId)
                        .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.store.not.found"),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                adminCorpName = store.getStoreName();
                break;
            case ROLE_CUSTOMER:
                Customer customer = customerRepository.findById(adminCorpId)
                        .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                                HttpStatus.INTERNAL_SERVER_ERROR));
                adminCorpName = customer.getCustomerName();
                break;
            default:
                break;
        }
        return adminCorpName;
    }

    @Override
    public String createAdmin(AdminRequest adminRequest) {
        log.info("Check admin mobileNumber exits with mobileNumber: {}", adminRequest.getMobileNumber());
        boolean existMobileNumber = adminRepository.existsByMobileNumber(adminRequest.getMobileNumber());

        if (existMobileNumber) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.admin.phone.exist"),
                    HttpStatus.BAD_REQUEST);
        }

        // check role has been existed
        checkRoleExist(adminRequest.getRoleCode());

        Admin admin = adminMapper.toAdmin(adminRequest);
        log.info("Start generate id for user with userMobileNumber: {}", adminRequest.getMobileNumber());
        String id = AuthDataUtils.generateRandomString(20);
        log.info("Result generate userId: {}", id);

        admin.setId(id);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setPasswordInitYn(EnumValidYn.Y);
        admin.setPasswordUpdateDate(new Date());
        admin.setValidYn(EnumValidYn.Y);
        admin.setLoginFailCount(0);

        log.info("Create admin with mobileNumber: {}", admin.getMobileNumber());
        admin = adminRepository.save(admin);

        return admin.getId();
    }

    private void checkRoleExist(String roleCode) {
        log.info("Check Role exists with roleCode: {}", roleCode);
        boolean existRole = roleRepository.existsByRoleCodeAndValidYn(roleCode, EnumValidYn.Y);
        if (!existRole) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.role.not.found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String updateAdmin(String id, AdminUpdateRequest adminUpdateRequest) {
        log.info("Find admin with id: {}", id);
        Admin admin = adminRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.admin.not.found"),
                        HttpStatus.BAD_REQUEST));

        // check role has been existed
        checkRoleExist(adminUpdateRequest.getRoleCode());

        // check case change mobile number
        if (!admin.getMobileNumber().equals(adminUpdateRequest.getMobileNumber())) {
            // with case change mobile number must check mobile number was exist
            log.info("Check admin mobileNumber exits with mobileNumber: {}", adminUpdateRequest.getMobileNumber());
            boolean existMobileNumber = adminRepository.existsByMobileNumber(adminUpdateRequest.getMobileNumber());

            if (existMobileNumber) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.admin.phone.exist"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        admin.setEmail(adminUpdateRequest.getEmail());
        admin.setAdminName(adminUpdateRequest.getAdminName());
        admin.setMobileNumber(adminUpdateRequest.getMobileNumber());
        admin.setTelephone(adminUpdateRequest.getTelephone());
        admin.setRoleCode(adminUpdateRequest.getRoleCode());
        admin.setAdminCorporationId(adminUpdateRequest.getAdminCorporationId());

        log.info("Update admin with id: {}", id);
        admin = adminRepository.save(admin);

        return admin.getId();
    }

    @Override
    public String deleteAdmin(String id) {
        log.info("Find admin with id: {}", id);
        Admin admin = adminRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.admin.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Remove admin with adminId: {}", id);
        admin.setValidYn(EnumValidYn.N);
        adminRepository.save(admin);

        return id;
    }

    @Override
    public Page<SearchAdminResponse> searchAdmin(FilterSearchAuth filterSearchAuth) {
        int page = Objects.nonNull(filterSearchAuth.getPage()) ? filterSearchAuth.getPage() - 1 : 0;
        int pageSize = Objects.nonNull(filterSearchAuth.getPageSize()) ? filterSearchAuth.getPageSize() : 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SearchAdminResponse> adminDTOS = adminRepository.searchAdmin(filterSearchAuth, pageable);
        long countAdmin = 0;
        if (!CollectionUtils.isEmpty(adminDTOS)) {
            countAdmin = adminRepository.countAdmin(filterSearchAuth);
        }
        return new PageImpl<>(adminDTOS, pageable, countAdmin);
    }

    @Override
    public AdminDTO getAdminCurrent() {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        return findById(user.getId());
    }

    @Override
    public void changePassword(AdminChangePasswordRequest changePasswordRequest) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        log.info("Find admin with id: {}", user.getId());
        Admin admin = adminRepository.findByIdAndValidYn(user.getId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.admin.not.found"),
                        HttpStatus.BAD_REQUEST));

        admin.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        admin.setPasswordInitYn(EnumValidYn.N);
        admin.setPasswordUpdateDate(new Date());

        log.info("Update password for adminId: {}", admin.getId());
        adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
//        log.info("Find admin by phoneNumber: {}", phoneNumber);
        Admin admin = adminRepository.findAdminByMobileNumberAndValidYn(phoneNumber, EnumValidYn.Y)
                .orElseThrow(() -> {
                    log.error("Admin not found with phoneNumber: {}", phoneNumber);
                    return new UsernameNotFoundException("User not found!");
                });

//        log.info("Find Role for Admin with phoneNumber: {}", phoneNumber);
        Optional<Role> role = roleRepository.findByRoleCodeAndValidYn(admin.getRoleCode(), EnumValidYn.Y);

//        log.info("Build UserPrincipal with user: {} and role: {}", admin, role);
        return UserPrincipal.build(admin, role.map(Set::of).orElse(null));
    }
    public void validateCorpIdPermission(String customerId) throws CustomCodeException {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminType = user.getAdminType();
        EnumRole role = Enum.valueOf(EnumRole.class, adminType);
        String adminCorpId = user.getAdminCorpId();

        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_CUSTOMER: {
                if (!adminCorpId.equals(customerId)) {
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
}
