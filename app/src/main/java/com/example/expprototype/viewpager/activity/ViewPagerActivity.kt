package com.example.expprototype.viewpager.activity

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.ActivityViewPagerBinding
import com.example.expprototype.viewpager.fragment.CategoryFragment
import com.example.expprototype.viewpager.transform.CubeOutTransformer
import com.example.expprototype.viewpager.util.ViewPagerAdapter
import com.example.expprototype.viewpager.util.setCurrentItemExtension


class ViewPagerActivity : AppCompatActivity() {

    private val transformDuration :Long = 2000L // cube - transform duration
    private var isTransforming = false

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
        fragments.add(CategoryFragment.newInstance(0, "#000000"))
        fragments.add(CategoryFragment.newInstance(1, "#ff3333"))
        fragments.add(CategoryFragment.newInstance(2, "#1fbc00"))
        fragments.add(CategoryFragment.newInstance(3, "#ffe600"))
        fragments.add(CategoryFragment.newInstance(4, "#bb86fc"))

        pagerAdapter = ViewPagerAdapter(this, fragments)
        with(binding) {

            vpOuter.adapter = pagerAdapter
            vpOuter.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            vpOuter.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    isTransforming = false
                    setChildPageUserInput(true)
                }
            })
            vpOuter.offscreenPageLimit = fragments.size
            vpOuter.isUserInputEnabled = false
            vpOuter.setPageTransformer(CubeOutTransformer())
        }
    }

    /**
     * 이전 페이지 (Cube)
     */
    fun prevPage() {

        if (isTransforming) return

        val position = binding.vpOuter.currentItem
        if (position > 0) {

            isTransforming = true
            setChildPageUserInput(false)

            binding.vpOuter.setCurrentItemExtension(
                position - 1,
                transformDuration,
                AccelerateDecelerateInterpolator(),
                binding.vpOuter.width,
                binding.vpOuter.height)

            //binding.vpOuter.setCurrentItem(position - 1, true)
        }
    }

    /**
     * 다음 페이지 (Cube)
     */
    fun nextPage() {

        if (isTransforming) return

        val position = binding.vpOuter.currentItem
        val size = fragments.size
        if (position < size - 1) {

            isTransforming = true
            setChildPageUserInput(false)

            binding.vpOuter.setCurrentItemExtension(
                position + 1,
                transformDuration,
                AccelerateDecelerateInterpolator(),
                binding.vpOuter.width,
                binding.vpOuter.height)
            //binding.vpOuter.setCurrentItem(position + 1, true)
        }
    }

    /**
     * ChildViewPager - isUserInputEnabled 설정
     */
    private fun setChildPageUserInput(isEnabled: Boolean) {

        for (fragment in fragments) {

            if (fragment is CategoryFragment) {
                fragment.setChildPageUserInput(isEnabled)
            }
        }
    }
}