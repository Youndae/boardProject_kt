package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.board.business.BoardPatchDetail
import com.example.boardproject_kt.domain.dto.board.response.BoardDetailResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardListResponse
import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.example.boardproject_kt.domain.entity.QBoard.board;
import com.example.boardproject_kt.domain.entity.QMember.member;
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.transaction.annotation.Transactional

@Repository
class CustomBoardRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomBoardRepository {

    override fun findAllListByPageable(
        condition: PageCondition,
        pageable: Pageable
    ): Page<BoardListResponse> {
        val list = jpaQueryFactory.select(
            Projections.constructor(
                BoardListResponse::class.java,
                board.id,
                board.title,
                board.member.nickname.`as`("writer"),
                board.createdAt,
                board.indent
            )
        )
            .from(board)
            .innerJoin(board.member, member)
            .where(
                searchTypeEq(condition.searchType, condition.keyword)
            )
            .orderBy(board.groupNo.desc())
            .orderBy(board.upperNo.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch();

        val count: JPAQuery<Long> = jpaQueryFactory.select(board.countDistinct())
            .from(board)
            .where(searchTypeEq(condition.searchType, condition.keyword))

        return PageableExecutionUtils.getPage(list, pageable) { count.fetchOne() ?: 0L }
    }

    private fun searchTypeEq(searchType: String?, keyword: String?): BooleanExpression? {
        if (searchType == null) return null

        return when(searchType) {
            "t" -> board.title.like(keyword)
            "c" -> board.content.like(keyword)
            "tc" -> board.title.like(keyword).or(board.content.like(keyword))
            "u" -> board.member.nickname.like(keyword)
            else -> null
        }
    }

    override fun findDetailResponseById(id: Long): BoardDetailResponse? {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    BoardDetailResponse::class.java,
                    board.title,
                    board.member.nickname.`as`("writer"),
                    board.member.userId.`as`("writerId"),
                    board.content,
                    board.createdAt
                )
            )
            .from(board)
            .innerJoin(board.member, member)
            .where(board.id.eq(id))
            .fetchOne()
    }

    override fun findPatchDetailById(id: Long): BoardPatchDetail? {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    BoardPatchDetail::class.java,
                    board.member.userId,
                    board.title,
                    board.content
                )
            )
            .from(board)
            .innerJoin(board.member, member)
            .fetchOne()
    }

    @Transactional
    override fun deleteByGroupNo(id: Long) {
        jpaQueryFactory.delete(board)
            .where(board.groupNo.eq(id))
            .execute()
    }

    override fun deleteByPath(groupNo: Long, selfUpperNo: String, childUpperNo: String) {
        jpaQueryFactory.delete(board)
            .where(
                board.groupNo.eq(groupNo)
                    .and(
                        board.upperNo.eq(selfUpperNo)
                            .or(
                                board.upperNo.like(childUpperNo)
                            )
                    )
            )
            .execute()
    }
}