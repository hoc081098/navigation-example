package com.hoc.example.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoc.example.R
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val mainViewModel by viewModel<MainViewModel>()
    private val photoAdapter = PhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_photos.run {
            adapter = photoAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
        subscribe()
    }

    private fun subscribe() {
        mainViewModel.listPhotos.observe(this, Observer {
            it?.let { photoAdapter.submitList(it) }
        })
        swipe_layout.setOnRefreshListener(mainViewModel::refreshData)
        mainViewModel.isLoading.observe(this, Observer {
            it?.let {
                swipe_layout.post {
                    swipe_layout.isRefreshing = it
                }
            }
        })
        mainViewModel.errorMessage.observe(this, Observer {
            it?.let {
                Snackbar.make(main, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
