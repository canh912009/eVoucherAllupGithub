package com.castis.publishservice.controller;

import com.castis.publishservice.dto.request.CancelRequest;
import com.castis.publishservice.dto.request.PublishRequest;
import com.castis.publishservice.service.ProcessService;
import com.castis.publishservice.utils.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/publish")
@Slf4j
public class PublishServiceController {
    private final ProcessService service;

    @PostMapping("")
    void publish(@RequestBody @NonNull PublishRequest publishRequest) {
        log.info("Received request to publish={}", Utils.toJson(publishRequest));
        service.publish(publishRequest);
    }

    @PutMapping("/cancel")
    void cancelPublishing(@RequestBody List<CancelRequest> request) {
        log.info("Received request to cancel publishing={}", Utils.toJson(request));
        service.cancelPublishing(request);
    }
}
