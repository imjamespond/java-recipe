package com.james.hibernate;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author cwd
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml","/dataSource.xml"})  
public class GoodsServiceTest {
	
	@Test
	public	void testSave(){
		
/**/		Goods goods = new Goods();
		goods.setName("spring touch");
		goods.setType("book");
		goods.setPrice(40.5);
		goods.save();
		
		SessionFactory sf = DaoFactory.getGenericDao();
		
//		List<Goods> list = Goods.getByName("spring touch");
//		for(Goods g:list){
//			//g.delete();
//			System.out.println(g.getName());
//		}
		
		Goods goods1 = Goods.getById("8a80828f4350a6ff014350a701120000");
		goods1.setName("hahahahhahahah");
		goods1.update();
		goods1.save();
		System.out.println(goods1.getName());
	}
}