package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duc.karaoke_app.R
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding
import com.duc.karaoke_app.databinding.FragmentMusicBinding

class AlbumDetailsFragment : Fragment() {

    private lateinit var albumDetailBinding: FragmentAlbumDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumDetailBinding = FragmentAlbumDetailsBinding.inflate(layoutInflater)
        return albumDetailBinding.root
    }

}