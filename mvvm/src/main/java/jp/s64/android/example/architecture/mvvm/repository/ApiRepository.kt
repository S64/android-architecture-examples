package jp.s64.android.example.architecture.mvvm.repository

import jp.s64.android.example.architecture.mvvm.model.DateModel
import jp.s64.android.example.architecture.service.AppService
import java.util.concurrent.CompletableFuture

class ApiRepository constructor(
    val service: AppService
) {

    fun requestDate(): CompletableFuture<DateModel> {
        return CompletableFuture.supplyAsync {
            val date = service.date().executesSynchronously()
            return@supplyAsync DateModel(date)
        }
    }
}
