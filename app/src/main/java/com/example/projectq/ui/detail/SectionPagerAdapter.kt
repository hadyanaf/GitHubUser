package com.example.projectq.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectq.data.util.IntentConstant

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = DetailFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(IntentConstant.ARG_POSITION, position)
            putString(IntentConstant.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

    fun setUsername(username: String) {
        this.username = username
    }
}