package com.delightreading

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service

@Service
class SpringApplicationContextUtil : ApplicationContextAware {

    companion object {
        var applicationContext: ApplicationContext? = null

        fun <T> lookUpBean(tClass: Class<T>): T {
            return applicationContext!!.getBean(tClass)
        }
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(appContext: ApplicationContext) {
        applicationContext = appContext
    }

}