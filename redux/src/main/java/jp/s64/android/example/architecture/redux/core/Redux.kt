package jp.s64.android.example.architecture.redux.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.android.schedulers.AndroidSchedulers

interface Action
interface State

interface Reducer<S : State> {
    fun reduce(oldState: S, action: Action): S
}

interface IStore<S : State> {
    fun dispatch(action: Action)
    fun dispatch(actions: Observable<out Action>)
    fun asObservable(): Observable<S>
    fun currentState(): S
    fun tearDown()
}

class Store<S : State>(
    private val initialState: S,
    reducer: Reducer<S>
) : IStore<S> {

    private val actionsSubject = PublishSubject.create<Action>()
    private val statesSubject = BehaviorSubject.create<S>()

    private val disposables = CompositeDisposable()

    init {
        disposables.add(
                actionsSubject
                        .scan(initialState, reducer::reduce)
                        .distinctUntilChanged()
                        .subscribe { statesSubject.onNext(it) }
        )
    }

    override fun dispatch(action: Action) {
        actionsSubject.onNext(action)
    }

    override fun dispatch(actions: Observable<out Action>) {
        disposables.add(actions.subscribe { dispatch(it) })
    }

    override fun asObservable(): Observable<S> = statesSubject

    override fun currentState(): S {
        return if (statesSubject.hasValue())
            statesSubject.value
        else
            initialState
    }

    override fun tearDown() {
        disposables.clear()
    }
}

fun <S : State> combineReducers(vararg reducers: Reducer<S>): Reducer<S> {
    return object : Reducer<S> {

        override fun reduce(oldState: S, action: Action): S {
            var affectedState: S = oldState
            for (reducer in reducers) {
                affectedState = reducer.reduce(affectedState, action)
            }
            return affectedState
        }
    }
}

interface Props

abstract class Dispatcher<S : State, out P : Props>(
    protected val store: Store<S>,
    private val viewStateMapper: (state: S) -> P
) {

    companion object {
        val mainScheduler: Scheduler = AndroidSchedulers.mainThread()
    }

    protected open val state: Observable<S> = store.asObservable()
    private val disposables = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun subscribe(onNext: (props: P) -> Unit): Disposable = state
        .map(viewStateMapper)
        .observeOn(mainScheduler)
        .subscribe(onNext)

    fun onCleared() {
        store.tearDown()
        disposables.clear()
    }
}

abstract class DispatcherHolder<S : State, out P : Props, out D : Dispatcher<S, P>>
    constructor(
        val dispatcher: D
    ) : ViewModel() {

    override fun onCleared() {
        dispatcher.onCleared()
        super.onCleared()
    }
}

class DispatcherBinder<S : State, P : Props>
    constructor(
        lifecycle: Lifecycle,
        private val dispatcher: Dispatcher<S, P>,
        private val onNext: (props: P) -> Unit
    ) : LifecycleObserver {

    private var disposable: Disposable? = null

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        disposable = dispatcher.subscribe(onNext)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposable = null
    }
}
