package com.castis.pos_api.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommonClient {
    private final RestTemplate restTemplate;

//    public <I, O, E>
}
