package jp.s64.android.example.architecture.mvvm.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import jp.s64.android.example.architecture.mvvm.repository.ApiRepository
import java.time.ZonedDateTime

class MainViewModel : ViewModel() {

    lateinit var api: ApiRepository

    val date: MutableLiveData<ZonedDateTime> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoading.value = false
    }

    fun requestDate(view: View) {
        isLoading.value = true
        api.requestDate()
            .thenAccept({
                isLoading.postValue(false)
                date.postValue(it.date)
            })
    }
}
