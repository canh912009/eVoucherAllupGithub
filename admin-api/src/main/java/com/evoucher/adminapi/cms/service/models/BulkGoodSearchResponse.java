package com.evoucher.adminapi.cms.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkGoodSearchResponse extends SearchGroupResponse{
    private List<String> brandIds;
    private List<String> categoryIds;
}
