package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Code;
import com.evoucher.adminapi.auth.dao.models.CodeGroup;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
import com.evoucher.adminapi.auth.service.models.CodeGroupRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CodeGroupMapper {

    CodeGroupMapper INSTANT = Mappers.getMapper(CodeGroupMapper.class);

    CodeGroup toCodeGroup(CodeGroupDTO codeGroupDTO);

    CodeGroup toCodeGroup(CodeGroupRequest codeGroupRequest);

    CodeGroupDTO toCodeGroupDTO(CodeGroup codeGroup);

    @Mappings({
            @Mapping(source = "codes", target = "codes")
    })
    CodeGroupDTO toCodeGroupDTO(CodeGroup codeGroup, List<Code> codes);
}
