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
import com.example.expprototype.viewpager.util.TempListAdapter
import com.example.expprototype.viewpager.util.ViewPagerAdapter

private const val ARG_PARAM_CATEGORY = "category"
private const val ARG_PARAM_CHILD_COUNT = "childCount"
private const val ARG_PARAM_TOTAL_COUNT = "totalCount"
private const val ARG_PARAM_FIRST_CHILD_POSITION = "firstChildPosition"
private const val ARG_PARAM_COLOR = "color"

class CategoryFragment : Fragment() {

    // Arguments
    private var paramCategory: Int = 0              // 카테고리
    private var paramChildCount: Int = 0            // 현재 카테고리의 Feature 개수
    private var paramTotalCount: Int = 0            // 전체 Feature 개수
    private var paramFirstChildPosition: Int = 0    // 현재 카테고리 첫번째 Feature의 Position (전체 중)
    private var paramColorCode: String? = null      // 컬러코드

    // inner viewpager
    private val fragments: ArrayList<Fragment> = ArrayList()
    private var pagerAdapter: ViewPagerAdapter? = null

    // temp viewpager
    private lateinit var mAdapter: TempListAdapter
    private var mData: ArrayList<Int> = ArrayList()

    // DragListener
    private var oldX = 0f
    private var posX = 0f
    private var diffPosX = 0f
    var dragListener: DragListener? = null

    // flag
    var isFirstPosition = false         // 현재 카테고리 내에서 처음
    var isLastPosition = false          // 현재 카테고리 내에서 마지막
    var isFarLeft = false               // 전체 Feature 중 처음 (최좌측) - overscroll 버그방지
    var isFarRight = false              // 전체 Feature 중 마지막 (최우측)

    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(categoryNumber: Int, childCount: Int, totalCount: Int, firstChildPosition: Int, colorCode: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_CATEGORY, categoryNumber)
                    putInt(ARG_PARAM_CHILD_COUNT, childCount)
                    putInt(ARG_PARAM_TOTAL_COUNT, totalCount)
                    putInt(ARG_PARAM_FIRST_CHILD_POSITION, firstChildPosition)
                    putString(ARG_PARAM_COLOR, colorCode)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramCategory = it.getInt(ARG_PARAM_CATEGORY)
            paramChildCount = it.getInt(ARG_PARAM_CHILD_COUNT)
            paramTotalCount = it.getInt(ARG_PARAM_TOTAL_COUNT)
            paramFirstChildPosition = it.getInt(ARG_PARAM_FIRST_CHILD_POSITION)
            paramColorCode = it.getString(ARG_PARAM_COLOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initViewPager()
        initTempViewPager()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {

        if (activity == null) return

        // Feature 생성
        for (i in 0 until paramChildCount) {
            fragments.add(FeatureFragment.newInstance(paramCategory, i, paramColorCode?: "#ffffff"))
        }

        pagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, fragments)
        with(binding) {

            vpInner.adapter = pagerAdapter
            vpInner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            vpInner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    try {

                        // flag
                        isFirstPosition = position == 0
                        isLastPosition = position == fragments.size - 1
                        isFarLeft = paramFirstChildPosition + position == 0
                        isFarRight = paramFirstChildPosition + position == paramTotalCount - 1

                        // Indicator 관련 viewpager
                        vpTemp.currentItem = paramFirstChildPosition + position
                        tvFeatureNumber.text = "${paramFirstChildPosition + position + 1}"

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            })
            vpInner.offscreenPageLimit = fragments.size

            isFarLeft = paramFirstChildPosition == 0
            isFarRight = paramFirstChildPosition == paramTotalCount - 1
            tvFeatureNumber.text = "${paramFirstChildPosition + 1}"

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

            vpInner.getChildAt(0).setOnTouchListener { view, event ->

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

                            if (!isFarLeft && isFirstPosition) {
                                dragListener?.onRight()
                                return@setOnTouchListener true
                            }

                        } else {

                            if (!isFarRight && isLastPosition) {
                                dragListener?.onLeft()
                                return@setOnTouchListener true
                            }
                        }
                }

                return@setOnTouchListener false
            }
        }
    }

    private fun initTempViewPager() {

        activity?.let {

            with(binding) {

                for (i in 0 until paramTotalCount) {
                    mData.add(i)
                }

                mAdapter = TempListAdapter(it, mData)
                vpTemp.adapter = mAdapter
                vpTemp.isUserInputEnabled = false
                vpTemp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                vpTemp.currentItem = paramFirstChildPosition

                indicator.visibleDotCount = 11
                indicator.dotColor = Color.parseColor("#99eeeeee")
                indicator.selectedDotColor = Color.parseColor("#ffffff")
                indicator.attachToPager(vpTemp)
            }
        }
    }

    /**
     * ChildViewPager - isUserInputEnabled 설정
     */
    fun setChildPageUserInput(isEnabled: Boolean) {

        binding.vpInner.isUserInputEnabled = isEnabled
    }
}