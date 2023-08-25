package org.example;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/long-polling")
public class LongPollingController {

    private static final Cache<String, DeferredResult<String>> SETTABLE_FUTURE_LOADING_CACHE = CacheBuilder.newBuilder().build();

    /**
     * 模拟长轮询的请求
     *
     * @param requestId 在该用例中，使用该值做请求标识，用于注册、区别DeferredResult实例
     * @return 返回DeferredResult实例给Spring框架
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<String> longPolling(@RequestParam("requestId") String requestId) {
        // 设定30秒超时，如果30秒内没有设置值，则直接返回timeoutResult指定的值
        DeferredResult<String> result = new DeferredResult<>(30_000L, "{\"error\":\"timeout\"}");
        if (null == requestId) {
            // 如果缺少RequestId直接返回错误
            result.setResult("{\"error\":\"Missing important properties.\"}");
            return result;
        }
        // 将请求的延期返回的值注册到cache中，以便后续流程可以找到相应的实例，进行值设置。
        SETTABLE_FUTURE_LOADING_CACHE.put(requestId, result);
        // 设置完成的回调函数，以便完成了之后，将该实例从缓存中清除。
        result.onCompletion(() -> SETTABLE_FUTURE_LOADING_CACHE.invalidate(requestId));
        return result;
    }

    /**
     * 模拟延时响应
     *
     * @param body      模拟返回值
     * @param requestId 长轮询中的RequestId。
     */
    @PostMapping
    public void response(@RequestBody Map<?, ?> body, @RequestParam("requestId") String requestId) {
        // 根据RequestId找到之前请求的DeferredResult实例
        final DeferredResult<String> responseFuture = SETTABLE_FUTURE_LOADING_CACHE.getIfPresent(requestId);
        if (null == responseFuture) {
            return;
        }
        Map map = new HashMap<String,Object>();
        map.put("time", Long.valueOf(System.currentTimeMillis()) );
        map.put("body", body);
        // 将值设置到DeferredResult的实例中
        String json = new Gson().toJson(map);
        System.out.println(json);
        responseFuture.setResult(json);
    }
}