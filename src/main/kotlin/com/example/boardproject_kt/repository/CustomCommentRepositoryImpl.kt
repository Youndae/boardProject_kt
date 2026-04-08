package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.comment.response.BoardCommentResponse
import com.example.boardproject_kt.domain.entity.Comment
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

import com.example.boardproject_kt.domain.entity.QComment.comment
import com.example.boardproject_kt.domain.entity.QMember.member

@Repository
class CustomCommentRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomCommentRepository {

    override fun findAllCommentByBoardId(
        id: Long,
        pageable: Pageable
    ): Page<BoardCommentResponse> {
        val condition: BooleanExpression = comment.board.id.eq(id)

        return findAllCommentList(pageable, condition)
    }

    override fun findAllCommentByImageBoardId(
        id: Long,
        pageable: Pageable
    ): Page<BoardCommentResponse> {
        val condition: BooleanExpression = comment.imageBoard.id.eq(id)

        return findAllCommentList(pageable, condition)
    }

    private fun findAllCommentList(
        pageable: Pageable,
        condition: BooleanExpression
    ): Page<BoardCommentResponse> {
        val list = jpaQueryFactory
            .select(
                Projections.constructor(
                    BoardCommentResponse::class.java,
                    comment.id,
                    comment.member.nickname,
                    comment.member.userId,
                    comment.createdAt,
                    comment.content,
                    comment.indent,
                    comment.deletedAt
                )
            )
            .from(comment)
            .innerJoin(comment.member, member)
            .where(condition)
            .orderBy(comment.groupNo.desc())
            .orderBy(comment.upperNo.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: JPAQuery<Long> = jpaQueryFactory.select(comment.countDistinct())
                                            .from(comment)
                                            .where(condition)

        return PageableExecutionUtils.getPage(list, pageable) { count.fetchOne() ?: 0}
    }

    override fun findNotDeleteCommentById(id: Long): Comment? {
        return jpaQueryFactory
            .selectFrom(comment)
            .where(comment.id.eq(id).and(comment.deletedAt.isNull))
            .fetchOne()
    }
}