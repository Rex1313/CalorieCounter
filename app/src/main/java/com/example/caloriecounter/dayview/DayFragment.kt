package com.example.caloriecounter.dayview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounter.MainActivityViewModel
import com.example.caloriecounter.R
import kotlinx.android.synthetic.main.food_card.*
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DayFragment : Fragment() {

    lateinit var activityViewModel: MainActivityViewModel
    lateinit var fragmentViewModel: DayFragmentViewModel
    lateinit var date: String


    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel = ViewModelProviders.of(activity as FragmentActivity)
            .get(MainActivityViewModel::class.java)



        fragmentViewModel =
            ViewModelProviders.of(this).get(DayFragmentViewModel::class.java)

        fragmentViewModel.dayDate = date
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false)
    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {
            fragmentViewModel.getData()
        }


        fragmentViewModel.uiModelLiveData.observe(this, Observer { uiModel ->
            val myOverflowClicked: (view: View, id: Int?) -> Unit = { view: View, id: Int? ->
                val popupMenu = PopupMenu(context!!, view)
                popupMenu.menuInflater.inflate(R.menu.food_card_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_card_delete -> {
                            GlobalScope.launch {
                                fragmentViewModel.deleteEntryById(id)
                                fragmentViewModel.refreshData()
                            }
                            true
                        }
                        R.id.menu_card_edit -> {
                            //TODO code here for edit
                            true
                        }
                        else -> false
                    }
                }

                popupMenu.show()
            }


            // RecyclerView node initialized here
            recycler_view_food.apply {
                // set a LinearLayoutManager to handle Android
                // RecyclerView behavior
                layoutManager = LinearLayoutManager(activity)
                // set the custom adapter to the RecyclerView
                adapter = FoodRecycleListAdapter(uiModel.entries, myOverflowClicked)
            }

            text_view_day.text = uiModel.dateDescription
            text_view_calculation.text =
                uiModel.limit + '-' + uiModel.eatenCalories + '=' + uiModel.leftCalories
        })
        floating_action_button_add_meal.setOnClickListener {
            NewEntryDialogFragment.newInstance()
                .show(childFragmentManager, "NewEntry")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DayFragment {
            return DayFragment()
        }
    }

}
