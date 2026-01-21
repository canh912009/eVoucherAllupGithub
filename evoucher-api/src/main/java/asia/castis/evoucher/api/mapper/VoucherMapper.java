package asia.castis.evoucher.api.mapper;

import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.entity.EVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);
    VoucherModel copy(VoucherModel source);
    VoucherResponse toVoucherResponse(EVoucher voucher);
}
