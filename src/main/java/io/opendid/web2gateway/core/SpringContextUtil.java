package io.opendid.web2gateway.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * class description SpringContextUtil
 *
 * @author Dong-Jianguo
 * @version 1.0.0
 * @Date: 2021/9/16
 * @history date, modifier,and description
 **/
@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  public static DefaultListableBeanFactory getBeanFactory() {
    return (DefaultListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
  }

  /**
   * @return ApplicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  /**
   * Get the object
   *
   * @param name
   * @return Object An instance of a bean registered with the given name
   * @throws BeansException
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) throws BeansException {
    return (T) applicationContext.getBean(name);
  }

  /**
   * Get an object of type requiredType
   *
   * @param clz
   * @return
   * @throws BeansException
   */
  public static <T> T getBean(Class<T> clz) throws BeansException {
    return (T) applicationContext.getBean(clz);
  }

  /**
   * @param name beanName
   * @param <T>
   * @return
   * @throws BeansException
   */
  public static <T> T getBean(String name, Class<T> castClz) throws BeansException {
    if (applicationContext.containsBean(name)) {
      return castClz.cast(applicationContext.getBean(name));
    } else {
      return null;
    }
  }

  /**
   * Returns true if the BeanFactory contains a bean definition matching the given name
   *
   * @param name
   * @return boolean
   */
  public static boolean containsBean(String name) {
    return applicationContext.containsBean(name);
  }

  /**
   * Determine whether the bean definition registered with the given name is a singleton or a
   * prototype. If the bean definition corresponding to the given name is not found, an exception
   * (NoSuchBeanDefinitionException) will be thrown
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
   * @return Class the type of registered object
   * @throws NoSuchBeanDefinitionException
   */
  public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
    return applicationContext.getType(name);
  }

  /**
   * If the given bean name has aliases in the bean definition, these aliases are returned
   *
   * @param name
   * @return
   * @throws NoSuchBeanDefinitionException
   */
  public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
    return applicationContext.getAliases(name);
  }

  public static List<Map<String, Object>> getAllBean() {

    List<Map<String, Object>> list = new ArrayList<>();

    String[] beans = getApplicationContext().getBeanDefinitionNames();

    for (String beanName : beans) {
      Class<?> beanType = getApplicationContext().getType(beanName);

      Map<String, Object> map = new HashMap<>();

      map.put("BeanName", beanName);
      map.put("beanType", beanType);
      map.put("package", beanType.getPackage());
      list.add(map);
    }

    return list;
  }
}
