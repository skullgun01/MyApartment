package th.co.myapartment.view.user.detail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import th.co.myapartment.R
import th.co.myapartment.databinding.FragmentUserDetailBinding
import th.co.myapartment.viewmodel.UserMenuViewModel

class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentUserDetailBinding

    private val vm: UserMenuViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        init()
    }

    private fun init() {
        binding.tvAlertMessage.movementMethod = ScrollingMovementMethod()
        binding.tvRuleMessage.movementMethod = ScrollingMovementMethod()
    }

    private fun observeData() {
        vm.apartmentDetailLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvAlertMessage.text = it.announceMessage
            binding.tvRuleMessage.text = it.ruleMessage
        })
    }

    companion object {
        fun newInstance(): Fragment = UserDetailFragment()
    }
}
