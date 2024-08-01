package com.orm.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.orm.R
import com.orm.databinding.FragmentGraphBinding

class  GraphFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!
    private var data: List<Pair<Float, Float>>? = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root = binding.root

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            val dataArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable("data", ArrayList::class.java) as? ArrayList<Pair<Float, Float>>
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable("data") as? ArrayList<Pair<Float, Float>>
            }
            dataArray?.let { data = it }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart: LineChart = view.findViewById(R.id.graph)

        // 전달된 데이터가 없으면 예외 처리
        val chartData = data ?: emptyList()

        // 데이터 설정
        setDataToChart(lineChart, chartData)
    }

    // 데이터를 차트에 설정하는 함수
    private fun setDataToChart(lineChart: LineChart, data: List<Pair<Float, Float>>) {
        val entries = data.map { Entry(it.first, it.second) }

        val dataSet = LineDataSet(entries, "").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            setDrawCircles(false)
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // x축 설정
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setLabelCount(10, true)

        // y축 설정
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.axisMinimum = 0f
//        yAxisLeft.axisMaximum = 750f
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawAxisLine(true)
        yAxisLeft.setLabelCount(10, true)

        // 오른쪽 y축 비활성화
        lineChart.axisRight.isEnabled = false

        // 차트에 선 및 기타 설정
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.setBackgroundColor(Color.TRANSPARENT)
        lineChart.invalidate()  // 차트 업데이트
    }

    companion object {
        fun newInstance(data: List<Pair<Float, Float>>): GraphFragment {
            val fragment = GraphFragment()
            fragment.data = data
            return fragment
        }
    }
}