package th.co.myapartment.view.admin.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.myapartment.R
import th.co.myapartment.alert.AlertMessageDialogFragment
import th.co.myapartment.databinding.FragmentAdminMainBinding
import th.co.myapartment.viewmodel.AdminMainViewModel

class AdminMainFragment : Fragment() {

    lateinit var binding: FragmentAdminMainBinding

    private val vm: AdminMainViewModel by viewModel()

    private var addressNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        init()
    }

    private fun init() {
        vm.getAnnounceAndRule()

        binding.btnSaveAnnounce.setOnClickListener {
            vm.callSaveAnnounce(addressNumber, binding.etAnnounceMessage.text.toString())
        }

        binding.btnSaveRule.setOnClickListener {
            vm.callSaveRule(addressNumber, binding.etRuleMessage.text.toString())
        }
    }

    private fun observeData() {
        vm.apartmentDetailLiveData.observe(viewLifecycleOwner, Observer {
            binding.etAnnounceMessage.setText(it.announceMessage)
            binding.etRuleMessage.setText(it.ruleMessage)
            addressNumber = it.addressNumber
        })

        vm.saveAnnounceLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                alertDialog(getString(R.string.alert_save_success))
            } else {
                alertDialog(getString(R.string.alert_save_fail))
            }
        })

        vm.saveRuleLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                alertDialog(getString(R.string.alert_save_success))
            } else {
                alertDialog(getString(R.string.alert_save_fail))
            }
        })

        vm.loadingLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        vm.errorLiveData.observe(viewLifecycleOwner, Observer {
            alertDialog(it)
        })
    }

    private fun alertDialog(message: String) {
        AlertMessageDialogFragment.Builder()
            .setMessage(message)
            .build()
            .show(childFragmentManager, TAG)
    }

    companion object {
        private const val TAG = "AdminMainFragment"

        fun newInstance(): Fragment = AdminMainFragment()
    }
}