package com.example.boardproject_kt_ver_default.config.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource();

        val config: CorsConfiguration = CorsConfiguration();
        config.addAllowedOriginPattern("*")
        config.allowCredentials = true
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        return CorsFilter(source)
    }
}