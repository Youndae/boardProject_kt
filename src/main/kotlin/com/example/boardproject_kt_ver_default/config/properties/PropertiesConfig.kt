package com.example.boardproject_kt_ver_default.config.properties

import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class PropertiesConfig {

    @Bean(name = ["jwt"])
    @kotlin.jvm.Throws(IOException::class)
    fun propertiesFactoryBean(): PropertiesFactoryBean {
        val propertiesFactoryBean: PropertiesFactoryBean = PropertiesFactoryBean()
        val classPathResource: ClassPathResource = ClassPathResource("jwt.properties")

        propertiesFactoryBean.setLocation(classPathResource)

        return propertiesFactoryBean
    }
}