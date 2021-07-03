package com.example.pecodetestapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.pecodetestapp.databinding.FragmentNotificationBinding


class NotificationFragment(
    var position: Int
) : Fragment() {
    private val CHANNEL_ID = position.toString()
    private val CHANNEL_NAME = NotificationFragment::class.simpleName
    private val NOTIFICATION_ID = position

    private lateinit var manager: NotificationManager

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()

        val notificationManager = NotificationManagerCompat.from(requireContext())

        val pagerAdapter = (activity as MainActivity).pagerAdapter

        binding.btnAdd.setOnClickListener {
            val pagerPosition = pagerAdapter.list.maxOf { it.position } + 1
            val newFragment = NotificationFragment(pagerPosition)
            pagerAdapter.list.add(newFragment)
            pagerAdapter.notifyDataSetChanged()
            (requireActivity() as MainActivity).binding.pager.currentItem = pagerPosition - 1
        }
        binding.btnRemove.setOnClickListener {
            if (pagerAdapter.list.size > 1) {
                pagerAdapter.list.remove(this)
                pagerAdapter.notifyDataSetChanged()
            }
        }

        binding.textView.text = position.toString()

        binding.btnCreateNotification.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("fragment", position)

            val pendingIntent = PendingIntent.getActivity(
                requireContext(),
                System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT
            )

            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle("Chat heads active")
                .setContentText("Notification ${position}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("NotificationFragment", "$position")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("NotificationFragment", "$position")
    }
}