package com.metasoft.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.springframework.util.StringUtils;

import com.keymobile.common.persistence.metadata.Attribute;

public class AttributeUtil {

	public static List<Attr> GetAttributes(Class<?> clazz) {
		TreeSet<Attr> all = new TreeSet<Attr>();
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Attribute attribute = methods[i].getAnnotation(Attribute.class);
			if (null == attribute) {
				continue;
			}

			String attr = attribute.attr();
			if (StringUtils.isEmpty(attr)) {
				attr = methods[i].getName();
				if (attr.toLowerCase().startsWith("get")) {
					attr = attr.substring(3);
				} else if (attr.toLowerCase().startsWith("is")) {
					attr = attr.substring(2);
				}
				attr = attr.substring(0, 1).toLowerCase() + attr.substring(1);
			}

			all.add(new Attr(attr, attribute.desc(), attribute.prior()));
		}
		return Arrays.asList(all.toArray(new Attr[all.size()]));
	}
	
}
