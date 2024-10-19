package com.example.kelompok10_tickdone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.kelompok10_tickdone.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var skipButton: Button
    private lateinit var nextButton: Button
    private lateinit var getStartedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        skipButton = findViewById(R.id.btn_skip)
        nextButton = findViewById(R.id.btn_next)
        getStartedButton = findViewById(R.id.btn_get_started)

        val fragments = listOf(
            OnboardingFragment1(),
            OnboardingFragment2(),
            OnboardingFragment3()
        )

        val adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter

        skipButton.setOnClickListener {
            navigateToMain()
        }

        nextButton.setOnClickListener {
            val nextItem = viewPager.currentItem + 1
            if (nextItem < fragments.size) {
                viewPager.currentItem = nextItem
            } else {
                navigateToMain()
            }
        }

        getStartedButton.setOnClickListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, GetStarted::class.java))
        finish()
    }
}
