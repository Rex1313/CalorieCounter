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
import com.example.caloriecounter.dayview.DayFragment
import kotlinx.android.synthetic.main.splash_screen_fragment.*
import androidx.core.view.ViewCompat.setScaleX
import android.R.attr.y
import android.R.attr.x
import android.graphics.SurfaceTexture
import org.robolectric.shadows.ShadowDisplay.getDefaultDisplay
import android.util.DisplayMetrics




class SplashScreenFragment : Fragment() {


    lateinit var viewmodel:SplashScreenFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProviders.of(this).get(SplashScreenFragmentViewModel::class.java)
        context?.let {
//            if (viewmodel.getWelcomeScreenViewed(it)) {
//                findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
//            }else{
//                viewmodel.setWelcomeScreenViewed(it)
//            }
        }
       return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
//
//        video_view_welcome.setVideoURI(Uri.parse("android.resource://"+ (activity as AppCompatActivity).getPackageName() + "/raw/berry2"))
//        video_view_welcome.start()

        val dayFragment = DaysFragment.newInstance()
        button_welcome.setOnClickListener {
            findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
        }

    }
    video_view_welcome.setSurfaceTextureListener((){

    })
//    video_view_welcome.setSurfaceTextureListener(new SurfaceTextureListener() {
//        @Override
//        public void onSurfaceTextureAvailable(
//            SurfaceTexture surface,
//            int width, int height) {
//            FullScreenActivity.this.mSurface = surface;
//
//        }
//private fun scaleVideo(mPlayer: MediaPlayer) {
//
//    val videoParams = mTextureView
//        .getLayoutParams() as LayoutParams
//    val dm = DisplayMetrics()
//    this@FullScreenActivity.getWindowManager().getDefaultDisplay()
//        .getMetrics(dm)
//
//    val height = dm.heightPixels
//    val width = dm.widthPixels
//    val videoHeight = mPlayer.videoHeight
//    val videoWidth = mPlayer.videoWidth
//    var hRatio = 1.0
//
//    hRatio = height * 1.0 / videoHeight / (width * 1.0 / videoWidth)
//    videoParams.x = (if (hRatio <= 1)
//        0
//    else
//        Math.round(-(hRatio - 1) / 2 * width)).toInt()
//    videoParams.y = (if (hRatio >= 1)
//        0
//    else
//        Math
//            .round((-1 / hRatio + 1) / 2 * height)).toInt()
//    videoParams.width = width - videoParams.x - videoParams.x
//    videoParams.height = height - videoParams.y - videoParams.y
//    mTextureView.setScaleX(1.00001f)//<-- this line enables smoothing of the picture in TextureView.
//    mTextureView.requestLayout()
//    mTextureView.invalidate()
//
//}
    companion object {
        fun newInstance() = SplashScreenFragment()
    }
}
