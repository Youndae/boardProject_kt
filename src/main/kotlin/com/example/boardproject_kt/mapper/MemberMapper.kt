package com.example.boardproject_kt.mapper

import com.example.boardproject_kt.domain.dto.member.request.JoinRequest
import com.example.boardproject_kt.domain.entity.Member
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Mapper(componentModel = "spring")
abstract class MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "auths", ignore = true)
    @Mapping(target = "provider", constant = "local")
    @Mapping(target = "username", source = "userName")
    abstract fun toEntity(dto: JoinRequest): Member

    fun toFullEntity(dto: JoinRequest, passwordEncoder: BCryptPasswordEncoder): Member {
        val member = toEntity(dto)
        return member.apply {
            updatePassword(dto.password, passwordEncoder)
            addAuth()
        }
    }
}