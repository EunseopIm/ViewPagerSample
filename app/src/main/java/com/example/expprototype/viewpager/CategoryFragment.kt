package com.example.expprototype.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.FragmentCategoryBinding

private const val ARG_PARAM1 = "category"
private const val ARG_PARAM2 = "feature"
private const val ARG_PARAM3 = "count"

class CategoryFragment : Fragment() {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private var pagerAdapter: ViewPagerAdapter? = null

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

        initViewPager()
    }

    private fun initViewPager() {

        if (activity == null) return

        if (param3 == 0) {
            fragments.add(FeatureFragment.newInstance("category1", "feature1", 0))
            fragments.add(FeatureFragment.newInstance("category1", "feature2", 1))
            fragments.add(FeatureFragment.newInstance("category1", "feature3", 2))
            fragments.add(FeatureFragment.newInstance("category1", "feature4", 3))
        } else if (param3 == 1) {
            fragments.add(FeatureFragment.newInstance("category2", "feature5", 4))
            fragments.add(FeatureFragment.newInstance("category2", "feature6", 5))
        } else if (param3 == 2) {
            fragments.add(FeatureFragment.newInstance("category3", "feature7", 6))
        } else if (param3 == 3) {
            fragments.add(FeatureFragment.newInstance("category4", "feature8", 7))
            fragments.add(FeatureFragment.newInstance("category4", "feature9", 8))
            fragments.add(FeatureFragment.newInstance("category4", "feature10", 9))
        }

        pagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, fragments)
        with(binding) {

            viewPager.adapter = pagerAdapter
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            })
            viewPager.offscreenPageLimit = fragments.size
            viewPager.requestDisallowInterceptTouchEvent(true)
        }
    }
}