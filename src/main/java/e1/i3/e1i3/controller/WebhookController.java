package e1.i3.e1i3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> createWebHook(@RequestBody Object eventData) {
        // 이벤트 처리 로직을 작성합니다.
        // eventData에는 이벤트에 관련된 데이터가 포함되어 있습니다.
        // 필요한 처리를 수행하고 응답을 반환합니다.

        // 예시: 이벤트 데이터 출력
        System.out.println("Received event data: " + eventData);

        // 예시: 성공적인 응답 반환
        return ResponseEntity.ok("Event handled successfully");
    }
}