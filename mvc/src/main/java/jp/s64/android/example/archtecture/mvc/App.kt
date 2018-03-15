package jp.s64.android.example.archtecture.mvc

import android.app.Application
import jp.s64.android.example.architecture.service.AppService
import jp.s64.android.example.archtecture.mvc.model.ApiModel

class App : Application() {

    val modelLocator: IModelLocator = object : IModelLocator {

        override fun getApiModel() = ApiModel(getService())

        override fun getService() = AppService()
    }
}
