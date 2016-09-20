package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service1 {
	public Service1() {
		System.out.printf("construct:%s\n",this.getClass().getName());
	}
	@Autowired
	private Service3 s3;
	public void print(){
		System.out.println(this.getClass().getName());
	}
}
