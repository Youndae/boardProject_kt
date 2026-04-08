package com.example.boardproject_kt.config

import com.example.boardproject_kt.auth.filter.JwtAuthorizationFilter
import com.example.boardproject_kt.auth.filter.LoginFilter
import com.example.boardproject_kt.auth.handler.LoginFailureHandler
import com.example.boardproject_kt.auth.handler.LoginSuccessHandler
import com.example.boardproject_kt.auth.oAuth.CustomOAuth2UserService
import com.example.boardproject_kt.auth.oAuth.CustomOAuthSuccessHandler
import com.example.boardproject_kt.auth.service.TokenProvider
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.properties.TokenProperties
import com.example.boardproject_kt.repository.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val corsFilter: CorsFilter,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customOAuthSuccessHandler: CustomOAuthSuccessHandler,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val loginSuccessHandler: LoginSuccessHandler,
    private val loginFailureHandler: LoginFailureHandler,
    private val om: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val tokenProperties: TokenProperties,
    private val memberRepository: MemberRepository,
    private val cookieProperties: CookieProperties
) {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(
        configuration: AuthenticationConfiguration
    ): AuthenticationManager = configuration.authenticationManager

    fun loginFilter(): LoginFilter {
        val filter = LoginFilter(om).apply {
            setAuthenticationManager(authenticationConfiguration.authenticationManager)
            setFilterProcessesUrl("/api/member/login")
            setAuthenticationSuccessHandler(loginSuccessHandler)
            setAuthenticationFailureHandler(loginFailureHandler)
        }

        return filter
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val jwtAuthFilter = JwtAuthorizationFilter(
            memberRepository,
            tokenProvider,
            tokenProperties,
            cookieProperties
        )

        http.csrf { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilter(corsFilter)
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }

        http.exceptionHandling { handling ->
            handling.authenticationEntryPoint { _, response, _ ->
                response.sendError(HttpServletResponse.SC_FORBIDDEN, ErrorCode.FORBIDDEN.message)
            }
            handling.accessDeniedHandler { _, response, _ ->
                response.sendError(HttpServletResponse.SC_FORBIDDEN, ErrorCode.FORBIDDEN.message)
            }
        }

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/", "/resources/**", "/api/member/login").permitAll()
                auth.anyRequest().permitAll()
            }

        http.oauth2Login { oauth2 ->
            oauth2.userInfoEndpoint { userInfo ->
                userInfo.userService(customOAuth2UserService)
            }
            oauth2.successHandler(customOAuthSuccessHandler)
        }

        return http.build()
    }

}