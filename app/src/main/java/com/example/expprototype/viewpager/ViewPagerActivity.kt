package com.example.expprototype.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.expprototype.databinding.ActivityViewPagerBinding

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
        fragments.add(CategoryFragment.newInstance("category1", "feature1", 0))
        fragments.add(CategoryFragment.newInstance("category1", "feature2", 1))
        fragments.add(CategoryFragment.newInstance("category2", "feature3", 2))
        fragments.add(CategoryFragment.newInstance("category2", "feature4", 3))
        fragments.add(CategoryFragment.newInstance("category3", "feature5", 4))
        fragments.add(CategoryFragment.newInstance("category4", "feature6", 5))

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
        }
    }
}