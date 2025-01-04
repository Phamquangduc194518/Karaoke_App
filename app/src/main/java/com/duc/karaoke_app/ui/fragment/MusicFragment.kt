package com.duc.karaoke_app.ui.fragment

import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.os.Build
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.network.DriveUploader
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentMusicBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File

class MusicFragment : Fragment() {

    private lateinit var musicBinding: FragmentMusicBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private val REQUEST_CODE=100
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
        val seekBar = musicBinding.seekBar
        val tvCurrentTime = musicBinding.tvCurrentTime
        val tvDuration = musicBinding.tvDuration

        DriveUploader.initDriveService(requireActivity())
        val file = File("/storage/emulated/0/Android/data/com.duc.karaoke_app/cache/recording.mp4") // Replace with actual file path
        val folderId = "1sek-KlPDD0HXqqsTy6phX3yunky_N6pl"

        viewModel.duration.observe(viewLifecycleOwner) { duration ->
            seekBar.max = duration.toInt()
            tvDuration.text = formatTime(duration)
        }

        viewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition ->
            seekBar.progress = currentPosition.toInt()
            tvCurrentTime.text = formatTime(currentPosition)
        }

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
        musicBinding.tvRecording.setOnClickListener{
            checkPermissions()
            startRecording()
        }
        musicBinding.tvDone.setOnClickListener{
            stopRecording()
            val fragment = AudioPreviewFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_music_player, fragment).apply {
                commit()
            }
            viewModel.uploadFile(file, folderId)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tvCurrentTime.text = formatTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                viewModel.setSeekbarTracking(true)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setSeekbarTracking(false)
                viewModel.seekTo(seekBar.progress.toLong())
            }
        })

    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }
    private fun startRecording() {
        // Đường dẫn lưu file ghi âm
        outputFile = "${requireActivity().externalCacheDir?.absolutePath}/recording.mp4" // Lưu file trong bộ nhớ cache của ứng dụng

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // Ghi âm từ microphone
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // Định dạng file .mp4
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // Bộ mã hóa âm thanh
            setOutputFile(outputFile) // Đường dẫn lưu file

            try {
                prepare() // Chuẩn bị MediaRecorder
                start() // Bắt đầu ghi âm
                Toast.makeText(requireActivity(),"Start Recording", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun stopRecording() {
        mediaRecorder.apply {
            stop() // Dừng ghi âm
            release() // Giải phóng tài nguyên
            Toast.makeText(requireActivity(),"Stop Record", Toast.LENGTH_SHORT).show()
        }
    }
}
