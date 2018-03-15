package jp.s64.android.example.architecture.mvvm.di

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import jp.s64.android.example.architecture.mvvm.App
import jp.s64.android.example.architecture.mvvm.repository.ApiRepository
import jp.s64.android.example.architecture.mvvm.view.MainView
import jp.s64.android.example.architecture.service.AppService
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    UiModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: App): Builder

        fun build(): AppComponent
    }
}

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppService(): AppService {
        return AppService()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideApiRepository(service: AppService): ApiRepository {
        return ApiRepository(service)
    }
}

@Module
abstract class UiModule {

    @ContributesAndroidInjector
    abstract fun contributeMainView(): MainView
}
