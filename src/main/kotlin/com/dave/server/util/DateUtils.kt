package com.dave.fish.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    /**
     * Java 8 버전
     */
    fun toStringDateTime(localDateTime: LocalDateTime?): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("")
    }

    /**
     * Java 7 버전
     */
    fun toStringDateTimeByJava7(localDateTime: LocalDateTime?): String{
        if(localDateTime == null){
            return ""
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return formatter.format(localDateTime)
    }

    /**
     * Kotlin 버전
     */
    fun toStringDateTimeByKotlin(localDateTime: LocalDateTime?): String{
        return localDateTime?.let {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(it)
        } ?: ""
    }
}