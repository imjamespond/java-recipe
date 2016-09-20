package com.james.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"/applicationContext.xml","/dataSource.xml"});
        
		String hql =  "from Goods a where a.name=?";//a.date between :dateBegin and :dateEnd";
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, "spring touch");
		List<Goods> list = query.list();
//		List<Goods> list = Goods.getByName("spring touch");
		for(Goods g:list){
			System.out.println(g.getName());
		}
	}

}