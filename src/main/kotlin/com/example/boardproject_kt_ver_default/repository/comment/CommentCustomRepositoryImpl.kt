package com.example.boardproject_kt_ver_default.repository.comment

import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.example.boardproject_kt_ver_default.domain.entity.QComment.comment
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.support.PageableExecutionUtils

@Repository
class CommentCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): CommentCustomRepository {

    override fun findAll(boardNo: Long?, imageNo: Long?, pageable: Pageable): Page<CommentDTO> {
        val list: List<CommentDTO> = jpaQueryFactory.select(
                                        Projections.constructor(
                                            CommentDTO::class.java,
                                            comment.commentNo,
                                            comment.commentDate,
                                            CaseBuilder()
                                                .`when`(comment.commentStatus.gt(0))
                                                .then("삭제된 댓글입니다.")
                                                .otherwise(comment.commentContent)
                                                .`as`("commentContent"),
                                            comment.commentGroupNo,
                                            comment.commentIndent,
                                            comment.commentUpperNo
                                        )
                                    )
                                        .from(comment)
                                        .where(commentBoardEq(boardNo, imageNo))
                                        .orderBy(comment.commentGroupNo.desc())
                                        .orderBy(comment.commentUpperNo.asc())
                                        .offset(pageable.offset)
                                        .limit(pageable.pageSize.toLong())
                                        .fetch()

        val count: JPAQuery<Long> = jpaQueryFactory.select(comment.count())
                                        .from(comment)
                                        .where(commentBoardEq(boardNo, imageNo))

        return PageableExecutionUtils.getPage(list, pageable) {
            count.fetchOne() ?: 0L
        }
    }

    private fun commentBoardEq(boardNo: Long?, imageNo: Long?): BooleanExpression {
        return if(boardNo == null)
                    comment.imageBoard.imageNo.eq(imageNo)
                else
                    comment.hierarchicalBoard.boardNo.eq(boardNo)
    }
}