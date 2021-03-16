package th.co.appman.myapartment.view.user.detail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.tvAlertMessage.movementMethod = ScrollingMovementMethod()
        binding.tvRuleMessage.movementMethod = ScrollingMovementMethod()
    }

    companion object {
        fun newInstance(): Fragment = UserDetailFragment()
    }
}