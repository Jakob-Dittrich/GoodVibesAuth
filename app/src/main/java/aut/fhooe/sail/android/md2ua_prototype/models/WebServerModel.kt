package aut.fhooe.sail.android.md2ua_prototype.models

import aut.fhooe.sail.android.md2ua_prototype.ProgramController
import aut.fhooe.sail.android.md2ua_prototype.interfaces.*
import com.nphausg.app.embeddedserver.utils.NetworkUtils
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebServerModel : ProgramController {

    private val PORT = 6868
    private lateinit var server: ApplicationEngine
    override var info: String? = null


    private val observers = mutableListOf<Observer>()

    init{
        // Start web server
        startServer()

        info =
            String.format("%s:%d", NetworkUtils.getLocalIpAddress(), PORT)
    }

    private fun startServer() {
        server = embeddedServer(Netty, port = PORT) {
            install(CORS) {
                anyHost()
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
            routing {
                get("/") {
                    notifyObservers(ObserverCases.main_route)
                    call.respondText(
                        text = "Hello!! Route to /turnOn or to /turnOff",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/turnOn") {
                    notifyObservers(ObserverCases.turnOn_route)
                    call.respondText(
                        text = "The feature has been turned On!",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/turnOff") {
                    notifyObservers(ObserverCases.turnOff_route)
                    call.respondText(
                        text = "The feature has been turned Off!",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/vibrate") {
                    notifyObservers(ObserverCases.vibrate_route)
                    call.respondText(
                        text = "The watch should vibrate",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/vibrate_rand") {
                    notifyObservers(ObserverCases.vibrate_rand_route)
                    call.respondText(
                        text = "The watch should vibrate in random pattern",
                        contentType = ContentType.Text.Plain
                    )
                }
                get("/vibrate_wrong") {
                    notifyObservers(ObserverCases.vibrate_wrong_route)
                    call.respondText(
                        text = "The watch should vibrate in wrong pattern",
                        contentType = ContentType.Text.Plain
                    )
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            server.start(wait = true)
        }
    }

    override fun stopConnection() {
        stopServer()
    }

    fun stopServer() {
        server.stop(1_000, 2_000)
    }

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(case: ObserverCases) {
        for (observer in observers) {
            observer.update(case)
        }
    }
}