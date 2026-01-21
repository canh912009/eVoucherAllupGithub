package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Code;
import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.auth.service.models.CodeRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CodeMapper {

    CodeMapper INSTANT = Mappers.getMapper(CodeMapper.class);

    Code toCode(CodeDTO codeDTO);

    Code toCode(CodeRequest codeRequest);

    CodeDTO toCodeDTO(Code code);
}
