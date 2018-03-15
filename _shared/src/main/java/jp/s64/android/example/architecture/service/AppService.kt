package jp.s64.android.example.architecture.service

import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class AppService {

    fun date(): Caller<ZonedDateTime> {
        return object : Caller<ZonedDateTime>() {

            override fun createValue(): ZonedDateTime {
                TimeUnit.SECONDS.sleep(1)
                return ZonedDateTime.now()
            }
        }
    }

    abstract class Caller<T>() {

        protected abstract fun createValue(): T

        fun executesSynchronously(): T {
            return createValue()
        }
    }
}
