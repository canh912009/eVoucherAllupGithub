package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.mapper.AdminMapper;
import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.request.CustomerRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true), uses = AdminMapper.class)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(source = "admin", target = "admin", qualifiedByName = "mapAdminBasicInfo")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "adminId", source = "request.admin.id")
    Customer toCustomer(CustomerRequest request);

    @Mapping(source = "admin", target = "admin", qualifiedByName = "mapAdminBasicInfo")
    List<CustomerDTO> toListDTO(List<Customer> customers);

    List<Customer> toListEntity(List<CustomerDTO> customerDTOS);
//    @Named("mapAdminBasicInfo")
//    default AdminDTO toAdminBasicInfo(Admin admin, @Context AdminMapper adminMapper) {
//        return adminMapper.toDtoWithBasicInfoOnly(admin);
//    }

    @Named("toCusOperatorReqInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerName", target = "customerName")
    CustomerDTO toOperatorReqInfo(Customer entity);

}
