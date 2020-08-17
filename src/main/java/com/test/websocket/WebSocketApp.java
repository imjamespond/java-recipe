package com.test.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WebSocketApp {
    static Logger log = LoggerFactory.getLogger(WebSocketApp.class);

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/path", new MyWebSocketHandler());
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

    class MyWebSocketHandler implements WebSocketHandler {

        ThreadPoolExecutor worker = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

        @Override
        public Mono<Void> handle(WebSocketSession session) {
            log.info("----------------");

            Flux<WebSocketMessage> output =
                    session.receive()//	Access the stream of inbound messages.
                            .doOnNext(message -> {
                                // Do something with each message.
                                log.info("doOnNext: " + message.getPayloadAsText());
                            })
                            .concatMap(message -> {
                                // Perform nested asynchronous operations that use the message content
                                log.info("concatMap: " + message.getPayloadAsText());

                                return Mono.just(message.getPayloadAsText());
                            })
                            .map(session::textMessage);// Return a Mono<Void> that completes when receiving completes.

            return session.send(output);
        }

        /*@Override
        public Mono<Void> handle(WebSocketSession session) {
            return session.receive()
                    .doOnNext(message -> {
                        // ...
                    })
                    .concatMap(message -> {
                        return Mono.just(message);
                    })
                    .then();
        } // not sending*/

        /*@Override
        public Mono<Void> handle(WebSocketSession session) {

            Mono<Void> input = session.receive()
                    .doOnNext(message -> {
                        // ...
                    })
                    .concatMap(message -> {
                        return Mono.just(message);
                    })
                    .then();

            Flux<String> source = Flux
                    .just("foobar")
                    //.interval(Duration.ofMillis(1000l))
                    .flatMap(msg -> Mono.fromCallable(() -> {
                        sleep(3000l);
                        return msg.toString();
                    })).subscribeOn(Schedulers.parallel());

            Mono<Void> output = session.send(source.map(session::textMessage));

            return Mono.zip(input, output).then();
        }*/
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
