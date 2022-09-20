package com.example.expprototype.viewpager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.expprototype.databinding.FragmentCategoryBinding

private const val ARG_PARAM1 = "category"
private const val ARG_PARAM2 = "feature"
private const val ARG_PARAM3 = "count"

class CategoryFragment : Fragment() {

    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(param1: String, param2: String, param3: Int) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null
    private var param3: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getInt(ARG_PARAM3)
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
        if (param3 % 2 == 1) {
            binding.clRoot.setBackgroundColor(Color.parseColor("#ff0000"))
        } else {
            binding.clRoot.setBackgroundColor(Color.parseColor("#0000ff"))
        }

        // category title
        binding.tvCategory.text = param1?: ""

        // feature title
        binding.tvFeature.text = param2?: ""
    }
}