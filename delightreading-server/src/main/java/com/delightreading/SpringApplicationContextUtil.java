package com.delightreading;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {

        applicationContext = appContext;
    }

    public static <T> T lookUpBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
