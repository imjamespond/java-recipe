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
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@SpringBootApplication
public class WebSocketApp {
    static Logger log = LoggerFactory.getLogger(WebSocketApp.class);

    @Bean
    MsgConsumer getPublisher(){
        return new MsgConsumer();
    };

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws", new MyWebSocketHandler(getPublisher()));
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
        private final Flux<Msg> publisher;
        private final Disposable disposable;

        public MyWebSocketHandler(MsgConsumer consumer) {
            this.consumer = consumer;
            this.publisher = Flux.create(consumer).share();
            this.disposable = this.publisher.subscribe();//设置subscribers防止为零被dispose
        }

        // http://kojotdev.com/2019/08/spring-webflux-websocket-with-vue-js/
        @Override
        public Mono<Void> handle(WebSocketSession session) {
            HandshakeInfo info = session.getHandshakeInfo();

            final User user = new User();
            List<String> keys = info.getHeaders().get("Sec-WebSocket-Key");
            if (keys.size() > 0) {
                user.key = keys.get(0);
                log.info(user.key);
            }

            return session
                    .receive()
                    .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
                    .doOnNext(msg -> {
                        user.setName(msg);
                        consumer.push(new Msg(user.key, msg));
                    })
                    .doOnComplete(() -> consumer.push(new Msg(user.key, user.name+" left")))
                    .zipWith(session.send(publisher
                            .filter(msg -> filter(user.key, msg))
                            .map(msg -> session.textMessage(msg.msg))))
                    .then();
        }
    }

    boolean filter(String key, Msg msg) {
        if (key != null && msg != null && msg.key != null && key.compareTo(msg.key) == 0) {
            return false;//Do not send to self
        }
        try {
            return Double.valueOf(msg.msg) > .5;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    void sleep(long millis) {
        log.debug("====================");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class User {
        public String key = null;
        public String name = null;

        public void setName(String name) {
            if (name != null && name.startsWith("name:")) {
                this.name = name;
            }
        }
    }

    class Msg {
        public String key = null;
        public String msg = null;

        public Msg(String key, String msg) {
            this.key = key;
            this.msg = msg;
        }
    }

    class MsgConsumer implements Consumer<FluxSink<Msg>> {
        private final Logger log = LoggerFactory.getLogger(MsgConsumer.class);
        private final BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();
        private final Executor executor = Executors.newSingleThreadExecutor();

        public boolean push(Msg msg) {
            return queue.offer(msg);
        }

        @Override
        public void accept(FluxSink<Msg> sink) {
            this.executor.execute(() -> {
                while (true) {
                    final Msg msg;
                    try {
                        msg = queue.take();
                        sink.next(msg);
                        log.info(msg.msg);
                    } catch (InterruptedException e) {
                        log.error("Could not take msg from queue", e);
                    }
                }
            });
        }
    }

}
