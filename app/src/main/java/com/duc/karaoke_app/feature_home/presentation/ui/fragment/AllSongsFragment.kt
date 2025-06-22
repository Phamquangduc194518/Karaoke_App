package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentAllSongsBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.feature_player.presentation.ui.MusicPlayerActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class AllSongsFragment : Fragment() {

    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private var selectedBarIndex = -1
    private lateinit var allSongsBinding: FragmentAllSongsBinding
    private lateinit var barChart: BarChart
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
        barChart = allSongsBinding.barChart
        allSongsBinding.rcvAllSongs.layoutManager= LinearLayoutManager(requireContext())

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

        viewModel.topSongChart()
        viewModel.barEntries.observe(viewLifecycleOwner){ entries->
            if (entries != null){
                val songNames = viewModel.songNames.value ?: arrayOf("", "", "","","")
                // Tạo BarDataSet với danh sách BarEntry
                val barDataSet = BarDataSet(entries, "Số tim")
                barDataSet.color = Color.parseColor("#FFA726")
                barDataSet.valueTextSize = 10f
                barDataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
                // Tạo BarData và gán cho chart
                val barData = com.github.mikephil.charting.data.BarData(barDataSet)
                barData.barWidth = 0.5f
                barChart.data = barData

                val xAxis = barChart.xAxis
                xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        // Chỉ hiển thị tên nếu index bằng với selectedBarIndex
                        return if (index == selectedBarIndex && index in songNames.indices) songNames[index] else ""
                    }
                }
                val yAxis = barChart.axisLeft
                yAxis.granularity = 1f                // Mỗi đơn vị là 1
                yAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
                barChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener{
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e?.let {
                            selectedBarIndex = it.x.toInt()
                            barChart.invalidate()
                        }
                    }

                    override fun onNothingSelected() {
                        selectedBarIndex = -1
                        barChart.invalidate()
                    }

                })
                barChart.animateXY(1500, 1500)
                barChart.description.isEnabled = false
                barChart.axisRight.isEnabled = false
                barChart.setDoubleTapToZoomEnabled(false)
                barChart.invalidate()
            }
        }
    }



}