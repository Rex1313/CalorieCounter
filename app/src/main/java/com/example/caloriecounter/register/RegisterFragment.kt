package com.example.caloriecounter.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.caloriecounter.R
import com.example.caloriecounter.base.ResourceProvider
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {


    lateinit var viewModel: RegisterViewModel
    lateinit var navHost: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        navHost = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        super.onViewCreated(view, savedInstanceState)


        button_register.setOnClickListener {
            viewModel.register(
                textview_name.text.toString(),
                textview_password.text.toString(),
                textview_password.text.toString()
            )
        }
        observeState()
    }

    fun observeState() {
        viewModel.uiState.observe(this, Observer {
            onResumeState()
            when (it.state) {
                RegisterUIState.State.ERROR -> onError(
                    it.errorMessage ?: ResourceProvider.getString(R.string.error_unknown)
                )
                RegisterUIState.State.WAIT -> onWait()
                RegisterUIState.State.SUCCESS -> {
                    Toast.makeText(
                        activity,
                        ResourceProvider.getString(R.string.user_created_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                    navHost.navController.navigate(R.id.daysFragment)
                }
            }
        })
    }
    fun onWait() {
        progress.visibility = View.VISIBLE
    }

    fun onResumeState() {
        progress.visibility = View.GONE
    }

    fun onError(message: String) {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
    }


    companion object {

        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}
