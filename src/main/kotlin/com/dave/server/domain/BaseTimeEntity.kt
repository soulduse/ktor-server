package com.dave.server.domain

import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

abstract class BaseTimeEntity : Table(){
    val createdDate = datetime("created_date").default(DateTime.now())
    val modifiedDate = datetime("modified_date").default(DateTime.now())
}
