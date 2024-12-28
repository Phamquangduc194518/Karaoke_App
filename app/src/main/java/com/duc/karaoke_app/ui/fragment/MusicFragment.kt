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
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentMusicBinding
import java.io.File


class MusicFragment : Fragment() {

    private lateinit var musicBinding: FragmentMusicBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private lateinit var audioFile: File
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        musicBinding = FragmentMusicBinding.inflate(layoutInflater)
        musicBinding.viewModelMusic= viewModel
        musicBinding.lifecycleOwner= viewLifecycleOwner
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

        musicBinding.playPauseButton.setOnClickListener {
            val songUrl = "https://drive.google.com/uc?export=download&id=1OsvmfPCh10cSMHYWpMmoqUlYs4bdlFCk" // Replace with actual URL
            viewModel.playSong(songUrl)
        }
        musicBinding.tvRecording.setOnClickListener{

            if (checkPermissions()) {
                startRecording()
            } else {
                Toast.makeText(requireContext(), "Permissions are not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allGranted) {
                Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permissions denied. Please grant permissions to proceed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val notGranted = permissions.any {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 101)
        }

        return !notGranted
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    requireActivity().finish() // Đóng ứng dụng nếu quyền bị từ chối
                }
            }
        }

    private fun startRecording() {
        try {
            // Khởi tạo file ghi âm
            audioFile = File(
                requireContext().getExternalFilesDir(null),
                "recorded_audio_${System.currentTimeMillis()}.mp4"
            )

            val audioSource = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaRecorder.AudioSource.REMOTE_SUBMIX
            } else {
                MediaRecorder.AudioSource.MIC
            }

            // Cấu hình MediaRecorder
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(audioSource)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile.absolutePath)
                prepare()
                start()
            }

            isRecording = true
            Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to start recording: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false

            Toast.makeText(requireActivity(), "Recording saved: ${audioFile.absolutePath}", Toast.LENGTH_SHORT).show()

            // Lưu file vào thư viện ảnh
            saveToLibrary(audioFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToLibrary(file: File) {
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
            put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/")
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            requireContext().contentResolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().copyTo(outputStream)
            }
            Toast.makeText(requireActivity(), "Saved to Music Library", Toast.LENGTH_SHORT).show()
        }
    }




}