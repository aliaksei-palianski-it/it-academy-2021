package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_main.*

const val RESULT_MAIN_FRAGMENT = "RESULT_MAIN_FRAGMENT"
const val EXTRA_FRAGMENT_RESULT_MESSAGE = "EXTRA_FRAGMENT_RESULT_MESSAGE"

class MainFragment : Fragment(
    R.layout.fragment_main
), MainFragmentInteractionContract {

    private val viewModel = activityViewModels<SharedViewModel>()
    private val sharedViewModel = viewModels<MainViewModel>()

    companion object {
        fun createInstance(input: String): Fragment {
            val bundle = Bundle()
            bundle.putString("EXTRA_KEY", input)

            return MainFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.sampleLiveData?.observe(viewLifecycleOwner) {
            title?.text = it
        }

        if (savedInstanceState == null) {
            arguments?.getString("EXTRA_KEY", "Default")?.let {
                context?.let { context ->
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        sendActivityResultEx?.setOnClickListener {
            (activity as? MainActivityInteractionContract)?.startForResult()
        }

        sendFragmentResultEx?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment())
                .commit()
        }

        recyclerViewEx?.setOnClickListener {
            (activity as? MainActivityInteractionContract)?.startRecyclerExample()
        }

        listViewEx?.setOnClickListener {
            (activity as? MainActivityInteractionContract)?.startListExample()
        }

        networkEx?.setOnClickListener {
            (activity as? MainActivityInteractionContract)?.startNetworkExample()
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