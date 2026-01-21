package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.MessageTemplateService;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message_template")
public class MessageTemplateController {
    private final MessageTemplateService service;

    @GetMapping("/get-all")
    ResponseEntity<BaseResponse> getAll(
            @RequestParam(required = false) List<String> system) {
        return ResponseEntity.ok(new BaseResponse(service.findAllDTO(system)));
    }
}
