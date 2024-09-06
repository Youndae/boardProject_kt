package com.example.boardproject_kt_ver_default.repository.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.out.HierarchicalBoardListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.example.boardproject_kt_ver_default.domain.entity.QHierarchicalBoard.hierarchicalBoard
import com.example.boardproject_kt_ver_default.domain.entity.QMember.member
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.support.PageableExecutionUtils


@Repository
class HierarchicalBoardCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): HierarchicalBoardCustomRepository {

    override fun findListToPageable(pageable: Pageable): Page<HierarchicalBoardListDTO> {

        var list: List<HierarchicalBoardListDTO> = jpaQueryFactory.select(
                                                                        Projections.fields(
                                                                            HierarchicalBoardListDTO::class.java
                                                                            , hierarchicalBoard.boardNo
                                                                            , hierarchicalBoard.boardTitle
                                                                            , member.nickName
                                                                            , hierarchicalBoard.boardDate
                                                                            , hierarchicalBoard.boardIndent
                                                                        )
                                                                    )
                                                                    .from(hierarchicalBoard)
                                                                    .innerJoin(hierarchicalBoard.member, member)
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
}