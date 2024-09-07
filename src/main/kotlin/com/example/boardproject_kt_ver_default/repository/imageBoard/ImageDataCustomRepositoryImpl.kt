package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageDataDTO
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import com.example.boardproject_kt_ver_default.domain.entity.QImageData.imageData
import com.querydsl.core.types.Projections

@Repository
class ImageDataCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): ImageDataCustomRepository {

    override fun getImageData(imageNo: Long): List<ImageDataDTO> {

        return jpaQueryFactory.select(
                                    Projections.constructor(
                                        ImageDataDTO::class.java,
                                        imageData.imageName,
                                        imageData.oldName,
                                        imageData.imageStep
                                    )
                                )
                                    .from(imageData)
                                    .where(imageData.imageBoard.imageNo.eq(imageNo))
                                    .fetch()
    }

    override fun findByLastStep(imageNo: Long): Int {
        return jpaQueryFactory.select(imageData.imageStep.max())
                                .from(imageData)
                                .where(imageData.imageBoard.imageNo.eq(imageNo))
                                .fetchOne()!!
    }
}