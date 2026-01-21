package asia.castis.evoucher.api.mapper;

import asia.castis.evoucher.api.elastic.response.ElasticVoucherResponse;
import asia.castis.evoucher.api.elastic.response.UserVoucherDetailResponse;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElasticVoucherMapper {
    ElasticVoucherMapper INSTANCE = Mappers.getMapper(ElasticVoucherMapper.class);
    VoucherModel copy(VoucherModel source);
    ElasticVoucherResponse toVoucherResponse(VoucherModel voucherModel);
    UserVoucherDetailResponse toUserVoucherResponse(VoucherModel voucherModel);

}
