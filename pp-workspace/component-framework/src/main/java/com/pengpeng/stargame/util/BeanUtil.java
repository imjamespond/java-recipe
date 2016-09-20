package com.pengpeng.stargame.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * BeanUtil
 */
@Deprecated
public abstract class BeanUtil {
// ------------------------------ 属性 ------------------------------

    private static Logger logger = Logger.getLogger(BeanUtil.class);

// -------------------------- 静态方法 --------------------------

    /**
     * Get a property of the object
     *
     * @param object   An object
     * @param propName A property name of the object
     * @return If the property is exists, its value will be returned,or
     *         null instead.
     */
    public static final Object getProperty(Object object, String propName) {
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        //try to visit the get method
        String methodName = "get" +
                propName.substring(0, 1).toUpperCase() +
                propName.substring(1);
        try {
            Method getMethod = clazz.getMethod(methodName, new Class[0]);
            return getMethod.invoke(object, new Object[0]);
        } catch (Exception e) {
            //No such get method, the property is not visiable
            return null;
        }
    }

    public static void copyProperty(Object dest, Object src) {
        copyProperty(dest, src, false);
    }

    public static void copyProperty(Object dest, Object src, boolean keepOnNull) {
        try {
            copyProperties(dest, src, keepOnNull);
        } catch (Exception e) {
            logger.error("属性复制Exception" + src + "=====================>>" + dest, e);
        }
    }

    /**
     * 在对象之间拷贝值.
     *
     * @param dest       目标对象
     * @param src        　源对象
     * @param keepOnNull 为true时，如果source的值为空，则维持dest的旧值
     * @throws Exception
     */
    public static void copyProperties(Object dest, Object src, boolean keepOnNull) throws Exception {
        if (src == null || dest == null) {
            return;
        }
        BeanInfo info = Introspector.getBeanInfo(src.getClass());
        PropertyDescriptor[] props = info.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            PropertyDescriptor srcProp = props[i];
            PropertyDescriptor destProp = findProperty(dest, srcProp.getName());
            if (destProp != null && destProp.getWriteMethod() != null &&
                    srcProp.getReadMethod() != null) {
                try {
                    Object srcVal = srcProp.getReadMethod().invoke(src, new Object[0]);
                    Object destVal = destProp.getReadMethod().invoke(dest, new Object[0]);
                    if (keepOnNull) {
                        //If keepOnNull is open ,keep the old value if it exist
                        if (srcVal instanceof String && StringUtils.isEmpty(String.valueOf(srcVal)) && dest != null) {
                            srcVal = destVal;
                        } else if (dest != null && srcVal == null) {
                            srcVal = destVal;
                        }
                    }
                    //set the property only when the input is the same type as the dest type
                    destProp.getWriteMethod().invoke(dest, new Object[]{srcVal});
                } catch (Exception ex) {
                    logger.error("属性复制:" + dest, ex);
                }
            }
        }
    }

    /**
     * 查找对象属性.
     *
     * @param object
     * @param name
     * @return
     * @throws java.beans.IntrospectionException
     *
     */
    private static PropertyDescriptor findProperty(Object object, String name) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    /**
     * 在对象之间拷贝值,并且不保存旧值
     *
     * @param destination 目标对象
     * @param source      　源对象
     * @throws Exception ex
     */
    public static void copyProperties(Object destination, Object source) throws Exception {
        copyProperties(destination, source, false);
    }

    /**
     * 将 bean 中值不为 null 的属性值全部打印出来.
     * <p>例子：</p>
     * <pre>
     * MemberInfo memberInfo = new MemberInfo();
     * memberInfo.setId(650000);
     * memberInfo.setMemberName(&quot;TyroneChan&quot;);
     * String dump = dumpNotNull(memberInfo);
     * </pre>
     * <p>dump 的值为：<br/>
     * com.com.pengpeng.model.domain.MemberInfo{id = 650000, memberName = "TyroneChan", }</p>
     *
     * @param bean Object 对象
     * @return
     */
    public static String dumpNotNull(Object bean) {
        return dump(bean, false);
    }

    /**
     * 调试, 打印出给定 Bean 的所有属性的取值.
     *
     * @param bean Bean 对象
     * @return
     */
    private static String dump(Object bean, boolean includeNullValue) {
        PropertyDescriptor[] descriptors = getAvailablePropertyDescriptors(bean);
        final String beanClassName = bean.getClass().getName();
        final StringBuffer values = new StringBuffer();

        for (int i = 0; descriptors != null && i < descriptors.length; i++) {
            Method readMethod = descriptors[i].getReadMethod();
            try {
                Object propValue = readMethod.invoke(bean, null);
                final String propName = descriptors[i].getName();
                final String propType = descriptors[i].getPropertyType().getName();
                if (null == propValue && !includeNullValue) {
                    continue;
                }
                values.append(propName).append(" = ");
                if ("java.lang.String".equals(propType)) {
                    values.append("\"");
                    values.append(propValue);
                    values.append("\"");
                } else {
                    values.append(propValue);
                }
                values.append(", ");
            } catch (Exception e) {
                logger.error("error occurs ", e);
            }
        }

        if (values.length() == 0) {
            values.append(" ");
        } else {
            //删除最后的逗号
            values.deleteCharAt(values.length() - 2);
        }

        final StringBuffer sb = new StringBuffer();
        sb.append(beanClassName);
        sb.append("{");
        sb.append(values);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 从 bean 中读取有效的属性描述符.
     * <p/>
     * NOTE: 名称为 class 的 PropertyDescriptor 被排除在外.
     *
     * @param bean Object - 需要读取的 Bean
     * @return PropertyDescriptor[] - 属性列表
     */
    private static PropertyDescriptor[] getAvailablePropertyDescriptors(Object bean) {
        try {
            // 从 Bean 中解析属性信息并查找相关的 write 方法
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            if (info != null) {
                PropertyDescriptor pd[] = info.getPropertyDescriptors();
                Vector<PropertyDescriptor> columns = new Vector<PropertyDescriptor>();

                for (PropertyDescriptor aPd : pd) {
                    String fieldName = aPd.getName();

                    if (fieldName != null && !"class".equals(fieldName)) {
                        columns.add(aPd);
                    }
                }

                PropertyDescriptor[] arrays = new PropertyDescriptor[columns.size()];

                for (int j = 0; j < columns.size(); j++) {
                    arrays[j] = columns.get(j);
                }

                return arrays;
            }
        } catch (Exception ex) {
            logger.error("error occurs ", ex);
            return null;
        }
        return null;
    }
}
