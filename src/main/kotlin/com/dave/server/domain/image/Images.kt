package com.dave.server.domain.image

import com.dave.server.domain.BaseTimeEntity

object Images : BaseTimeEntity() {
    val id = long("id").autoIncrement().primaryKey()
    val url = varchar("url", length = 500)
    val boardType = enumeration("board_type", BoardType::class.java)
    val boardId = long("board_id")
}

enum class BoardType {
    CAUGHT, STAMP, TOGETHER, YOUTUBE;
}
