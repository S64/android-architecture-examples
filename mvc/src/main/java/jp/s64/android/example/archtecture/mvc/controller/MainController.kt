package jp.s64.android.example.archtecture.mvc.controller

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import jp.s64.android.example.archtecture.mvc.Controller
import jp.s64.android.example.archtecture.mvc.ModelLocator
import jp.s64.android.example.archtecture.mvc.R
import jp.s64.android.example.archtecture.mvc.model.ApiModel

class MainController : Controller() {

    lateinit var api: ApiModel

    val text by lazy { findViewById<TextView>(R.id.text) }
    val button by lazy { findViewById<Button>(R.id.button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controller_main)

        api = ModelLocator
            .with(this)
            .getApiModel()

        button.setOnClickListener {
            button.isEnabled = false
            api.getDateEntity()
                .thenAccept {
                    Handler(Looper.getMainLooper())
                        .post({
                            text.text = it.date.toString()
                            button.isEnabled = true
                        })
                }
        }
    }
}
