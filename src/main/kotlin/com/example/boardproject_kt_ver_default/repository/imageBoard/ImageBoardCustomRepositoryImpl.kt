package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardListDTO
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import com.example.boardproject_kt_ver_default.domain.entity.QImageBoard.imageBoard
import com.example.boardproject_kt_ver_default.domain.entity.QImageData.imageData
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.support.PageableExecutionUtils

@Repository
class ImageBoardCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): ImageBoardCustomRepository {

    override fun findListToPageable(pageable: Pageable, pageDTO: PaginationDTO): Page<ImageBoardListDTO> {

        val list: List<ImageBoardListDTO> = jpaQueryFactory.select(
                                                Projections.constructor(
                                                    ImageBoardListDTO::class.java,
                                                    imageBoard.imageNo,
                                                    imageBoard.imageTitle,
                                                    imageBoard.member.nickName,
                                                    imageBoard.imageDate,
                                                    imageData.imageName
                                                )
                                            )
                                                .from(imageBoard)
                                                .innerJoin(imageBoard.imageDataSet, imageData)
                                                .where(searchTypeEq(pageDTO))
                                                .groupBy(imageBoard.imageNo)
                                                .orderBy(imageBoard.imageNo.desc())
                                                .offset(pageable.offset)
                                                .limit(pageable.pageSize.toLong())
                                                .fetch()

        val count: JPAQuery<Long> = jpaQueryFactory.select(imageBoard.countDistinct())
                                                    .from(imageBoard)
                                                    .where(searchTypeEq(pageDTO))

        return PageableExecutionUtils.getPage(list, pageable) {
            count.fetchOne() ?: 0L
        }
    }

    private fun searchTypeEq(pageDTO: PaginationDTO): BooleanExpression? {
        if(pageDTO.searchType == null)
            return null
        else if(pageDTO.searchType == "t")
            return imageBoard.imageTitle.like(pageDTO.keyword)
        else if(pageDTO.searchType == "c")
            return imageBoard.imageContent.like(pageDTO.keyword)
        else if(pageDTO.searchType == "tc")
            return imageBoard.imageTitle.like(pageDTO.keyword).or(imageBoard.imageContent.like(pageDTO.keyword))
        else if(pageDTO.searchType == "u")
            return imageBoard.member.userId.like(pageDTO.keyword)
        else
            return null;
    }
}