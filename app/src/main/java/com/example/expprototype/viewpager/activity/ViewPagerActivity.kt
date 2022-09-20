package com.example.expprototype.viewpager.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.ActivityViewPagerBinding
import com.example.expprototype.viewpager.fragment.CategoryFragment
import com.example.expprototype.viewpager.transform.CubeOutTransformer
import com.example.expprototype.viewpager.util.ViewPagerAdapter


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
            viewPager.isUserInputEnabled = false
            viewPager.setPageTransformer(CubeOutTransformer())
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