package jp.s64.android.example.architecture.redux.ui.main

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import jp.s64.android.example.architecture.redux.App
import jp.s64.android.example.architecture.redux.AppGlobalState
import jp.s64.android.example.architecture.redux.core.Action
import jp.s64.android.example.architecture.redux.core.Dispatcher
import jp.s64.android.example.architecture.redux.core.DispatcherHolder
import jp.s64.android.example.architecture.redux.core.Reducer
import jp.s64.android.example.architecture.redux.core.State
import jp.s64.android.example.architecture.redux.core.Store
import jp.s64.android.example.architecture.redux.core.Props
import jp.s64.android.example.architecture.redux.model.DateModel
import jp.s64.android.example.architecture.redux.service.ApiService
import javax.inject.Inject

data class MainState(
    val isLoading: Boolean,
    val apiResponse: DateModel?
) : State {

    companion object {

        val INITIAL_STATE = MainState(
            false,
            null
        )
    }
}

data class MainProps(
    val buttonEnabled: Boolean,
    val date: String
) : Props

fun mapStateToProps(state: AppGlobalState): MainProps = MainProps(
    state.mainState.isLoading == false,
    state.mainState.apiResponse?.date?.toString() ?: "N/A"
)

object MainReducer : Reducer<AppGlobalState> {

    override fun reduce(oldState: AppGlobalState, action: Action): AppGlobalState {
        return when (action) {
            is DateActions.DateResultSuccess -> {
                oldState.copy(
                    mainState = oldState.mainState.copy(
                        apiResponse = action.apiResponse,
                        isLoading = false
                    )
                )
            }
            is DateActions.DateLoading -> {
                oldState.copy(
                    mainState = oldState.mainState.copy(
                        isLoading = true
                    )
                )
            }
            else -> oldState
        }
    }
}

class MainDispatcher(
    private val dateMiddleware: DateMiddleware,
    store: Store<AppGlobalState>
) : Dispatcher<AppGlobalState, MainProps>(
    store,
    ::mapStateToProps
) {

    fun clickButton() {
        if (!store.currentState().mainState.isLoading) {
            store.dispatch(dateMiddleware.requestDate())
        }
    }
}

class DateMiddleware(
    private val api: ApiService
) {

    fun requestDate(): Observable<DateActions> =
        api.requestDate()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map { DateActions.DateResultSuccess(it) as DateActions }
            .startWith(DateActions.DateLoading)
}

sealed class DateActions : Action {
    data class DateResultSuccess(val apiResponse: DateModel) : DateActions()
    object DateLoading : DateActions()
}

class MainDispatcherHolder @Inject constructor(
    api: ApiService,
    app: App
) : DispatcherHolder<AppGlobalState, MainProps, MainDispatcher>(
    MainDispatcher(
        DateMiddleware(api),
        app.store
    )
)
