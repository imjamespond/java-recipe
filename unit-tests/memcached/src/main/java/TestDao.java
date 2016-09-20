package com.james;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.james.commons.data.DaoAnnotation;
import com.james.memcached.XmemcachedDao;

//@Service
@DaoAnnotation(prefix = "player.stall.")
public class TestDao extends XmemcachedDao<TestData>{

	@Override
	public Class<TestData> getClassType() {
		// TODO Auto-generated method stub
		return TestData.class;
	}
	
    public TestData getData(String pid) {
    	
    	//get data from cache
    	//null
    	//get data from mysql
    	//null
    	//create or throw errors
    	
    	TestData data=this.getBean(pid);
    	
        if(null == data){
        	data = new TestData();
        	data.setPid(pid);
        	data.setLoginDate(new Date());
        }
        
        return data;
    }

}
