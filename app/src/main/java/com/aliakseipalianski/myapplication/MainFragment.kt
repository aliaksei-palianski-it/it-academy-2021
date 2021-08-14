package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import kotlinx.android.synthetic.main.fragment_main.*

const val RESULT_MAIN_FRAGMENT = "RESULT_MAIN_FRAGMENT"
const val EXTRA_FRAGMENT_RESULT_MESSAGE = "EXTRA_FRAGMENT_RESULT_MESSAGE"

class MainFragment : Fragment(
    R.layout.fragment_main
), MainFragmentInteractionContract {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendActivityResultEx?.setOnClickListener {
            (activity as? MainActivityInteractionContract)?.startForResult()
        }

        sendFragmentResultEx?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment())
                .commit()
        }

        setFragmentResultListener(RESULT_MAIN_FRAGMENT) { _, bundle ->
            bundle.getString(EXTRA_FRAGMENT_RESULT_MESSAGE)?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun handleResult(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
    }
}

interface MainFragmentInteractionContract {
    fun handleResult(string: String)
}