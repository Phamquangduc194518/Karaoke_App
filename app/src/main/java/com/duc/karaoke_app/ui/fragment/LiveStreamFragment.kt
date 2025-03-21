package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentLiveStreamBinding
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1

class LiveStreamFragment : Fragment(), ConnectCheckerRtmp {

    private lateinit var liveStreamBiding: FragmentLiveStreamBinding
    private lateinit var rtmpCamera1: RtmpCamera1
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private val rtmpUrl = "rtmp://origin.cdn.wowza.com/live/0I5p2opzhJySU7inBFH7z5a0QWCt5855"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        liveStreamBiding= FragmentLiveStreamBinding.inflate(layoutInflater)
        liveStreamBiding.viewModelLiveStream = viewModel
        liveStreamBiding.lifecycleOwner = viewLifecycleOwner
        return liveStreamBiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rtmpCamera1 = RtmpCamera1(liveStreamBiding.surfaceView, this)

        liveStreamBiding.surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) {
                rtmpCamera1.startPreview()
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                if (rtmpCamera1.isStreaming) {
                    rtmpCamera1.stopStream()
                }
                rtmpCamera1.stopPreview()
            }

        })

        viewModel.isClickButtonLive.observe(viewLifecycleOwner){isClickButtonLive->
            if(isClickButtonLive){
                if(!rtmpCamera1.isStreaming){
                    if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()){
                        rtmpCamera1.startStream(rtmpUrl)
                        viewModel.createLiveStream()
                        Log.e("LiveStream","Livestream started")
                    }else {
                        Log.e("LiveStream","Error preparing stream")
                    }
                }
            }else{
                viewModel.updateLiveStream()
                if (rtmpCamera1.isStreaming) {
                    rtmpCamera1.stopStream()
                    Log.e("LiveStream","Livestream stopped")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (rtmpCamera1.isStreaming) {
            rtmpCamera1.stopStream()
        }
        rtmpCamera1.stopPreview()
    }

    override fun onAuthErrorRtmp() {
        Log.e("LiveStream","RTMP authentication error") }

    override fun onAuthSuccessRtmp() {
        Log.e("LiveStream","RTMP authentication successful")
    }

    override fun onConnectionFailedRtmp(reason: String) {
        Log.e("LiveStream","RTMP connection failed: $reason")
        if (rtmpCamera1.isStreaming) {
            rtmpCamera1.stopStream()
        }
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
        Log.e("LiveStream","Connection started: $rtmpUrl")
    }

    override fun onConnectionSuccessRtmp() {
        Log.e("LiveStream","RTMP connection successful")

    }

    override fun onDisconnectRtmp() {
        Log.e("LiveStream","RTMP disconnected")
    }

    override fun onNewBitrateRtmp(bitrate: Long) {

    }
}