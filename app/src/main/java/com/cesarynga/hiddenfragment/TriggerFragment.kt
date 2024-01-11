package com.cesarynga.hiddenfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit


class TriggerFragment : Fragment() {

    private lateinit var backPressedCallback: OnBackPressedCallback
    private var isPreloadEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback is only called when MyFragment is at least started
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this, false) {
            toggleShowFragment(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trigger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonShowFragment = view.findViewById<Button>(R.id.button_show_fragment)
        val checkBoxPreload = view.findViewById<CheckBox>(R.id.checkbox_preload)
        buttonShowFragment.setOnClickListener {
            toggleShowFragment(true)
        }
        checkBoxPreload.setOnCheckedChangeListener { _, isChecked ->
            isPreloadEnable = isChecked
            if (isPreloadEnable) {
                addFragment(true)
            } else {
                childFragmentManager.findFragmentByTag("hidden")?.let { hiddenFragment ->
                    removeFragment(hiddenFragment)
                }
            }
        }
    }

    private fun addFragment(hidden: Boolean) {
        val url = "https://developer.android.com"
        val bundle = bundleOf("url" to url)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            if (!hidden) {
                setCustomAnimations(androidx.appcompat.R.anim.abc_slide_in_bottom, 0)
            }
            val f = HiddenFragment()
            f.arguments = bundle
            add(R.id.fragment_container_view, f, "hidden")
            if (hidden) {
                hide(f)
            }
        }
    }

    private fun removeFragment(f: Fragment) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(0, androidx.appcompat.R.anim.abc_slide_out_bottom)
            remove(f)
        }
    }

    private fun hideFragment(f: Fragment) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(0, androidx.appcompat.R.anim.abc_slide_out_bottom)
            hide(f)
        }
    }

    private fun showFragment(f: Fragment) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(androidx.appcompat.R.anim.abc_slide_in_bottom, 0)
            show(f)
        }
    }

    private fun toggleShowFragment(isHidden: Boolean) {
        childFragmentManager.findFragmentByTag("hidden")
            ?.let { hiddenFragment ->
                if (isHidden) {
                    showFragment(hiddenFragment)
                    backPressedCallback.isEnabled = true
                } else {
                    if (isPreloadEnable) {
                        hideFragment(hiddenFragment)
                    } else {
                        removeFragment(hiddenFragment)
                    }
                    backPressedCallback.isEnabled = false
                }
            } ?: run {
            addFragment(false)
            backPressedCallback.isEnabled = true
        }
    }
}