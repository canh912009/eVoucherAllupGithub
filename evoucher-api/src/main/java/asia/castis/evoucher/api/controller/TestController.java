package asia.castis.evoucher.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final RestTemplate restTemplate;

    @GetMapping()
    public ResponseEntity<Object> test() {
        restTemplate.getForObject("http://localhost:19932/voucher/test", String.class);
        return ResponseEntity.ok("");
    }
}
