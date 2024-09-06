package com.example.boardproject_kt_ver_default.auth.oAuth.user

import com.example.boardproject_kt_ver_default.auth.oAuth.converter.OAuth2ResponseEntityConverter
import com.example.boardproject_kt_ver_default.auth.oAuth.domain.*
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.domain.enumuration.OAuthProvider
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository
): DefaultOAuth2UserService() {

    // ExceptionHandler -> OAuth2AuthenticationException
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User: OAuth2User = super.loadUser(userRequest)
        val registrationId: String = userRequest.clientRegistration.registrationId
        val oAuth2Response: OAuth2Response = createProviderObject(registrationId, oAuth2User)
        val userId: String = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId()
        var existsData: Member? = memberRepository.findByOAuthUserId(userId, oAuth2Response.getProvider())

        if(existsData == null){
            val converter = OAuth2ResponseEntityConverter()
            val member: Member = converter.toEntity(oAuth2Response, userId)
            member.addAuth()
            member.username = oAuth2Response.getName()
            existsData = member
        }else {
            existsData.email = oAuth2Response.getEmail()
            existsData.username = oAuth2Response.getName()
        }

        memberRepository.save(existsData)
        val oAuth2DTO = OAuth2DTO(existsData)

        return CustomOAuth2User(oAuth2DTO)
    }

    private fun createProviderObject(registrationId: String, oAuth2User: OAuth2User): OAuth2Response {
        if(registrationId == OAuthProvider.NAVER.getKey())
            return NaverResponse(oAuth2User.attributes)
        else if(registrationId == OAuthProvider.GOOGLE.getKey())
            return GoogleResponse(oAuth2User.attributes)
        else if(registrationId == OAuthProvider.KAKAO.getKey())
            return KakaoResponse(oAuth2User.attributes)
        else
            throw BadCredentialsException("OAuth2 BadCredentials")
    }
}