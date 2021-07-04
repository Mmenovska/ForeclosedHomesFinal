package com.gsixacademy.android.foreclosedhomes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.gsixacademy.android.foreclosedhomes.MainActivity
import com.gsixacademy.android.foreclosedhomes.R
import com.gsixacademy.android.foreclosedhomes.adapters.CityAdapter
import com.gsixacademy.android.foreclosedhomes.data.PropertiesData
import com.gsixacademy.android.foreclosedhomes.data.PropertiesModel
import com.gsixacademy.android.foreclosedhomes.data.SpinnerModel
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {
    lateinit var cityList : ArrayList<SpinnerModel>
    lateinit var cityAdapter : CityAdapter

    lateinit var propertiesModel : PropertiesModel
    lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference

        propertiesModel = (activity as MainActivity).propertiesModel!!
        initialiseFirebaseDatabase()



        button_next.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("cityClicked",city_spinner.selectedItem.toString())
            findNavController().navigate(R.id.action_homeFragment_to_propertiesFragment,bundle)
        }


    }

    private fun initialiseFirebaseDatabase (){
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
//                propertiesModel = snapshot.getValue(PropertiesModel :: class.java)
                for (document in snapshot.children){
                    for (item in document.children ){
                        val data = item.getValue(PropertiesData::class.java)
                        propertiesModel?.imoti?.add(data!!)
                    }

                }
                var cities : HashSet<String> = HashSet()
                if (propertiesModel?.imoti != null)
                    for (item in propertiesModel?.imoti!!) {
                            cities.add(item.city.toString())
                    }

                val array = arrayOfNulls<String>(cities.size)
                cityAdapter = CityAdapter(requireActivity(),cities.toArray(array))
                city_spinner.adapter = cityAdapter
            }
        }

        database.addValueEventListener(postListener)
    }
}
