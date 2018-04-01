package com.dave.server

import com.dave.server.domain.image.BoardType
import com.dave.server.domain.image.Images
import com.dave.server.domain.image.Images.url
import com.dave.server.rest.ImageRestController
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.routing.route
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.DateFormat

fun initDB() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
}

fun Application.main() {
    initDB()
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Routing) {
        route("/api") {
            route("/images") {
                val imageRestController = ImageRestController(this@route)
            }
        }
    }

    transaction {
        logger.addLogger(StdOutSqlLogger)
        create(Images)

        val testDb = Images.insert {
            it[url] = "https://www.naver.com"
            it[boardType] = BoardType.CAUGHT
            it[boardId] = 0L
        } get Images.id

        val images = Images.selectAll()
        images.forEach {
            println("result --> ${it[url]}")
        }
        println("Images : ${Images.selectAll()}")

    }
}

/*

fun clientSample() = runBlocking {
    val client = HttpClient(Apache) {
        install(JsonFeature)
    }

    val model = client.get<Model>(port = 8080, path = "/v1")
    println("Fetch items for ${model.name}:")

    for ((key, _) in model.items) {
        val item = client.get<Item>(port = 8080, path = "/v1/item/$key")
        println("Received: $item")
    }
}


install(DefaultHeaders)
install(Compression)
install(CallLogging)
install(ContentNegotiation) {
    gson {
        setDateFormat(DateFormat.LONG)
        setPrettyPrinting()
    }
}

val model = Model("root", listOf(Item("A", "Apache"), Item("B", "Bing")))
routing {
    get("/v1") {
        call.respond(model)
    }
    get("/v1/item/{key}") {
        val item = model.items.firstOrNull { it.key == call.parameters["key"] }
        if (item == null)
            call.respond(HttpStatusCode.NotFound)
        else
            call.respond(item)
    }
}
*/
