package com.sharemusic.sharemusicserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SseService {

    // 접속 중인 emitters
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 접속 전 임시 파일 저장
    private final Map<String, List<Map<String, Object>>> pendingFiles = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // SSE 연결
    public SseEmitter connect(String userId) {

        System.out.println("connect 서비스 실행 | ");

        //emmiter 추가
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);


        emitter.onCompletion(() -> {
            System.out.println("SSE 전송 완료 | user: " + userId);
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            System.out.println("SSE 시간 만료 | user: " + userId);
            emitters.remove(userId);
        });

        // 접속 전에 pending 파일이 있으면 전송
        List<Map<String, Object>> responses = pendingFiles.getOrDefault(userId, new ArrayList<>());
        for (Map<String, Object> response : responses) {
            try {
                String jsonPayload = objectMapper.writeValueAsString(response);
                emitter.send(jsonPayload, MediaType.APPLICATION_JSON);
                System.out.println("접속중인 user에게 전송, " + userId + " | file_name: " + response.get("file_name"));
            } catch (IOException e) {
                System.err.println("전달 실패,  user: " + userId);
                e.printStackTrace();
            }
        }
        pendingFiles.remove(userId);

        return emitter;
    }

    // 파일 전송
    public boolean sendFile(String userId, MultipartFile file, String user_name, String file_name) {
        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            System.err.println("파일 읽기 실패 : " + file_name);
            e.printStackTrace();
            return false;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_name", user_name);
        payload.put("file_name", file_name);
        payload.put("data", Base64.getEncoder().encodeToString(data));

        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                String jsonPayload = objectMapper.writeValueAsString(payload);
                emitter.send(jsonPayload, MediaType.APPLICATION_JSON);
                System.out.println("파일 전달 성공! | userId : " + userId + " | file_name: " + file_name);
                return true;
            } catch (IOException e) {
                System.err.println("파일 전달 실패 | userId : " + userId + " | file_name: " + file_name);
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("emitter를 찾을 수 없습니다. : " + userId);
            // 접속 전이면 pendingFiles에 저장
            pendingFiles.computeIfAbsent(userId, k -> new ArrayList<>()).add(payload);
            System.out.println("파일 임시 저장 성공 | user_id : " + userId + "file_name" + file_name);
            return false;
        }
    }
}
