package com.duc.karaoke_app.data.viewmodel

import android.view.SurfaceHolder
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import net.ossrs.rtmp.ConnectCheckerRtmp

class LiveStreamRepository(): ConnectCheckerRtmp, SurfaceHolder.Callback {

    private lateinit var rtmpCamera: RtmpCamera1
    var onConnectionSuccess: (() -> Unit)?=null
    var onConnectionFailed: ((reason: String) -> Unit)? = null
    var onDisconnect: (() -> Unit)? = null
    var onAuthError: (() -> Unit)? = null
    var onAuthSuccess: (() -> Unit)? = null

    fun prepareStream(): Boolean {
        return rtmpCamera.prepareAudio() && rtmpCamera.prepareVideo()
    }
    fun startStream(rtmpUrl: String) {
        rtmpCamera.startStream(rtmpUrl)
    }
    fun setRtmpCamera(rtmpCamera: RtmpCamera1) {
        this.rtmpCamera = rtmpCamera
    }

    fun stopStream() {
        if (rtmpCamera.isStreaming) {
            rtmpCamera.stopStream()
        }
    }

    fun isStreaming(): Boolean {
        return rtmpCamera.isStreaming
    }

    fun startPreview() {
        rtmpCamera.startPreview()
    }

    fun stopPreview() {
        rtmpCamera.stopPreview()
    }
    override fun surfaceCreated(p0: SurfaceHolder) {
        startPreview()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stopStream()
        stopPreview()
    }

    override fun onConnectionSuccessRtmp() {
        onConnectionSuccess?.invoke()
    }

    override fun onConnectionFailedRtmp(reason: String) {
        onConnectionFailed?.invoke(reason)
    }

    override fun onNewBitrateRtmp(bitrate: Long) {

    }

    override fun onDisconnectRtmp() {
        onDisconnect?.invoke()
    }

    override fun onAuthErrorRtmp() {
        onAuthError?.invoke()
    }

    override fun onAuthSuccessRtmp() {
        onAuthSuccess?.invoke()
    }

}