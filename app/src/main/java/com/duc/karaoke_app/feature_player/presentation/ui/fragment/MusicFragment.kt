package com.duc.karaoke_app.feature_player.presentation.ui.fragment

import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_favorite.data.FavoriteSongsRepository
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModelFactory
import com.duc.karaoke_app.databinding.FragmentMusicBinding


class MusicFragment : Fragment() {

    private lateinit var musicBinding: FragmentMusicBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        MusicPlayerViewModelFactory(
            Repository(),
            FavoriteSongsRepository(requireContext()), requireActivity().application)
    }
    private val REQUEST_CODE = 100
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var outputFile: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        musicBinding = FragmentMusicBinding.inflate(layoutInflater)
        musicBinding.viewModelMusic = viewModel
        musicBinding.lifecycleOwner = viewLifecycleOwner
        return musicBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.song.observe(viewLifecycleOwner) { song ->
            if (song != null) {
                Log.e("song in MusicFragment", "Song title: ${song.title}")
            } else {
                Log.e("song in MusicFragment", "Song is null")
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigateBack ->
            if (shouldNavigateBack == true) {
                requireActivity().finish()
            }
        }
        musicBinding.tvRecording.setOnClickListener {
            checkPermissions()
            startRecording()
        }
        musicBinding.tvDone.setOnClickListener {
            viewModel.releaseExoPlayer()
            val fragment = AudioPreviewFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_music_player, fragment).apply {
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopRecording()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE
            )
        }
    }

    private fun startRecording() {
        // Đường dẫn lưu file ghi âm
        outputFile =
            "${requireActivity().externalCacheDir?.absolutePath}/recording.mp4" // Lưu file trong bộ nhớ cache của ứng dụng

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // Ghi âm từ microphone
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // Định dạng file .mp4
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // Bộ mã hóa âm thanh
            setOutputFile(outputFile) // Đường dẫn lưu file

            try {
                prepare() // Chuẩn bị MediaRecorder
                start() // Bắt đầu ghi âm
                Toast.makeText(requireActivity(), "Start Recording", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        if (::mediaRecorder.isInitialized) {
            mediaRecorder.apply {
                stop() // Dừng ghi âm
                release() // Giải phóng tài nguyên
                Toast.makeText(requireActivity(), "Stop Record", Toast.LENGTH_SHORT).show()
            }
        }else {
            Log.e("MediaRecorder", "stopRecording called but mediaRecorder is not initialized.")
        }
    }


}
