package jp.s64.android.example.architecture.redux.service

import io.reactivex.Single
import jp.s64.android.example.architecture.redux.model.DateModel
import jp.s64.android.example.architecture.service.AppService

class ApiService(protected val service: AppService) {

    fun requestDate(): Single<DateModel> {
        return Single.create<DateModel> {
            it.onSuccess(
                    DateModel(service.date().executesSynchronously())
            )
        }
    }
}
