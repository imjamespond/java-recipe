package com.test.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@SpringBootApplication
public class WebSocketApp {
    static Logger log = LoggerFactory.getLogger(WebSocketApp.class);
    
    @Autowired
    MsgConsumer publisher;

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws", new MyWebSocketHandler(publisher));
        int order = -1; // before annotated controllers

        return new SimpleUrlHandlerMapping(map, order);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public WebSocketService webSocketService() {
        ReactorNettyRequestUpgradeStrategy strategy = new ReactorNettyRequestUpgradeStrategy();
        return new HandshakeWebSocketService(strategy);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebSocketApp.class, args);
    }

    public class MyWebSocketHandler implements WebSocketHandler {

        private final MsgConsumer consumer;
        private final Flux<String> publisher;
        private final Disposable disposable;

        public MyWebSocketHandler(MsgConsumer consumer) {
            this.consumer = consumer;
            this.publisher = Flux.create(consumer).share();
            this.disposable = this.publisher.subscribe();//设置subscribers防止为零被dispose
        }

        // http://kojotdev.com/2019/08/spring-webflux-websocket-with-vue-js/
        @Override
        public Mono<Void> handle(WebSocketSession session) {
            return session
                    .receive()
                    .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
                    .doOnNext(msg -> consumer.push(msg))
                    .doOnComplete(()->consumer.push("someone left"))
                    .zipWith(session.send(publisher
                            .map(msg -> session.textMessage(msg))))
                    .then();
        }
    }

    void sleep(long millis) {
        log.debug("====================");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


@Component
class MsgConsumer implements Consumer<FluxSink<String>> {
    private static final Logger log = LoggerFactory.getLogger(MsgConsumer.class);
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final Executor executor = Executors.newSingleThreadExecutor();
    public boolean push(String msg) {
        return queue.offer(msg);
    }
    @Override
    public void accept(FluxSink<String> sink) {
        this.executor.execute(() -> {
            while (true) {
                final String msg;
                try {
                    msg = queue.take();
                    sink.next(msg);
                } catch (InterruptedException e) {
                    log.error("Could not take msg from queue", e);
                }
            }
        });
    }
}
