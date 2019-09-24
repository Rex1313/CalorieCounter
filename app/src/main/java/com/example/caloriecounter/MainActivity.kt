package com.example.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MainActivityViewModel()

            viewModel.getData()
            viewModel.dailySettingsLiveData.observe(this, Observer {
            dailySetting->
                text2.setText(dailySetting.caloriesLimit)

        })

        viewModel.entriesLiveData.observe(this, Observer {
//            val entryAdapter = EntryAdapter(it, context)
//            list.setAdapter(entryAdapter)
        })

    }
}
