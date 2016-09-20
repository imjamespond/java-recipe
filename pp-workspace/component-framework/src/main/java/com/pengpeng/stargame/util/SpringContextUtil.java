package com.pengpeng.stargame.util;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderSupport;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 用于取得 spring 的 bean 定义.
 * <pre>需要在 content-hibernate.xml中加上:
 * &lt;bean id=&quot;SpringContextUtil &quot; class=&quot;com.pengpeng.common.utils.SpringContextUtil &quot; scope=&quot;singleton&quot; /&gt;
 * 当对SpringContextUtil 实例时就自动设置applicationContext,以便后来可直接用applicationContext
 * </pre>
 *
 * @author ChenHonghong@gmail.com
 * @since 4.0
 */

public class SpringContextUtil implements ApplicationContextAware {
// ------------------------------ 属性 ------------------------------

    //Spring应用上下文环境
    private static ApplicationContext applicationContext;

    private static Properties properties=new Properties();

// -------------------------- 静态方法 --------------------------

    public static void init(){
        final String configLocation = "classpath*:/spring/context-*.xml";
        System.out.println("Initialize Spring Context from: " + configLocation);
        SpringContextUtil.applicationContext = new ClassPathXmlApplicationContext(configLocation);
        readProperty();
    }

    public static void readProperty(){
        try{
            // get the names of BeanFactoryPostProcessor
            String[] postProcessorNames = applicationContext.getBeanNamesForType(BeanFactoryPostProcessor.class, true, true);

            for (String ppName : postProcessorNames) {
                // get the specified BeanFactoryPostProcessor
                BeanFactoryPostProcessor beanProcessor=
                        applicationContext.getBean(ppName, BeanFactoryPostProcessor.class);
                // check whether the beanFactoryPostProcessor is
                // instance of the PropertyResourceConfigurer
                // if it is yes then do the process otherwise continue
                if(beanProcessor instanceof PropertyResourceConfigurer){
                    PropertyResourceConfigurer propertyResourceConfigurer=
                            (PropertyResourceConfigurer) beanProcessor;

                    // get the method mergeProperties
                    // in class PropertiesLoaderSupport
                    Method mergeProperties=PropertiesLoaderSupport.class.
                            getDeclaredMethod("mergeProperties");
                    // get the props
                    mergeProperties.setAccessible(true);
                    Properties props=(Properties) mergeProperties.
                            invoke(propertyResourceConfigurer);

                    // get the method convertProperties
                    // in class PropertyResourceConfigurer
                    Method convertProperties=PropertyResourceConfigurer.class.
                            getDeclaredMethod("convertProperties", Properties.class);
                    // convert properties
                    convertProperties.setAccessible(true);
                    convertProperties.invoke(propertyResourceConfigurer, props);

                    properties.putAll(props);
                }
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     * @throws BeansException
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
    public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

// ------------------------ 接口实现方法 ------------------------

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }
}
