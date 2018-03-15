package jp.s64.android.example.architecture.redux

import dagger.android.support.DaggerApplication
import jp.s64.android.example.architecture.redux.core.State
import jp.s64.android.example.architecture.redux.core.Store
import jp.s64.android.example.architecture.redux.core.combineReducers
import jp.s64.android.example.architecture.redux.di.DaggerAppComponent
import jp.s64.android.example.architecture.redux.ui.main.MainReducer
import jp.s64.android.example.architecture.redux.ui.main.MainState

class App : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent
            .builder()
            .app(this)
            .build()

    val store: Store<AppGlobalState> = Store(
        AppGlobalState.INITIAL_STATE,
        combineReducers(
            MainReducer
        )
    )
}

data class AppGlobalState(
    val mainState: MainState
) : State {

    companion object {
        val INITIAL_STATE = AppGlobalState(
            MainState.INITIAL_STATE
        )
    }
}
