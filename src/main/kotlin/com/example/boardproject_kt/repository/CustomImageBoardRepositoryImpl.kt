package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardDetail
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardPatchDetail
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardListResponse
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.example.boardproject_kt.domain.entity.QImageBoard.imageBoard
import com.example.boardproject_kt.domain.entity.QImageData.imageData
import com.example.boardproject_kt.domain.entity.QMember.member
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.support.PageableExecutionUtils

@Repository
class CustomImageBoardRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomImageBoardRepository {

    override fun findAllListByPageable(
        pageCondition: PageCondition,
        pageable: Pageable
    ): Page<ImageBoardListResponse> {
        val list = jpaQueryFactory
            .select(
                Projections.constructor(
                    ImageBoardListResponse::class.java,
                    imageBoard.id,
                    imageBoard.title,
                    imageData.imageName.min()
                )
            )
            .from(imageBoard)
            .innerJoin(imageData)
            .on(imageBoard.id.eq(imageData.imageBoard.id))
            .where(searchTypeEq(pageCondition.searchType, pageCondition.keyword))
            .groupBy(imageBoard.id)
            .orderBy(imageBoard.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: JPAQuery<Long> = jpaQueryFactory.select(imageBoard.countDistinct())
            .from(imageBoard)
            .where(searchTypeEq(pageCondition.searchType, pageCondition.keyword))

        return PageableExecutionUtils.getPage(list, pageable){ count.fetchOne() ?: 0L}
    }

    private fun searchTypeEq(searchType: String?, keyword: String?): BooleanExpression? {
        if(searchType == null) return null

        return when(searchType) {
            "t" -> imageBoard.title.like(keyword)
            "c" -> imageBoard.content.like(keyword)
            "tc" -> imageBoard.title.like(keyword).or(imageBoard.content.like(keyword))
            "u" -> imageBoard.member.nickname.like(keyword)
            else -> null
        }
    }

    override fun findDetailById(id: Long): ImageBoardDetail? {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ImageBoardDetail::class.java,
                    imageBoard.title,
                    imageBoard.content,
                    imageBoard.member.nickname.`as`("writer"),
                    imageBoard.member.userId.`as`("writerId"),
                    imageBoard.createdAt
                )
            )
            .from(imageBoard)
            .innerJoin(imageBoard.member, member)
            .fetchOne()
    }

    override fun findPatchDetailById(id: Long): ImageBoardPatchDetail? {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ImageBoardPatchDetail::class.java,
                    imageBoard.member.userId.`as`("writer"),
                    imageBoard.title,
                    imageBoard.content
                )
            )
            .from(imageBoard)
            .innerJoin(imageBoard.member, member)
            .fetchOne()
    }
}