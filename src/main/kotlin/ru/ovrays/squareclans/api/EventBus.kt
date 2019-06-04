package ru.ovrays.squareclans.api

import ru.ovrays.squareclans.event.AbstractEvent
import ru.ovrays.squareclans.util.EventClass
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors
import java.util.concurrent.Future



@Suppress("UNCHECKED_CAST")
object EventBus {

    private val pool = Executors.newCachedThreadPool()
    private val completeService = ExecutorCompletionService<Unit>(pool)

    private val eventSubscribers =
        hashMapOf<Class<*>, ArrayList<(event: AbstractEvent) -> Unit>>()

    fun <T : AbstractEvent> registerCallback(clazz: EventClass, callback: (T) -> Unit) {
        if (clazz in eventSubscribers) {
            eventSubscribers[clazz]?.add(callback as (AbstractEvent) -> Unit)
        } else {
            eventSubscribers[clazz] = arrayListOf(callback as (AbstractEvent) -> Unit)
        }
    }

    fun post(event: AbstractEvent): Future<Unit> {
        return completeService.submit {
            eventSubscribers[event::class.java]?.forEach { it(event) }
        }
    }

    inline fun <reified T : AbstractEvent> listen(noinline callback: (T) -> Unit) {
        registerCallback(T::class.java, callback)
    }
}