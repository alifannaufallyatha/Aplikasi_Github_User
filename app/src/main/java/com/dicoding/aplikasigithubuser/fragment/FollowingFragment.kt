package com.dicoding.aplikasigithubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.adapter.FollowingAdapter
import com.dicoding.aplikasigithubuser.data.remote.response.FollowingResponseItem
import com.dicoding.aplikasigithubuser.databinding.FragmentFollowingBinding
import com.dicoding.aplikasigithubuser.viewmodel.FollowingViewModel


class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel
    private var usernameFollowing: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usernameFollowing = it.getString("username")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        binding.rvFragmentFollowing.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFragmentFollowing.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager(requireContext()).orientation
            )
        )
        followingViewModel.getFollowingUsername(usernameFollowing.toString())

        followingViewModel.listFollowing.observe(viewLifecycleOwner) { consumeUserFollowers -> setFollowingData(consumeUserFollowers) }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            showError(error)
        }
    }
    private fun setFollowingData(followings: List<FollowingResponseItem>) {
        val adapter = FollowingAdapter()
        adapter.submitList(followings)
        binding.rvFragmentFollowing.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }


}