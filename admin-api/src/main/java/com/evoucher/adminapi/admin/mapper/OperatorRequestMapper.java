package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.OperatorRequest;
import com.evoucher.adminapi.admin.service.models.OperatorRequestDto;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.models.VoucherApprovalRequest;
import com.evoucher.adminapi.admin.service.models.VoucherDto;
import com.evoucher.adminapi.auth.mapper.AdminMapper;
import com.evoucher.adminapi.cms.mapper.CustomerMapper;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {
        CustomerMapper.class,
        VoucherMapper.class,
        PublishMapper.class,
        AdminMapper.class
})
public interface OperatorRequestMapper {
    OperatorRequest toEntity(OperatorRequestDto dto);
    @Mapping(source = "requestedAdmin", target = "requestedAdmin", qualifiedByName = "mapAdminBasicInfo")
    @Mapping(source = "approvedAdmin", target = "approvedAdmin", qualifiedByName = "mapAdminBasicInfo")
    OperatorRequestDto toDto(OperatorRequest entity);

    /**
     * map publish id, good id from voucher
     * @param request target mapping
     * @param voucher source mapping
     */
    @Mapping(target = "publishId", source = "publishId")
    @Mapping(target = "goodsId", source = "goodsId")
    void updateInfoFromVoucher(@MappingTarget OperatorRequestDto request, VoucherDto voucher);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "reqStatus", source = "reqStatus")
    @Mapping(target = "approveMemo", source = "approveMemo")
    void updateApprovalInfo(@MappingTarget OperatorRequestDto request, OperatorRequestDto approveInfo);

    @Mapping(target = "customer", source = "customer")
    VoucherApprovalRequest toVoucherRequests(VoucherDto voucher,
                                             PublishDTO publish,
                                             CustomerDTO customer);

}
