package taskly.system.event

data class BroadcastedEvent<out T> (
        val type: EventType,
        val data: T)