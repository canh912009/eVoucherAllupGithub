package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.cms.dao.models.ExternalPin;
import com.evoucher.adminapi.cms.dao.models.ExternalPinUpload;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalPinUploadDTO extends BaseDTO {

    private Integer id;

    private String uploadName;

    private Integer goodsId;

    private String uploadFilePath;

    private String uploadFileName;

    private Integer rowCount;

    private String memo;

    private String status;

    private List<ExternalPinDTO> pins;

    public ExternalPinUploadDTO(ExternalPinUpload externalPinUpload) {
        if ( externalPinUpload == null ) {
            return;
        }

        this.setRegId(externalPinUpload.getRegId());
        this.setRegDt(externalPinUpload.getRegDt());
        this.setUpdtId(externalPinUpload.getUpdtId());
        this.setUpdtDt(externalPinUpload.getUpdtDt());
        this.setId(externalPinUpload.getId());
        this.setUploadName(externalPinUpload.getUploadName());
        this.setGoodsId(externalPinUpload.getGoodsId());
        this.setUploadFilePath(externalPinUpload.getUploadFilePath());
        this.setUploadFileName(externalPinUpload.getUploadFileName());
        this.setRowCount(externalPinUpload.getRowCount());
        this.setMemo(externalPinUpload.getMemo());
        this.setStatus(externalPinUpload.getStatus().name());
        this.setRegId(externalPinUpload.getRegId());
        this.setRegDt(externalPinUpload.getRegDt());
        this.setUpdtId(externalPinUpload.getUpdtId());
        this.setUpdtDt(externalPinUpload.getUpdtDt());
        this.setPins( externalPinListToExternalPinDTOList(externalPinUpload.getPins()));
    }

    private List<ExternalPinDTO> externalPinListToExternalPinDTOList(List<ExternalPin> list) {
        if ( list == null ) {
            return Collections.emptyList();
        }

        return list.stream()
                .map(externalPin -> {
                    ExternalPinDTO externalPinDTO = new ExternalPinDTO();
                    externalPinDTO.setRegId(externalPin.getRegId());
                    externalPinDTO.setRegDt(externalPin.getRegDt());
                    externalPinDTO.setUpdtId(externalPin.getUpdtId());
                    externalPinDTO.setUpdtDt(externalPin.getUpdtDt());
                    externalPinDTO.setId(externalPin.getId());
                    externalPinDTO.setExternalPinNo(externalPin.getExternalPinNo());
                    externalPinDTO.setGoodsId(externalPin.getGoodsId());
                    externalPinDTO.setStatus(externalPin.getStatus());
                    externalPinDTO.setExpireTime(externalPin.getExpireTime());
                    externalPinDTO.setPassword(externalPin.getPassword());
                    return externalPinDTO;
                }).collect(Collectors.toList());
    }
}
