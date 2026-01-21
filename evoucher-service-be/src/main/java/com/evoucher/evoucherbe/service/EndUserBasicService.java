package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.SMSType;
import com.evoucher.evoucherbe.config.PropertyConverter;
import com.evoucher.evoucherbe.dto.EndUserDto;
import com.evoucher.evoucherbe.entity.EndUser;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.EndUserMapper;
import com.evoucher.evoucherbe.repository.EndUserRepository;
import com.evoucher.evoucherbe.service.request.EndUserRequest;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EndUserBasicService extends EntityService<EndUser, Long, EndUserDto> {
    @Getter
    private final EndUserRepository repository;
    private final EndUserMapper mapper;
    private final PropertyConverter propertyConverter;
    public static class ErrorCode {
        private ErrorCode(){}
        public static final String NOT_FOUND = "evoucher.voucher.not.found";
    }
//    @Override
//    public JpaRepository<EndUser, String> getRepository() {
//        return repository;
//    }

    @Override
    public String getEntityType() {
        return "end user";
    }
    @Override
    public EntityNotFoundException getNotFoundException(Long id) {
        return new EntityNotFoundException(MessageUtils.getMessage(ErrorCode.NOT_FOUND));
    }

    @Override
    public EndUser toEntity(EndUserDto dto) {
        return mapper.mapDtoToEntity(dto);
    }

    @Override
    public EndUserDto toDto(EndUser entity) {
        return mapper.mapEntityToDto(entity);
    }

    public EndUserDto findDtoByFindUserByMobileNumber(String mobileNumber) throws CustomCodeException {
        EndUser entity = repository.findByUserMobileNum(mobileNumber)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.end.user.not.found"),
                        HttpStatus.BAD_REQUEST));
        return mapper.mapEntityToDto(entity);
    }

    /**
     *
     * @param mobileNumber encrypted mobile number
     * @param smsType publish sms type
     * @param customerName id of publish customer
     * @return user info
     * @throws EntityNotFoundException user doesn't exist
     */

    public EndUserDto findPublishUser(String mobileNumber, SMSType smsType, String customerName) throws EntityNotFoundException {
        log.info("find user by user mobile number {}, and publish type: {} and customer name: {}", mobileNumber, smsType, customerName);
        EndUserDto result = findDtoByFindUserByMobileNumber(mobileNumber);
        if (PublishBasicService.useCustomerNameInsteadOfUserNameBySmsType(smsType)) {
            // sms type {} use customer name instead of user name
            result.setUserNm(customerName);
        }

        return result;
    }

    public EndUserDto saveUserByUserRequest(EndUserRequest request) throws CustomCodeException {
        log.info("save user by request: {}",request);
        EndUser entity = mapper.toEntity(request);
        entity = this.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    public EndUserDto saveDto(EndUserDto dto) throws CustomCodeException {
        log.info("save dto: {}", dto);
        try {
            EndUser entity = mapper.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return toDto(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public EndUserDto updatePhoneNumberAndGet(Long id, String phoneNumber) throws EntityNotFoundException, CustomCodeException {
        log.info("update user {} phone number to {}", id, phoneNumber);

        try {
            EndUser user = findById(id);

            user.setUserMobileNum(phoneNumber);

            save(user);

            return toDto(user);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
