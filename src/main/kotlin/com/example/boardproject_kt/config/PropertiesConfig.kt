package com.example.boardproject_kt.config

import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.properties.JwtSecretProperties
import com.example.boardproject_kt.properties.RedisProperties
import com.example.boardproject_kt.properties.TokenProperties
import com.example.boardproject_kt.properties.TokenRedisProperties
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
@EnableConfigurationProperties(
    CookieProperties::class,
    JwtSecretProperties::class,
    RedisProperties::class,
    TokenProperties::class,
    TokenRedisProperties::class
)
class PropertiesConfig {

    @Bean(name = ["filePath"])
    fun filePathPropertiesFactoryBean(): PropertiesFactoryBean {
        val filePathPropertiesPath = "dev-filepath.properties"

        return setPropertiesFactoryBean(filePathPropertiesPath)
    }

    private fun setPropertiesFactoryBean(path: String): PropertiesFactoryBean {
        return PropertiesFactoryBean().apply {
            setLocation(ClassPathResource(path))
        }
    }
}