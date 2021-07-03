package com.example.pecodetestapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pecodetestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pagerAdapter = ScreenSlidePagerAdapter(this)

        binding.pager.adapter = pagerAdapter

    }

    override fun onBackPressed() {
        if (binding.pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.pager.currentItem = binding.pager.currentItem - 1
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            binding.pager.currentItem = it.getIntExtra("fragment", 0) - 1
        }
    }

    inner class ScreenSlidePagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
        var list = mutableListOf(NotificationFragment(1))

        override fun getItemId(position: Int): Long = list[position].position.toLong()
        override fun getItemCount(): Int = list.size
        override fun createFragment(position: Int): Fragment = list[position]
    }
}