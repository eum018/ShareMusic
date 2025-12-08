package com.sharemusic.sharemusicserver.controllers;

import com.sharemusic.sharemusicserver.services.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    //클라이언트 구독 / 파일 전송 요청
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {
        System.out.println("connect 컨트롤러");
        return sseService.connect(userId);
    }


    //파일 전송
    @PostMapping("/send")
    public ResponseEntity<Map<String, Boolean>> sendFile(@RequestParam("user_id") String userId,
                                           @RequestParam("file") MultipartFile file,
                                                         @RequestParam("user_name") String user_name,
                                                         @RequestParam("file_name") String file_name) {

        System.out.println("send 컨트롤러");
        Boolean success = sseService.sendFile(userId, file, user_name, file_name);

        System.out.println("성공 여부 : " + success);

        Map<String,Boolean> response = new HashMap<>();
        response.put("status", success);


        return ResponseEntity.ok(response);

    }
}
