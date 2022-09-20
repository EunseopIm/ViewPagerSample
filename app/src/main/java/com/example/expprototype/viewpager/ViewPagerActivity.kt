package com.example.expprototype.viewpager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.ActivityViewPagerBinding
import java.lang.Math.abs


class ViewPagerActivity : AppCompatActivity() {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private var pagerAdapter: ViewPagerAdapter? = null

    private val binding: ActivityViewPagerBinding by lazy {
        ActivityViewPagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewPager()
    }

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->

        if (position == 0f || position == 1f || position == -1f
            || position == 0.5f || position == -0.5f) {
            Log.v(">>>", "@# Test---[${binding.viewPager.currentItem}] position($position)")
        }

        page.cameraDistance = 20000f
        page.cameraDistance = 10000f

        if (position < -1){     // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.alpha = 0f

        }
        else if (position <= 0){    // [-1,0]

            page.alpha = 1f
            page.pivotX = page.width.toFloat()
            page.rotationY = ((90 * abs(position)))
        }
        else if (position <= 1){    // (0,1]

            page.alpha = 1f
            page.pivotX = 0f
            page.rotationY = (-90 * abs(position))

        }
        else{    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0f
        }

        if (abs(position) <= 0.5){
            page.scaleY = Math.max(.4f,1- kotlin.math.abs(position));
        }
        else if (abs(position) <= 1){
            page.scaleY = Math.max(.4f, kotlin.math.abs(position));

        }
    }

    private fun initViewPager() {

        // 뷰페이저 초기화
        fragments.add(CategoryFragment.newInstance("", "", 0))
        fragments.add(CategoryFragment.newInstance("", "", 1))
        fragments.add(CategoryFragment.newInstance("", "", 2))
        fragments.add(CategoryFragment.newInstance("", "", 3))

        pagerAdapter = ViewPagerAdapter(this, fragments)
        with(binding) {

            viewPager.adapter = pagerAdapter
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            })
            viewPager.offscreenPageLimit = fragments.size
            //viewPager.requestDisallowInterceptTouchEvent(false)
            viewPager.isUserInputEnabled = false
            viewPager.setPageTransformer(pageTransformer)
        }
    }

    fun prevPage() {

        val position = binding.viewPager.currentItem
        val size = fragments.size
        if (position > 0) {
            binding.viewPager.setCurrentItem(position - 1, true)
        }
    }

    fun nextPage() {

        val position = binding.viewPager.currentItem
        val size = fragments.size
        if (position < size - 1) {
            binding.viewPager.setCurrentItem(position + 1, true)
        }
    }

    private var animFactor = 0
    private val animator = ValueAnimator()

    private fun animateViewPager(pager: ViewPager2, offset: Int, delay: Int) {
        if (!animator.isRunning) {
            animator.removeAllUpdateListeners()
            animator.removeAllListeners()
            //Set animation
            animator.setIntValues(0, -offset)
            animator.duration = delay.toLong()
            animator.repeatCount = 1
            animator.repeatMode = ValueAnimator.RESTART
            animator.addUpdateListener { animation ->
                val value = animFactor * animation.animatedValue as Int
                if (!pager.isFakeDragging) {
                    pager.beginFakeDrag()
                }
                pager.fakeDragBy(value.toFloat())
            }
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    animFactor = 1
                }

                override fun onAnimationEnd(animation: Animator) {
                    pager.endFakeDrag()
                }

                override fun onAnimationRepeat(animation: Animator) {
                    animFactor = -1
                }
            })
            animator.start()
        }
    }
}