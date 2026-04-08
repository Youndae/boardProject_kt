package com.example.boardproject_kt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BoardProjectKtApplication

fun main(args: Array<String>) {
    runApplication<BoardProjectKtApplication>(*args)
}
