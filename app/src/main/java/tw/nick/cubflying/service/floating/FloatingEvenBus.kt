package tw.nick.cubflying.service.floating

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object FloatingEventBus {
    private val _commands = MutableSharedFlow<FloatingCommand>(extraBufferCapacity = 10)
    val commands = _commands.asSharedFlow()

    fun send(command: FloatingCommand) {
        _commands.tryEmit(command)
    }
}