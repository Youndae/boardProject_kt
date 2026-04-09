package com.example.boardproject_kt.auth.oAuth

import com.example.boardproject_kt.auth.oAuth.converter.OAuth2ResponseEntityConverter
import com.example.boardproject_kt.auth.oAuth.domain.OAuth2Member
import com.example.boardproject_kt.auth.oAuth.response.GoogleResponse
import com.example.boardproject_kt.auth.oAuth.response.KakaoResponse
import com.example.boardproject_kt.auth.oAuth.response.NaverResponse
import com.example.boardproject_kt.auth.oAuth.response.OAuth2Response
import com.example.boardproject_kt.domain.entity.Member
import com.example.boardproject_kt.domain.enums.OAuthProvider
import com.example.boardproject_kt.repository.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {  }

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository
) : DefaultOAuth2UserService() {

    @Transactional(rollbackFor = [Exception::class])
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User? {
        val oAuth2User: OAuth2User = super.loadUser(userRequest)
        val registrationId: String = userRequest!!.clientRegistration.registrationId
        val oAuth2Response: OAuth2Response = createProviderObject(registrationId, oAuth2User)
        val userId = "${oAuth2Response.provider}_${oAuth2Response.providerId}"
        var existsData: Member? = memberRepository.findByUserId(userId)

        if(existsData == null) {
            val member: Member = OAuth2ResponseEntityConverter.toEntity(oAuth2Response, userId)
            member.addAuth()
            existsData = member
        }else {
            existsData.updateEmail(oAuth2Response.email)
            existsData.updateUsername(oAuth2Response.name)
        }

        memberRepository.save<Member>(existsData)
        val oAuth2Member = OAuth2Member(existsData)

        return CustomOAuth2User(oAuth2Member)
    }

    private fun createProviderObject(
        registrationId: String,
        oAuth2User: OAuth2User
    ): OAuth2Response {
        return when(registrationId) {
            OAuthProvider.NAVER.key -> NaverResponse(oAuth2User.attributes)
            OAuthProvider.GOOGLE.key -> GoogleResponse(oAuth2User.attributes)
            OAuthProvider.KAKAO.key -> KakaoResponse(oAuth2User.attributes)
            else -> throw BadCredentialsException("OAuth2 BadCredentials")
        }
    }
}