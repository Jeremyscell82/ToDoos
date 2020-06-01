package com.lloydsbyte.todoos.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.todoos.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            settings_back_fab.setOnClickListener {
                requireActivity().onBackPressed()
            }
            settings_user.setOnClickListener {
                val bottomsheet = UserBottomsheet()
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }
        }
    }
}