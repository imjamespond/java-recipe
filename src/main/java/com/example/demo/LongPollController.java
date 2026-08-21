package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@RestController
public class LongPollController {

    // 模拟存储用户是否已经被踢
    // 实际项目可以替换成 Redis
    private final Map<String, Boolean> kickedUserCache = new ConcurrentHashMap<>();

    // 每个用户当前的 poll Sink
    private final Map<String, Sinks.Many<String>> pollSinkMap =
            new ConcurrentHashMap<>();

    /**
     * 长轮询：
     *
     * 1. 如果用户已经被踢，立即返回：
     *    401 {"status":"kicked"}
     *
     * 2. 如果没有被踢，最多等待 60 秒
     *
     * 3. 60 秒内收到 kick：
     *    立即返回 401 {"status":"kicked"}
     *
     * 4. 60 秒内没有收到 kick：
     *    返回 200 {"status":"expired"}
     */
    @GetMapping("/poll/{userId}")
    public Mono<ResponseEntity<String>> poll(@PathVariable String userId) {

        // 1. 进入 poll 时先检查一次
        if (Boolean.TRUE.equals(kickedUserCache.get(userId))) {
            return Mono.just(
                    ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body("kicked")
            );
        }

        // 2. 获取/创建当前用户的 Sink
        Sinks.Many<String> sink = pollSinkMap.computeIfAbsent(
                userId,
                k -> Sinks.many()
                        .multicast()
                        .onBackpressureBuffer()
        );

        // 3. 等待 kick 事件，最多 60 秒
        Mono<String> kickEventMono = sink.asFlux()
                .next()
                .timeout(Duration.ofSeconds(60));

        return kickEventMono
                // 收到 kick 事件
                .map(event ->
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body("kicked")
                )

                // 60 秒没有收到事件
                .onErrorResume(
                        TimeoutException.class,
                        e -> Mono.just(
                                ResponseEntity
                                        .ok()
                                        .body("expired")
                        )
                )

                // 清理当前 Sink
                .doFinally(signalType ->
                        pollSinkMap.remove(userId, sink)
                );
    }

    /**
     * 踢用户。
     *
     * 如果用户当前正在 poll：
     * 立即唤醒 poll。
     */
    @PostMapping("/kick/{userId}")
    public ResponseEntity<String> kickUser(
            @PathVariable String userId) {

        // 1. 先记录 kicked 状态
        kickedUserCache.put(userId, true); // true 改为token, poll时校验token不匹配则返回kicked

        // 2. 找到当前正在等待的 poll
        Sinks.Many<String> sink = pollSinkMap.get(userId);

        if (sink != null) {

            // 立即发送 kick 事件
            Sinks.EmitResult result =
                    sink.tryEmitNext("KICKED_BY_OTHER_LOGIN");

            // 可选：记录异常情况
            if (result.isFailure()) {
                System.out.println(
                        "Failed to emit kick event for user "
                                + userId
                                + ", result=" + result
                );
            }
        }

        return ResponseEntity.ok("User kicked");
    }
}