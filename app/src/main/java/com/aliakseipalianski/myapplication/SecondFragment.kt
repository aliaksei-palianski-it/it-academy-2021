package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

class SecondFragment : Fragment(
    R.layout.fragment_second
) {
    /*
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = inflater.inflate(R.layout.fragment_main, container, false)
    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.sendResultButton)?.setOnClickListener {
            setFragmentResult(
                RESULT_MAIN_FRAGMENT,
                bundleOf(EXTRA_FRAGMENT_RESULT_MESSAGE to getString(R.string.app_name))
            )

            (activity as? MainActivityInteractionContract)?.navigateToMainFragment()
        }
    }
}