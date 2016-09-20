package test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.BeanUtils;

public class BeanTest {
	public static void main(String[] args) {
	Date date=new Date();

	try {
	Map<String,String> map=BeanUtils.describe(date);
	for (Entry<String,String> entry:map.entrySet()) {
	System.out.println(entry.getKey()+"->"+entry.getValue());
	}
	} catch (IllegalAccessException e) {
	e.printStackTrace();
	} catch (InvocationTargetException e) {
	e.printStackTrace();
	} catch (NoSuchMethodException e) {
	e.printStackTrace();
	}
	}
}
