package com.metasoft;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class AppStart {
    public static void main(String[] args) {
    	String CONTEXT = "/configuration/apps/";
    	String KEY = "test.foo";
    	String PATH = CONTEXT+KEY.replace('.', '/');
    	String connectString = "yy:2181";
    	CuratorFramework curator = CuratorFrameworkFactory.builder()
				.retryPolicy(new RetryOneTime(500)).connectString(connectString).build();
		curator.start();
		
		StringBuilder create = new StringBuilder(1024);
		try {
			curator.delete().deletingChildrenIfNeeded().forPath(PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			create.append(curator.create().creatingParentsIfNeeded()
					.forPath(PATH, "hello".getBytes())).append('\n');
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		curator.close();
		System.out.println(create);
    	
		ConfigurableApplicationContext  context = 
				SpringApplication.run(AppStart.class, args);
		Environment env = context.getEnvironment();
		System.out.println(KEY+": "+env.getProperty(KEY));
    }
    

	@Value("${spring.application.name:testZookeeperApp}")
	private String appName;

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private DiscoveryClient discovery;
	
	@Autowired
	Registration registry;

	@Autowired
	private Environment env;

	@Autowired
	private AppClient appClient;

	@RequestMapping("/")
	public ServiceInstance lb() {
		return this.loadBalancer.choose(this.appName);
	}

	@RequestMapping("/hi")
	public String hi() {
		//System.out.println(loadBalancer.choose(appName)); //打印当前调用的服务端地址
		return "Hello World! from " + this.registry.getServiceId();
	}

	@RequestMapping("/self")
	public String self() {
		return this.appClient.hi();
	}

	@RequestMapping("/myenv")
	public String env(@RequestParam("prop") String prop) {
		return this.env.getProperty(prop, "Not Found");
	}

	@FeignClient("testZkApp")
	interface AppClient {
		@RequestMapping(path = "/hi", method = RequestMethod.GET)
		String hi();
	}

	@Autowired
	RestTemplate rest;

	public String rt() {
		return this.rest.getForObject("http://" + this.appName + "/hi", String.class);
	}

	@Bean
	@LoadBalanced
	RestTemplate loadBalancedRestTemplate() {
		return new RestTemplate();
	}
	
	
	@Autowired
	private ZookeeperServiceRegistry serviceRegistry;
	ZookeeperRegistration registration = ServiceInstanceRegistration.builder()
            .defaultUriSpec()
            .address("yy")
            .port(2181)
            .name("/a/b/c/d/anotherservice")
            .build();

	public void registerThings() {    
	    this.serviceRegistry.register(registration);
	}
	
	@RequestMapping("/register")
	public void register() {
		this.registerThings();
	}
	
	
	@RequestMapping("/serviceUrl")
	public String serviceUrl() {
	    List<ServiceInstance> list = discovery.getInstances(this.registry.getServiceId());//(appName);
	    if (list != null && list.size() > 0 ) {
	        return "";//new Gson().toJson(list);
	    }
	    return null;
	}
	
	@EventListener
	public void handle(EnvironmentChangeEvent event) {
		for (String key : event.getKeys()) {
			System.out.println("EnvironmentChangeEvent: "+key);
		}
	}
}
