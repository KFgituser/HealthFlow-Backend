package com.healthflow.healthflowbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice // ✅ 核心注解
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        // 打印完整堆栈，方便你在 Render 后台看到
        e.printStackTrace();

        // 把错误信息返回给前端/JMeter
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("❌ Server error: " + e.getMessage());
    }
}