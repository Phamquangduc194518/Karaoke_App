package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.github.mikephil.charting.data.Entry
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentAllSongsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AllSongsFragment : Fragment() {

    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private lateinit var allSongsBinding: FragmentAllSongsBinding
    private lateinit var lineChart: LineChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allSongsBinding = FragmentAllSongsBinding.inflate(layoutInflater)
        allSongsBinding.viewModelAllSongs= viewModel
        allSongsBinding.lifecycleOwner= viewLifecycleOwner
        return allSongsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = allSongsBinding.lineChart
        val entriesA = mutableListOf<Entry>().apply {
            add(Entry(1f, 10f))  // Ngày 1 -> 10 tim
            add(Entry(2f, 15f))  // Ngày 2 -> 15 tim
            add(Entry(3f, 30f))  // Ngày 3 -> 30 tim
            add(Entry(4f, 40f))  // Ngày 4 -> 25 tim
        }
        val dataSetA = LineDataSet(entriesA, "Bài hát A").apply {
            color = Color.RED           // Màu đường
            setCircleColor(Color.RED)   // Màu chấm tròn
            lineWidth = 2f
            circleRadius = 4f
            mode = LineDataSet.Mode.CUBIC_BEZIER // Đường cong
        }
        val entriesB = mutableListOf<Entry>().apply {
            add(Entry(1f, 5f))
            add(Entry(5f, 20f))
            add(Entry(7f, 25f))
            add(Entry(9f, 40f))
        }
        val dataSetB = LineDataSet(entriesB, "Bài hát B").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            lineWidth = 2f
            circleRadius = 4f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        val lineData = LineData(dataSetA, dataSetB)
        // 5. Gán dữ liệu cho LineChart
        lineChart.data = lineData
        lineChart.invalidate() // Refresh biểu đồ
        lineChart.animateX(1000)
        allSongsBinding.rcvAllSongs.layoutManager= LinearLayoutManager(requireContext())
        viewModel.getIsFavoriteToSongIDOfAllSong()

        viewModel.selectedSong.observe(viewLifecycleOwner) { song ->
            song.let {
                Log.e("HomeFragment", "Selected song: ${song}")
                val intent = Intent(requireActivity(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Music_Fragment")
                    putExtra("Play_List", song)
                }
                startActivity(intent)

            }
        }
    }



}