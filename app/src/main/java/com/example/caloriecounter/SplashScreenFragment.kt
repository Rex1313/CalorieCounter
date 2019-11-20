package com.example.caloriecounter

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.splash_screen_fragment.*
import com.example.caloriecounter.utils.VIDEO_HEIGHT
import com.example.caloriecounter.utils.VIDEO_URL
import com.example.caloriecounter.utils.VIDEO_WIDTH
import com.example.caloriecounter.utils.getScreenHeight


class SplashScreenFragment : Fragment() {


    lateinit var viewmodel: SplashScreenFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProviders.of(this).get(SplashScreenFragmentViewModel::class.java)
        context?.let {
            if (viewmodel.getWelcomeScreenViewed(it)) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
            } else {
                viewmodel.setWelcomeScreenViewed(it)
            }
        }
        return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        playVideo()

        val dayFragment = DaysFragment.newInstance()
        button_welcome.setOnClickListener {
            findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
        }

    }

    private fun playVideo() {
        val url = Uri.parse(VIDEO_URL)
        video_view_welcome.setVideoURI(url)
        val params = video_view_welcome.layoutParams
        params.width = (getScreenHeight() * VIDEO_WIDTH / VIDEO_HEIGHT).toInt()
        video_view_welcome.layoutParams = params
        video_view_welcome.start()
    }

    companion object {
        fun newInstance() = SplashScreenFragment()
    }
}
