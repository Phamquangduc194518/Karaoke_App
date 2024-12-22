package com.duc.karaoke_app.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.LiveStreamRepository
import com.duc.karaoke_app.data.viewmodel.LiveStreamViewModel
import com.duc.karaoke_app.data.viewmodel.LiveStreamViewModelFactory
import com.pedro.rtplibrary.rtmp.RtmpCamera1


class LiveStreamFragment : Fragment() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var buttonStartStop: Button
    private lateinit var liveStreamViewModel: LiveStreamViewModel

    private val rtmpUrl = "rtmp://origin.cdn.wowza.com/live/0I5p2SaMygBGk1ESIoQYBiDeY"

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_stream, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        surfaceView = view.findViewById(R.id.cameraView)
        buttonStartStop = view.findViewById(R.id.btn_live)

        if (!hasPermissions()) {
            requestPermissions(permissions, REQUEST_CODE)
        } else {
            init()
        }
    }

    private fun init() {
        val liveStreamRepository = LiveStreamRepository()
        val rtmpCamera = RtmpCamera1(surfaceView, liveStreamRepository)
        liveStreamRepository.setRtmpCamera(rtmpCamera)
        val factory = LiveStreamViewModelFactory(liveStreamRepository)
        liveStreamViewModel = ViewModelProvider(this, factory).get(LiveStreamViewModel::class.java)

        surfaceView.holder.addCallback(liveStreamRepository)

        liveStreamViewModel.isStreaming.observe(viewLifecycleOwner) { isStreaming ->
            buttonStartStop.text = if (isStreaming) "Stop" else "Start"
        }

        liveStreamViewModel.connectionStatus.observe(viewLifecycleOwner) { status ->
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
        }

        buttonStartStop.setOnClickListener {
            if (!liveStreamViewModel.isStreaming.value!!) {
                if (liveStreamViewModel.prepareStream()) {
                    liveStreamViewModel.startStream(rtmpUrl)
                } else {
                    Toast.makeText(context, "Error preparing stream.", Toast.LENGTH_SHORT).show()
                }
            } else {
                liveStreamViewModel.stopStream()
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private const val REQUEST_CODE = 1234
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            init()
        } else {
            Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

}