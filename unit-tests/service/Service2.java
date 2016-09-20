package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service2 {
	public Service2() {
		System.out.printf("construct:%s\n",this.getClass().getName());
	}
	@Autowired
	private Service1 s1;
	public void print(){
		s1.print();
		System.out.println(this.getClass().getName());
	}
}