package jp.s64.android.example.archtecture.mvc

import jp.s64.android.example.architecture.service.AppService
import jp.s64.android.example.archtecture.mvc.model.ApiModel

object ModelLocator {

    @JvmStatic
    fun with(self: Controller): IModelLocator {
        return (self.application as App).modelLocator
    }
}

interface IModelLocator {

    fun getService(): AppService

    fun getApiModel(): ApiModel
}
