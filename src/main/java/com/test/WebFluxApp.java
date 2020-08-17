package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@EnableWebFlux
public class WebFluxApp {
    Logger log = LoggerFactory.getLogger(WebFluxApp.class);

    // Static Resources
    @Configuration
    public class WebConfig implements WebFluxConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/files/**")
                    .addResourceLocations("/public", "classpath:/static/")
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/hello-flux")
    public Flux<Integer> helloFlux() {
        return Flux
                .just(1, 2, 3, 4)
                .flatMap(num -> Mono
                        .fromCallable(() -> {
                            log.debug("------------------");
                            try {
                                Thread.sleep(1000l);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return num;
                        })
                        .subscribeOn(Schedulers.single()));//parallel()
        //return "Hello WebFlux";
    }

    ThreadPoolExecutor worker = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

    @GetMapping("/hello-mono")
    public Mono<String> helloMono() {
        log.debug("------------------");
        return Mono.just("Hello Mono!")
                .doOnNext(msg -> {
                    sleep(1000l);
                })
                .map(msg -> {
                    sleep(2000l);
                    return msg;
                }).subscribeOn(Schedulers.single());
//        return Mono.fromCallable(() -> {
//            return "Hello Mono!";
//        }).subscribeOn(Schedulers.parallel());
//        }).subscribeOn(new Scheduler() {
//            @Override
//            public Disposable schedule(Runnable runnable) {
//                worker.execute(runnable);
//                return null;
//            }
//
//            @Override
//            public Worker createWorker() {
//                return null;
//            }
//        });
    }

    void sleep(long millis) {
        log.debug("====================");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("reactor.netty.ioWorkerCount", "1");

        ApplicationContext context = new AnnotationConfigApplicationContext(WebFluxApp.class);
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        DisposableServer server = HttpServer
                .create()
                .host("0.0.0.0")
                .port(8080)
                .handle(adapter)
                .bindNow();

        server.onDispose()
                .block();
    }
}
