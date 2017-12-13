package taskly.system.command

interface Command<out T, out U> {
    fun execute(): T = throw UnsupportedOperationException()
    fun undo(): U = throw UnsupportedOperationException()
}

