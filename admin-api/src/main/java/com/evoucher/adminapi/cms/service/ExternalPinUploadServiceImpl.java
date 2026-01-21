package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.ExternalPinUploadRepository;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.models.ExternalPin;
import com.evoucher.adminapi.cms.dao.models.ExternalPinUpload;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.ExternalPinUploadDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.ExternalPinUploadRequest;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.DataUtils;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalPinUploadServiceImpl implements ExternalPinUploadService {

    private final ExternalPinUploadRepository externalPinUploadRepository;

    private final GoodsRepository goodsRepository;


    @Override
    public ExternalPinUploadDTO findExternalPinUploadById(Integer id) {
        log.info("Find ExternalPinUpload by ID: {}", id);
        ExternalPinUpload externalPinUpload = externalPinUploadRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.external.pin.upload.not.found"),
                        HttpStatus.BAD_REQUEST));
        Integer goodsId = externalPinUpload.getGoodsId();
        log.info("Find Goods with goodsId: {}", goodsId);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.goods.not.found"),
                        HttpStatus.BAD_REQUEST));

        // Validate permission user can create External PIN
        validatePermissionUser(goods);

        return new ExternalPinUploadDTO(externalPinUpload);
    }

    @Override
    public ExternalPinUploadDTO createExternalPinUpload(
            ExternalPinUploadRequest externalPinUploadRequest) {
        log.info("Create externalPinUpload with request: {}", externalPinUploadRequest);
        validateExternalPinUpload(externalPinUploadRequest);

        Integer goodsId = externalPinUploadRequest.getGoodsId();
        PinDisplayType displayType = externalPinUploadRequest.getDisplayType();

        log.info("Find Goods with goodsId: {}", goodsId);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.goods.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (!SystemType.EXTERNAL.equals(goods.getSystem())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.pin.goods.system.type.invalid"),
                    HttpStatus.BAD_REQUEST);
        }

        // Validate permission user can create External PIN
        validatePermissionUser(goods);

        ExternalPinUpload externalPinUpload = ExternalPinUpload.builder()
                .uploadName(externalPinUploadRequest.getUploadName())
                .goodsId(goodsId)
                .uploadFilePath(externalPinUploadRequest.getUploadFilePath())
                .uploadFileName(externalPinUploadRequest.getUploadFileName())
                .memo(externalPinUploadRequest.getMemo())
                .status(ExternalPinUploadStatus.UPLOAD_COMPLETED)
                .build();

        List<ExternalPin> externalPins = externalPinUploadRequest.getPins().stream()
                .map(
                     externalPin -> {
                         return ExternalPin.builder()
                                 .externalPinNo(externalPin.getExternalPinNo())
                                 .goodsId(goodsId)
                                 .status(ExternalPinStatus.AVAILABLE)
//                                 .externalPinType()
                                 .externalPinUpload(externalPinUpload)
                                 .expireTime(DateUtils.atEndOfDay(externalPin.getExpireTime()))
                                 .password(externalPin.getPassword())
                                 .displayType(displayType)
                                 .build();
                     })
                .collect(Collectors.toList());

        externalPinUpload.setPins(externalPins);
        externalPinUpload.setRowCount(externalPins.size());
        log.info("Save ExternalPinUpload");
        var result = externalPinUploadRepository.save(externalPinUpload);

        return new ExternalPinUploadDTO(result);
    }

    @Override
    public Page<ExternalPinUploadDTO> searchExternalPinUpload(FilterSearchCms filterSearchCms) {
        log.info("Search ExternalPinUpload with goodsId: {}", filterSearchCms.getGoodsId());

        validateSearchRequest(filterSearchCms);

        Integer goodsId = Integer.parseInt(filterSearchCms.getGoodsId());
        log.info("Find Goods with goodsId: {}", goodsId);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.goods.not.found"),
                        HttpStatus.BAD_REQUEST));
        // Validate permission user can create External PIN
        validatePermissionUser(goods);

        //pageable count from 0
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1;
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("Search ExternalPinUpload in DB with goodsId: {}", filterSearchCms.getGoodsId());
        List<ExternalPinUploadDTO> externalPinUploadDTOS =
                externalPinUploadRepository.searchExternalPinUpload(filterSearchCms, pageable);

        Long countTotal = 0L;
        if (!externalPinUploadDTOS.isEmpty()) {
            log.info("Count ExternalPinUpload for goodsId: {}", filterSearchCms.getGoodsId());
            countTotal = externalPinUploadRepository.countExternalPinUpload(filterSearchCms, pageable);
        }

        return new PageImpl<>(externalPinUploadDTOS, pageable, countTotal);
    }

    private void validateSearchRequest(FilterSearchCms filterSearchCms) {
        String goodsIdRequest = filterSearchCms.getGoodsId();

        if (Objects.isNull(goodsIdRequest)) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.goods.id.is.empty"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!NumberUtils.isCreatable(goodsIdRequest)) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.goods.id.not.number"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validatePermissionUser(Goods goods) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminCorpId = user.getAdminCorpId();
        EnumRole role = Enum.valueOf(EnumRole.class, user.getAdminType());

        log.info("Validate permission with user info: {}", user);
        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_SUPPLIER:
                String supplierId = DataUtils.getSupplierIdByBrandId(goods.getBrandId());

                if (!adminCorpId.equals(supplierId)) {
                    log.info("Account {} does not have permission!", user.getUsername());
                    throw new CustomCodeException(
                            MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.FORBIDDEN);
                }
                break;
            default:
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.account.permission"),
                        HttpStatus.FORBIDDEN);
        }
    }

    private void validateExternalPinUpload(ExternalPinUploadRequest externalPinUploadRequest) {
        log.info("Check goods already exists with goodsId: {}", externalPinUploadRequest.getGoodsId());
        if(!goodsRepository.existsById(externalPinUploadRequest.getGoodsId())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.goods.not.found"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
