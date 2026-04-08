package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageDataResponse
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import com.example.boardproject_kt.domain.entity.QImageBoard.imageBoard
import com.example.boardproject_kt.domain.entity.QImageData.imageData
import com.querydsl.core.types.Projections
import jakarta.transaction.Transactional
import kotlin.math.max

@Repository
class CustomImageDataRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomImageDataRepository {

    override fun getImageDataNameList(id: Long): List<String> {
        return jpaQueryFactory.select(
            imageData.imageName
        )
            .from(imageData)
            .where(imageData.imageBoard.id.eq(id))
            .orderBy(imageData.imageStep.asc())
            .fetch()
    }

    override fun getImageDataList(id: Long): List<ImageDataResponse> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ImageDataResponse::class.java,
                    imageData.imageName,
                    imageData.originName,
                    imageData.imageStep
                )
            )
            .from(imageData)
            .where(imageData.imageBoard.id.eq(id))
            .orderBy(imageData.imageStep.asc())
            .fetch()
    }

    override fun countImageStep(id: Long): Int {
        return jpaQueryFactory.select(imageData.imageStep.max())
            .from(imageData)
            .where(imageData.imageBoard.id.eq(id))
            .fetchOne() ?: 1
    }

    @Transactional
    override fun deleteImageDataByImageBoardId(id: Long) {
        jpaQueryFactory.delete(imageData)
            .where(imageData.imageBoard.id.eq(id))
            .execute()
    }
}