//package com.evoucher.adminapi.cms.service;
//
//import com.evoucher.adminapi.cms.dao.BrandRepository;
//import com.evoucher.adminapi.cms.dao.ExternalPinUploadRepository;
//import com.evoucher.adminapi.cms.dao.GoodsRepository;
//import com.evoucher.adminapi.cms.dao.models.ExternalPin;
//import com.evoucher.adminapi.cms.dao.models.ExternalPinUpload;
//import com.evoucher.adminapi.cms.mapper.GoodsMapper;
//import com.evoucher.adminapi.cms.mapper.VnptResponseMapper;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.GoodPurchaseInfo;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.request.PurchaseRequest;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.request.ServiceBEPurchaseRequest;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.response.BrandSearchDTO;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithDateDTO;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithoutDateDTO;
//import com.evoucher.adminapi.cms.service.models.vnptEPay.response.ServiceBEPurchaseResponse;
//import com.evoucher.adminapi.common.client.ServiceBEClient;
//import com.evoucher.adminapi.common.enums.ExternalPinStatus;
//import com.evoucher.adminapi.common.enums.ExternalPinUploadStatus;
//import com.evoucher.adminapi.common.exception.CustomCodeException;
//import com.evoucher.adminapi.common.message.BaseResponse;
//import com.evoucher.adminapi.common.utils.Constant;
//import com.evoucher.adminapi.common.utils.MessageUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.evoucher.adminapi.common.utils.DataUtils.getPageInfo;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class VNPTIntegrateServiceImpl /*implements VnptIntegrateService*/{
//    @Value("${vnpt-epay.supplier-id}")
//    private String vnptSupplierId;
//
//    private final BrandRepository brandRepository;
//    private final GoodsRepository goodsRepository;
//    private final ServiceBEClient beClient;
//    private final ExternalPinUploadRepository externalPinUploadRepository;
//    private final ObjectMapper objectMapper;
//
//    private static final GoodsMapper goodMapper = GoodsMapper.INSTANCE;
//    private static final VnptResponseMapper vnptResponseMapper = VnptResponseMapper.INSTANCE;
//
//    @Override
//    public Page<BrandSearchDTO> searchVnptBrand(String brandId, String brandTitle, Integer page, Integer pageSize) {
//        try {
//
//            log.info("search vnpt brands brandId: {}, brandName : {}, page: {}, pageSize: {}", brandId, brandTitle, page, pageSize);
//
//            Pageable pageable = getPageInfo(page, pageSize);
//
//            List<BrandSearchDTO> result = brandRepository.searchBrandByBrandIdAndBrandTitle(vnptSupplierId, brandId, brandTitle, pageable);
//            if (result.isEmpty()) {
//                return Page.empty();
//            }
//
//            long count = brandRepository.countBrandByBrandIdAndBrandTitle(vnptSupplierId, brandId, brandTitle);
//            log.info("success with {} results", count);
//            return new PageImpl<>(result, pageable, count);
//        } catch (CustomCodeException e) {
//            log.error(e.getMessage(), e);
//            throw e;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw CustomCodeException.internalException(e);
//        }
//    }
//
//    @Override
//    public Page<GiftSearchWithoutDateDTO> searchVnptGift(String brandId, Integer page, Integer pageSize, String giftTitle) {
//        try {
//            log.info("search vnpt gift by brand id: {}, page: {}, page size: {}", brandId, page, pageSize);
//
//            Pageable pageable = getPageInfo(page, pageSize);
//
//            List<GiftSearchWithDateDTO> result = new ArrayList<>(goodsRepository.searchVnptGift(vnptSupplierId, brandId, null, giftTitle, pageable));
//            List<GiftSearchWithoutDateDTO> final1 = goodMapper.toGiftSearchWithoutDate(result);
//
//            if (result.isEmpty()) {
//                return Page.empty();
//            }
//
//            Long count = goodsRepository.countVnptGift(vnptSupplierId, brandId, null, giftTitle);
//
//            return new PageImpl<>(final1, pageable, count);
//
//        } catch (CustomCodeException e) {
//            log.error(e.getMessage(), e);
//            throw e;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw CustomCodeException.internalException(e);
//        }
//    }
//
//    @Override
//    public Page<GiftSearchWithDateDTO> purchaseSearch(String brandId, String brandTitle, Integer page, Integer pageSize) {
//        try {
//            log.info("search vnpt gift by brand id: {}, page: {}, page size: {}", brandId, page, pageSize);
//
//            Pageable pageable = getPageInfo(page, pageSize);
//
//            List<GiftSearchWithDateDTO> result = new ArrayList<>(goodsRepository.searchVnptGift(vnptSupplierId, brandId, brandTitle, null, pageable));
//
//            if (result.isEmpty()) {
//                return Page.empty();
//            }
//
//            Long count = goodsRepository.countVnptGift(vnptSupplierId, brandId, brandTitle, null);
//
//            return new PageImpl<>(result, pageable, count);
//
//        } catch (CustomCodeException e) {
//            log.error(e.getMessage(), e);
//            throw e;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw CustomCodeException.internalException(e);
//        }
//    }
//
//    @Override
//    public BaseResponse checkBalance() throws CustomCodeException {
//        try {
//            return beClient.queryBalance();
//        } catch (Exception e) {
//             log.error(e.getMessage(), e);
//             throw CustomCodeException.internalException(e);
//        }
//    }
//
//    @Override
//    public void purchaseVnptGift(PurchaseRequest request) throws CustomCodeException {
//
//        //create external pin upload
//        ExternalPinUpload externalPinUpload = createVnptPinUpload(request);
//        try {
//
//            GoodPurchaseInfo purchaseInfo = goodsRepository.getGoodPurchaseInfoByGoodId(request.getGiftId(), vnptSupplierId);
//            if (purchaseInfo == null) {
//                log.error("can not find good by id: ".concat(request.getGiftId().toString()));
//                throw new CustomCodeException(
//                        MessageUtils.getMessage("evoucher.goods.not.found"),
//                        HttpStatus.BAD_REQUEST);
//            }
//            ServiceBEPurchaseRequest purchaseRequest = new ServiceBEPurchaseRequest();
//            purchaseRequest.setRequestId(externalPinUpload.getId().toString());
//            purchaseRequest.setAmount(purchaseInfo.getSellPrice().intValue());
//            purchaseRequest.setQuantity(request.getQuantity());
//            purchaseRequest.setProvider(purchaseInfo.getProvider());
//
//            //call to be service purchase gift
//            BaseResponse beResponse = beClient.downloadSoftPin(purchaseRequest);
//            log.info("be service return response of purchasing: {}", beResponse);
//
//            if (beResponse.getErrorCode().equals(Constant.SUCCESS_CODE)) {
//                ServiceBEPurchaseResponse purchaseData = objectMapper.convertValue(beResponse.getData(), ServiceBEPurchaseResponse.class) ;
//                if (purchaseData != null && purchaseData.getListCards() != null && !purchaseData.getListCards().isEmpty()) {
//                    List<ExternalPin> externalPins = purchaseData.getListCards().stream().map(o -> {
//                        ExternalPin pin = vnptResponseMapper.fromVnptCard(o);
//                        pin.setExternalPinUpload(externalPinUpload);
//                        pin.setStatus(ExternalPinStatus.AVAILABLE);
//                        pin.setGoodsId(request.getGiftId().intValue());
//                        return pin;
//                    }).collect(Collectors.toList());
//                    externalPinUpload.setRowCount(externalPins.size());
//                    externalPinUpload.setPins(externalPins);
//                    externalPinUpload.setStatus(ExternalPinUploadStatus.UPLOAD_COMPLETED);
//                    externalPinUploadRepository.save(externalPinUpload);
//                }
//                log.info("create external pin success");
//            } else {
//                log.error("be service return fail status");
//                externalPinUpload.setMemo(externalPinUpload.getMemo().concat(beResponse.getErrorCode()).concat(" ").concat(beResponse.getMessage()));
//                externalPinUpload.setStatus(ExternalPinUploadStatus.UPLOAD_FAILED);
//                externalPinUploadRepository.save(externalPinUpload);
//                throw CustomCodeException.internalException(new RuntimeException(beResponse.getMessage()));
//            }
//
//        } catch (CustomCodeException e) {
//            externalPinUpload.setMemo(externalPinUpload.getMemo().concat(" ").concat(e.getMessage()));
//            externalPinUpload.setStatus(ExternalPinUploadStatus.UPLOAD_FAILED);
//            externalPinUploadRepository.save(externalPinUpload);
//            log.error(e.getMessage(), e);
//            throw e;
//        } catch (Exception e) {
//            externalPinUpload.setMemo(externalPinUpload.getMemo().concat(" ").concat(e.getMessage()));
//            externalPinUpload.setStatus(ExternalPinUploadStatus.UPLOAD_FAILED);
//            externalPinUploadRepository.save(externalPinUpload);
//            log.error(e.getMessage(), e);
//            throw CustomCodeException.internalException(e);
//        }
//    }
//
//    private ExternalPinUpload createVnptPinUpload(PurchaseRequest request) throws CustomCodeException {
//        try {
//            ExternalPinUpload externalPinUpload = ExternalPinUpload.builder()
//                    .goodsId(request.getGiftId().intValue())
//                    .uploadName("vnpt purchase pin ".concat(request.getGiftId().toString()))
//                    .memo(request.toString())
//                    .build();
//
//            return externalPinUploadRepository.save(externalPinUpload);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw CustomCodeException.internalException(e);
//        }
//    }
//
//
//}
