package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long>, CustomBoardRepository {
}