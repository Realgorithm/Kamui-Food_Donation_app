package com.kamui.fooddonation.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamui.fooddonation.R

class PendingFragment : Fragment() {

    companion object {
        fun newInstance(): PendingFragment {
            return PendingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.history_child_fragment, container, false)
        }
    }

