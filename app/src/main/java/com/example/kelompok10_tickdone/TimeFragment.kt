package com.example.kelompok10_tickdone

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TimeFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private lateinit var startFocusButton: Button
    private lateinit var circularProgressBar: CircularProgressBar
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_time, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        startFocusButton = view.findViewById(R.id.startFocusButton)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)

        startFocusButton.setOnClickListener {
            startTimer()
        }

        return view
    }

    private fun startTimer() {
        timer?.cancel() // Cancel any previous timer if active
        val focusDuration = 600 * 1000L // 25 seconds in milliseconds
        val totalDuration = focusDuration.toFloat()

        timer = object : CountDownTimer(focusDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60)

                // Update progress
                val progress = ((totalDuration - millisUntilFinished) / totalDuration) * 100
                circularProgressBar.setProgress(progress)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
                circularProgressBar.setProgress(100f)
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Clean up timer to avoid memory leaks
    }
}
