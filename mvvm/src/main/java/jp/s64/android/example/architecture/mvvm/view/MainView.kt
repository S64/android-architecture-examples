package jp.s64.android.example.architecture.mvvm.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import dagger.android.AndroidInjection
import jp.s64.android.example.architecture.mvvm.Architecture_View
import jp.s64.android.example.architecture.mvvm.R
import jp.s64.android.example.architecture.mvvm.databinding.ViewMainBinding
import jp.s64.android.example.architecture.mvvm.repository.ApiRepository
import jp.s64.android.example.architecture.mvvm.viewmodel.MainViewModel
import javax.inject.Inject

class MainView : Architecture_View() {

    lateinit var binding: ViewMainBinding
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var api: ApiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView<ViewMainBinding>(this, R.layout.view_main)

        viewModel = ViewModelProviders
            .of(this)
            .get(MainViewModel::class.java)

        viewModel.api = api

        binding.setLifecycleOwner(this)
        binding.vm = viewModel
    }
}
