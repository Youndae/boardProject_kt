package com.example.boardproject_kt.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {  }

@Aspect
@Component
class TimeCheckAOP {


    @Around("execution(* com.example..*(*))")
    fun execute(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()

        try {
            return joinPoint.proceed()
        } finally {
            val finish = System.currentTimeMillis()
            val timeMs = finish - start
            val signature = joinPoint.signature.toShortString()

            if(timeMs > 1000)
                log.warn { "$signature --- time = ${timeMs}ms" }
            else
                log.info { "$signature --- time = ${timeMs}ms" }
        }
    }
}