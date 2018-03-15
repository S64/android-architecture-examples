package jp.s64.android.example.archtecture.mvc.model

import jp.s64.android.example.architecture.service.AppService
import jp.s64.android.example.archtecture.mvc.entity.DateEntity
import java.util.concurrent.CompletableFuture

class ApiModel(val service: AppService) {

    fun getDateEntity(): CompletableFuture<DateEntity> {
        return CompletableFuture.supplyAsync {
            val date = service.date().executesSynchronously()
            return@supplyAsync DateEntity(date)
        }
    }
}
