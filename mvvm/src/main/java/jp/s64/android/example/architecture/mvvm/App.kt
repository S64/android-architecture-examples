package jp.s64.android.example.architecture.mvvm

import dagger.android.support.DaggerApplication
import jp.s64.android.example.architecture.mvvm.di.DaggerAppComponent

class App : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent
        .builder()
        .app(this)
        .build()
}
