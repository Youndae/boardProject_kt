package com.example.boardproject_kt_ver_default.config.auth.security

import com.example.boardproject_kt_ver_default.auth.oAuth.user.CustomOAuth2SuccessHandler
import com.example.boardproject_kt_ver_default.auth.oAuth.user.CustomOAuth2UserService
import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val corsFilter: CorsFilter,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    private val jwtAuthorizationFilter: JwtAuthorizationFilter
) {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @kotlin.jvm.Throws(Exception::class)
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{ csrf -> csrf.disable() }
            .sessionManagement { config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilter(corsFilter)
            .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter::class.java)
            .formLogin { config -> config.disable() }
            .httpBasic{ config -> config.disable() }
            .logout{ config -> config.disable() }
            .oauth2Login{ oauth2 ->
                oauth2
                    .loginPage("/login")
                    .userInfoEndpoint{ userInfoEndPointConfig ->
                        userInfoEndPointConfig.userService(customOAuth2UserService)
                    }
                    .successHandler(customOAuth2SuccessHandler)
            }

        return http.build()
    }
}