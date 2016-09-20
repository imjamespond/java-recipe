package service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service3 {
	public Service3() {
		System.out.printf("construct:%s\n",this.getClass().getName());
	}
	@Autowired
	private Service1 s1;
	@Autowired
	private Service2 s2;	
	
	@PostConstruct
	public void init(){
		s1.print();
		s2.print();
	}
	
	public void print(){
		System.out.println(this.getClass().getName());
	}
	
	
}