package com.orm.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.Notification
import com.orm.databinding.FragmentNotificationsBinding
import com.orm.ui.adapter.ProfileNotificationAdapter
import com.orm.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val notificationViewModel: NotificationViewModel by viewModels()

    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileNotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            notificationViewModel.getAllNotifications()
            notificationViewModel.notifications.observe(viewLifecycleOwner) { notifications ->
                setupAdapter(notifications)
            }
        } catch (e: Exception) {
            Log.e("notification", "notification list", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter(notifications: List<Notification>) {
        if (_binding == null) return

        val reversedNotifications = notifications.reversed()

        adapter = ProfileNotificationAdapter(reversedNotifications.map {
            Notification.toRecyclerViewNotificationItem(it)
        })

        adapter.setItemClickListener(object : ProfileNotificationAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
            }
        })

        binding.count = reversedNotifications.count()

        rvBoard.adapter = adapter
        rvBoard.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
