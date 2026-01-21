package com.castis.pos_api.service.basic;

import com.castis.pos_api.dto.VoucherDto;
import com.castis.pos_api.entity.Voucher;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.mapper.VoucherMapper;
import com.castis.pos_api.repositories.VoucherRepository;
import com.castis.pos_api.service.common.EntityService;
import com.castis.pos_api.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherBasicService extends EntityService<Voucher, String, VoucherDto> {
    private final VoucherRepository repository;
    private static final VoucherMapper MAPPER = VoucherMapper.INSTANCE;

    @Override
    public JpaRepository<Voucher, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "voucher";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException("can not find voucher by id: " + id);
    }

    @Override
    public Voucher toEntity(VoucherDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public VoucherDto toDto(Voucher entity) {
        return MAPPER.toDto(entity);
    }

    public List<VoucherDto> findUsableVoucherBySerialNumber(@NotNull String serialNumber) throws ApplicationException {
        log.info("find usable voucher by serial number={}", serialNumber);

        try {
            List<Voucher> vouchers = repository.findAllBySerialNo(serialNumber);

            List<VoucherDto> result = vouchers.stream().map(this::toDto).collect(Collectors.toList());

            log.info("found: {}", result.stream().map(VoucherDto::getId));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public VoucherDto findDtoById(String id) throws ApplicationException {
        try {
            return toDto(findById(id));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Voucher not found");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
}
