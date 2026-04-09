package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.member.response.ProfileResponse
import com.example.boardproject_kt.domain.entity.Member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import com.example.boardproject_kt.domain.enums.OAuthProvider
import com.querydsl.core.types.Projections

import com.example.boardproject_kt.domain.entity.QMember.member
import com.example.boardproject_kt.domain.entity.QAuth.auth1

@Repository
class CustomMemberRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomMemberRepository {

    override fun findByUserId(userId: String): Member? {
        return jpaQueryFactory.selectFrom(member)
            .where(member.userId.eq(userId))
            .fetchOne()
    }

    override fun findByLoginUserId(userId: String): Member? {
        return jpaQueryFactory.selectFrom(member)
            .innerJoin(member.auths, auth1).fetchJoin()
            .where(
                member.userId.eq(userId)
                    .and(member.provider.eq(OAuthProvider.LOCAL.key)))
            .fetchOne()
    }

    override fun findOAuthUserByUserId(userId: String): Member? {
        return jpaQueryFactory.selectFrom(member)
            .where(
                member.userId.eq(userId)
                    .and(member.provider.ne(OAuthProvider.LOCAL.key))
            )
            .fetchOne()
    }

    override fun findByNickname(nickname: String): Member? {
        return jpaQueryFactory.selectFrom(member)
            .where(member.nickname.eq(nickname))
            .fetchOne()
    }

    override fun getMemberProfileDataByUserId(userId: String): ProfileResponse? {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ProfileResponse::class.java,
                    member.nickname,
                    member.email,
                    member.profile
                )
            )
            .from(member)
            .where(member.userId.eq(userId))
            .fetchOne()
    }
}