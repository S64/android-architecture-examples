package jp.s64.android.example.architecture.redux.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import jp.s64.android.example.architecture.redux.App
import jp.s64.android.example.architecture.redux.service.ApiService
import jp.s64.android.example.architecture.redux.ui.main.MainActivity
import jp.s64.android.example.architecture.redux.ui.main.MainDispatcherHolder
import jp.s64.android.example.architecture.service.AppService
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

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
    fun provideApiService(service: AppService): ApiService {
        return ApiService(service)
    }
}

@Module
abstract class UiModule {

    @ContributesAndroidInjector(modules = [

    ])
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainDispatcherHolder::class)
    abstract fun bindMainDispatcherHolder(viewModel: MainDispatcherHolder): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<ViewModel>? = null

        if (!creators.containsKey(modelClass)) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        } else {
            creator = creators[modelClass]
        }

        if (creator == null) {
            throw IllegalArgumentException(modelClass.name + "is unknown.")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
