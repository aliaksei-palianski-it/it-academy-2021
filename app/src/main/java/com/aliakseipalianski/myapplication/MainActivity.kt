package com.aliakseipalianski.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(
    R.layout.activity_main
), IMainView {

    private var presenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.bind(this)

        text.setOnClickListener {
            presenter.onTextButtonClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            // remove presenter from some presenter store
        }

        presenter.unbind()
    }

    override fun showError() {
        //show some error
    }

    override fun showView(arg: SomeModel) {
        // show default state
    }

    override fun showList(arg: List<*>) {
        // show some list
    }
}

interface IMainView {
    fun showError()

    fun showView(arg: SomeModel)

    fun showList(arg: List<*>)
}