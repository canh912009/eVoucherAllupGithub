package com.evoucher.adminapi.good.service.model.ur_box;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class UrBoxBrand {
    // Getters and setters
    private String banner;
    private String description;
    private String images;
    private String id;
    @JsonProperty("cat_id")
    private String catId;
    private String title;
    @JsonProperty("gift_count")
    private int giftCount;
    @JsonProperty("cat_title")
    private String catTitle;
    @JsonProperty("parent_cat_id")
    private String parentCatId;
}
