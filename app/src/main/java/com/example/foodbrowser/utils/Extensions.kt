package com.example.foodbrowser.utils

import android.os.CountDownTimer
import androidx.appcompat.widget.SearchView

/**
 * Extension function to simplify setting an afterTextChanged action to SearchView components.
 */
 fun SearchView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        var timer: CountDownTimer? = null

        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            timer?.cancel()
            timer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Noncompliant - method is empty}
                }

                override fun onFinish() {
                    afterTextChanged.invoke(newText)
                }
            }.start()
            return false
        }
    })
}