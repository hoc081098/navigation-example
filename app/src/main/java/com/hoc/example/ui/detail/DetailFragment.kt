package com.hoc.example.ui.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hoc.example.R
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photo = DetailFragmentArgs.fromBundle(arguments).photo
        textViewId.text = photo.id.toString()
        textViewTitle.text = photo.title
        Glide.with(imageView.context)
            .load(photo.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp))
            .apply(RequestOptions.fitCenterTransform())
            .into(imageView)
    }
}
