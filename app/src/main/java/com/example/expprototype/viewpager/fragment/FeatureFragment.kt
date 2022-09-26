package com.example.expprototype.viewpager.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.expprototype.databinding.FragmentFeatrueBinding

private const val ARG_PARAM1 = "category"
private const val ARG_PARAM2 = "feature"
private const val ARG_PARAM3 = "color"

class FeatureFragment : Fragment() {

    private var paramCategory: Int = 0              // Category number
    private var paramFeature: Int = 0               // Feature number
    private var paramColorCode: String? = null      // Color Code

    private val binding: FragmentFeatrueBinding by lazy {
        FragmentFeatrueBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(param1: Int, param2: Int, param3: String) =
            FeatureFragment().apply {

                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramCategory = it.getInt(ARG_PARAM1)
            paramFeature = it.getInt(ARG_PARAM2)
            paramColorCode = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initView()

        return binding.root
    }

    private fun initView() {

        // background color
        val color = paramColorCode?: "#ffffff"
        binding.clRoot.setBackgroundColor(Color.parseColor(color))

        // category title
        binding.tvCategory.text = "Category ${paramCategory + 1}"

        // feature title
        binding.tvFeature.text = "Feature ${paramFeature + 1}"
    }
}