package th.co.appman.myapartment.view.user.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.FragmentUserMainBinding

class UserMainFragment : Fragment() {

    lateinit var binding: FragmentUserMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserMainBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance(): Fragment = UserMainFragment()
    }
}