package jp.s64.android.example.architecture.redux.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import jp.s64.android.example.architecture.redux.R
import jp.s64.android.example.architecture.redux.core.DispatcherBinder
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var holder: MainDispatcherHolder

    val button: Button by lazy { findViewById<Button>(R.id.button) }
    val text: TextView by lazy { findViewById<TextView>(R.id.text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        DispatcherBinder(this.lifecycle, holder.dispatcher) {
            button.isEnabled = it.buttonEnabled
            text.text = it.date
        }

        button.setOnClickListener {
            holder.dispatcher.clickButton()
        }
    }
}
