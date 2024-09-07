package com.example.boardproject_kt_ver_default.repository.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardReplyInfoDTO
import com.example.boardproject_kt_ver_default.domain.entity.HierarchicalBoard
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.support.PageableExecutionUtils

import com.example.boardproject_kt_ver_default.domain.entity.QHierarchicalBoard.hierarchicalBoard
import com.example.boardproject_kt_ver_default.domain.entity.QMember.member
import com.querydsl.core.types.dsl.BooleanExpression

@Repository
class HierarchicalBoardCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): HierarchicalBoardCustomRepository {

    override fun findListToPageable(pageable: Pageable, pageDTO: PaginationDTO): Page<HierarchicalBoardListDTO> {

        var list: List<HierarchicalBoardListDTO> = jpaQueryFactory.select(
                                                                        Projections.constructor(
                                                                            HierarchicalBoardListDTO::class.java
                                                                            , hierarchicalBoard.boardNo
                                                                            , hierarchicalBoard.boardTitle
                                                                            , hierarchicalBoard.member.nickName
                                                                            , hierarchicalBoard.boardDate
                                                                            , hierarchicalBoard.boardIndent
                                                                        )
                                                                    )
                                                                    .from(hierarchicalBoard)
                                                                    .where(searchTypeEq(pageDTO))
                                                                    .orderBy(hierarchicalBoard.boardGroupNo.desc())
                                                                    .orderBy(hierarchicalBoard.boardUpperNo.asc())
                                                                    .offset(pageable.offset)
                                                                    .limit(pageable.pageSize.toLong())
                                                                    .fetch()

        var count: JPAQuery<Long> = jpaQueryFactory.select(hierarchicalBoard.countDistinct())
                                                    .from(hierarchicalBoard)


        return PageableExecutionUtils.getPage(list, pageable) {
            count.fetchOne() ?: 0L
        }
    }

    private fun searchTypeEq(pageDTO: PaginationDTO): BooleanExpression? {
        if(pageDTO.searchType == null)
            return null
        else if(pageDTO.searchType == "t")
            return hierarchicalBoard.boardTitle.like(pageDTO.keyword)
        else if(pageDTO.searchType == "c")
            return hierarchicalBoard.boardContent.like(pageDTO.keyword)
        else if(pageDTO.searchType == "tc")
            return hierarchicalBoard.boardTitle.like(pageDTO.keyword).or(hierarchicalBoard.boardContent.like(pageDTO.keyword))
        else if(pageDTO.searchType == "u")
            return hierarchicalBoard.member.userId.like(pageDTO.keyword)
        else
            return null;
    }

    override fun findBoardDetailByBoardNo(boardNo: Long): HierarchicalBoardDetailDTO? {

        return jpaQueryFactory.select(
                                    Projections.constructor(
                                        HierarchicalBoardDetailDTO::class.java,
                                        hierarchicalBoard.boardNo,
                                        hierarchicalBoard.boardTitle,
                                        hierarchicalBoard.member.nickName,
                                        hierarchicalBoard.boardContent,
                                        hierarchicalBoard.boardDate
                                    )
                                )
                                    .from(hierarchicalBoard)
                                    .where(hierarchicalBoard.boardNo.eq(boardNo))
                                    .fetchOne()
    }

    override fun getPatchData(boardNo: Long): HierarchicalBoardPatchDTO? {
        return jpaQueryFactory.select(
                                    Projections.constructor(
                                        HierarchicalBoardPatchDTO::class.java,
                                        hierarchicalBoard.boardNo,
                                        hierarchicalBoard.boardTitle,
                                        hierarchicalBoard.boardContent
                                    )
                                )
                                    .from(hierarchicalBoard)
                                    .where(hierarchicalBoard.boardNo.eq(boardNo))
                                    .fetchOne()
    }

    override fun findReplyInfoByBoardNo(boardNo: Long): HierarchicalBoardReplyInfoDTO? {
        return jpaQueryFactory.select(
                                    Projections.constructor(
                                        HierarchicalBoardReplyInfoDTO::class.java,
                                        hierarchicalBoard.boardGroupNo,
                                        hierarchicalBoard.boardIndent,
                                        hierarchicalBoard.boardUpperNo
                                    )
                                )
                                    .from(hierarchicalBoard)
                                    .where(hierarchicalBoard.boardNo.eq(boardNo))
                                    .fetchOne()
    }

    override fun deleteByBoardGroupNo(boardNo: Long) {
        jpaQueryFactory.delete(hierarchicalBoard)
                        .where(hierarchicalBoard.boardGroupNo.eq(boardNo))
    }

    override fun findAllByBoardGroupNo(groupNo: Long): List<HierarchicalBoard> {
        return jpaQueryFactory.select(hierarchicalBoard)
                                .from(hierarchicalBoard)
                                .where(hierarchicalBoard.boardGroupNo.eq(groupNo))
                                .fetch()
    }
}