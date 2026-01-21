package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.enums.PinDisplayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPinUploadRequest {

    @NotBlank(message = "Upload name is empty!")
    private String uploadName;

    @NotNull(message = "Goods ID is empty!")
    private Integer goodsId;

    private String uploadFilePath;

    @Size(max = 100, message = "Upload file name less than 100 characters!")
    private String uploadFileName;

    @Size(max = 500, message = "Memo less than 500 characters!")
    private String memo;

    @Valid
    @NotNull(message = "List Pin is empty!")
    private List<ExternalPinRequest> pins;

    private PinDisplayType displayType;
}
