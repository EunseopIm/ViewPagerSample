package com.example.expprototype.viewpager.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.FragmentCategoryBinding
import com.example.expprototype.viewpager.activity.ViewPagerActivity
import com.example.expprototype.viewpager.util.DragListener
import com.example.expprototype.viewpager.util.ViewPagerAdapter

private const val ARG_PARAM1 = "category"
private const val ARG_PARAM2 = "color"

class CategoryFragment : Fragment() {

    private var paramCategory: Int = 0          // 카테고리
    private var paramColorCode: String? = null  // 컬러코드

    private val fragments: ArrayList<Fragment> = ArrayList()
    private var pagerAdapter: ViewPagerAdapter? = null

    /**
     * DragListener
     */
    private var oldX = 0f
    private var posX = 0f
    private var diffPosX = 0f
    var dragListener: DragListener? = null

    var isLastPosition = false
    var isFirstPosition = false

    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(param1: Int, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramCategory = it.getInt(ARG_PARAM1)
            paramColorCode = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initViewPager()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {

        if (activity == null) return

        // Feature 생성
        val random = (1 .. 5).random()
        for (i in 0..random) {
            fragments.add(FeatureFragment.newInstance(paramCategory, i, paramColorCode?: "#ffffff"))
        }

        pagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, fragments)
        with(binding) {

            viewPager.adapter = pagerAdapter
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (fragments.size > 0) {

                        isFirstPosition = position == 0
                        isLastPosition = position == fragments.size - 1
                    }
                }
            })
            viewPager.offscreenPageLimit = fragments.size

            /*indicator.visibleDotCount = 11
            indicator.dotColor = Color.parseColor("#99eeeeee")
            indicator.selectedDotColor = Color.parseColor("#ffffff")
            indicator.attachToPager(viewPager)*/

            dragListener = object : DragListener {

                override fun onLeft() {

                    activity?.let {
                        if (it is ViewPagerActivity) {
                            it.nextPage()
                        }
                    }
                }

                override fun onRight() {

                    activity?.let {
                        if (it is ViewPagerActivity) {
                            it.prevPage()
                        }
                    }
                }
            }

            viewPager.getChildAt(0).setOnTouchListener { view, event ->

                when (event.action) {

                    MotionEvent.ACTION_DOWN -> oldX = event.rawX

                    MotionEvent.ACTION_MOVE -> {

                        posX = event.rawX
                        diffPosX = posX - oldX
                        //return@setOnTouchListener true
                    }

                    MotionEvent.ACTION_UP ->
                        // Right (Hide)
                        if (diffPosX > 0) {
                            if (isFirstPosition) {
                                dragListener?.onRight()
                                return@setOnTouchListener true
                            }
                        } else {
                            if (isLastPosition) {
                                dragListener?.onLeft()
                                return@setOnTouchListener true
                            }
                        }
                }

                return@setOnTouchListener false
            }
        }
    }
}