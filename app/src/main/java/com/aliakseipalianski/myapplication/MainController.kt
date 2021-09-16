package com.aliakseipalianski.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.view.View


private const val BROAD_CAST_ACTION = "com.aliakseipalianski.myapplication.other.activity.action"

class MainController(private val mainModel: MainModel) : BroadcastReceiver(), View.OnClickListener {

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.text -> mainModel.onTextClicked()
            else -> mainModel.someThingElse()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BROAD_CAST_ACTION -> mainModel.onActionReceived()
            else -> mainModel.someThingElse()
        }
    }

    fun handleLink(intent: Intent) {
        intent.data
        // parse it and do some action
        mainModel.doSomeAction()
    }

    fun handleSearchTextChange(text: Editable?) {
        if (text?.length ?: 0 > 2) {
            mainModel.search(text.toString())
        }
    }

    object OtherActivityBroadcastIntentFilter :
        IntentFilter("com.aliakseipalianski.myapplication.other.activity.action")

}