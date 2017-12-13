package taskly.system.command

data class CommandDecorator<out T, out U>(val command: Command<T, U>) :
        Command<T, U>