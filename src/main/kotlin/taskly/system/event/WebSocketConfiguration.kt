package taskly.system.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArrayList

@Component
class WebSocketHandler : TextWebSocketHandler() {

    private var sessions: MutableList<WebSocketSession> = CopyOnWriteArrayList()

    @Autowired
    private lateinit var mapper: ObjectMapper

    fun send(event: BroadcastedEvent<Any>) {
        println("Sending message: $event")
        sessions.filter {it.isOpen} .forEach {it.sendMessage(TextMessage(mapper.writeValueAsString(event)))}
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        super.handleTextMessage(session, message)
        sessions.filter { it.isOpen }.forEach { it.sendMessage(message) }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        session.let { sessions.add(it) }
    }
}

@Configuration
@EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer {

    @Autowired
    private lateinit var handler: WebSocketHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(handler, "/ws_reports")
    }
}