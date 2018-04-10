package taskly.system.notification

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.ClientHttpRequestFactory



@Service
class ExpoNotificationService {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(val status: String) {
        constructor(): this("")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DataExpoNotification(val data: Data) {
        constructor(): this(Data())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class ExpoNotification(
            val to: String,
            val title: String,
            val body: String,
            val sound: String = "default") {

        constructor(notification: Notification) :
                this(notification.user?.expoNotificationToken ?: "",
                        notification.title,
                        notification.message)
    }

    fun send(notification: Notification): DataExpoNotification {
        val url = "https://exp.host/--/api/v2/push/send"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
                .also { it.add("accept-encoding", "gzip, deflate") }
                .also { it.add("accept", "application/json") }
                .also { it.add("content-type", "application/json") }
        val body = ExpoNotification(notification)
        val request = HttpEntity<ExpoNotification>(body, headers)
        return restTemplate.postForObject(url, request, DataExpoNotification::class.java)
    }
}