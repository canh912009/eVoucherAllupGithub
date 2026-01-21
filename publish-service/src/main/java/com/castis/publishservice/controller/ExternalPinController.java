package com.castis.publishservice.controller;

import com.castis.publishservice.dto.request.ExternalPinGiftPopRequest;
import com.castis.publishservice.service.ExtPinService;
import com.castis.publishservice.utils.Utils;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external-pins")
@Slf4j
public class ExternalPinController {

    private final ExtPinService extPinService;

    @PostMapping("/gift-pop")
    public ResponseEntity<List<Long>> getListExternalPinGiftPop(@RequestBody ExternalPinGiftPopRequest request) {
        log.info("Start get Pin GiftPop: {}", Utils.toJson(request));
        List<Long> extPins = extPinService.getList3rdPartyExternalPin(request, SystemType.GIFTPOP);
        return new ResponseEntity<>(extPins, HttpStatus.OK);
    }

    @PostMapping("/ur-box")
    public ResponseEntity<List<Long>> getListUrBoxExternalPin(@RequestBody ExternalPinGiftPopRequest request) {
        log.info("Start get Pin Urbox: {}", Utils.toJson(request));
        List<Long> extPins = extPinService.getList3rdPartyExternalPin(request, SystemType.UR_BOX);
        return new ResponseEntity<>(extPins, HttpStatus.OK);
    }

    @PostMapping("/watane")
    public ResponseEntity<List<Long>> getListWataneExternalPin(@RequestBody ExternalPinGiftPopRequest request) {
        log.info("Start get Pin Watane: {}", Utils.toJson(request));
        List<Long> extPins = extPinService.getList3rdPartyExternalPin(request, SystemType.WATANE);
        return new ResponseEntity<>(extPins, HttpStatus.OK);
    }
}
