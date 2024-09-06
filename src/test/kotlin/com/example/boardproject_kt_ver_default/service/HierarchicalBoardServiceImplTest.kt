package com.example.boardproject_kt_ver_default.service

import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtProviderProperties
import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtTokenProvider
import com.example.boardproject_kt_ver_default.config.redis.RedisProperties
import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardRequestDTO
import com.example.boardproject_kt_ver_default.domain.entity.HierarchicalBoard
import com.example.boardproject_kt_ver_default.repository.hierarchicalBoard.HierarchicalBoardRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class HierarchicalBoardServiceImplTest {

    @Autowired
    lateinit var properties: RedisProperties

    @Test
    internal fun redisValueTest() {
        println(properties.host)
        println(properties.port)
    }
}