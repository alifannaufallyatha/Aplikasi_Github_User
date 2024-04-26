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
import com.dicoding.aplikasigithubuser.adapter.FollowersAdapter
import com.dicoding.aplikasigithubuser.data.remote.response.FollowersResponseItem
import com.dicoding.aplikasigithubuser.databinding.FragmentFollowersBinding
import com.dicoding.aplikasigithubuser.viewmodel.FollowersViewModel


class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var followersViewModel: FollowersViewModel
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString("username")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)

        binding.rvFragmentFollowers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFragmentFollowers.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager(requireContext()).orientation
            )
        )
        followersViewModel.getFollowerUsername(username.toString())

        followersViewModel.listFollowers.observe(viewLifecycleOwner) { consumeUserFollowers ->
            setFollowersData(
                consumeUserFollowers
            )
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followersViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            showError(error)
        }
    }
    private fun setFollowersData(userFollowers : List<FollowersResponseItem>){
        val adapter = FollowersAdapter()
        adapter.submitList(userFollowers)
        binding.rvFragmentFollowers.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

}
