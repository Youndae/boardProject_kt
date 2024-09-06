package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ImageBoardCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): ImageBoardCustomRepository {
}