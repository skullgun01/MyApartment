package th.co.myapartment.view.user.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.myapartment.R
import th.co.myapartment.alert.AlertMessageDialogFragment
import th.co.myapartment.base.Constants
import th.co.myapartment.databinding.FragmentUserMainBinding
import th.co.myapartment.view.user.exit.ExitRoomActivity
import th.co.myapartment.view.user.menu.UserMenuActivity
import th.co.myapartment.view.user.payment.MyPaymentActivity
import th.co.myapartment.view.user.room.MyRoomActivity
import th.co.myapartment.viewmodel.UserMenuViewModel

class UserMainFragment : Fragment() {

    lateinit var binding: FragmentUserMainBinding

    private val vm: UserMenuViewModel by sharedViewModel()

    private var roomNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            roomNumber = it.getString(Constants.KEY_USER, "")
        }

        observeData()
        init()
    }

    private fun observeData() {
        vm.apartmentDetailLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvTel.text = getString(R.string.label_address_tel, it.addressTel)
        })

        vm.loadingLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        vm.errorLiveData.observe(viewLifecycleOwner, Observer {
            AlertMessageDialogFragment.Builder()
                .setMessage(it)
                .build()
                .show(childFragmentManager, TAG)
        })
    }

    private fun init() {
        vm.getDetailApartment()

        binding.btnPayment.setOnClickListener {
            openNewPage(MyPaymentActivity::class.java)
        }

        binding.btnMyRoom.setOnClickListener {
            openNewPage(MyRoomActivity::class.java)
        }

        binding.btnAlertExit.setOnClickListener {
            openNewPage(ExitRoomActivity::class.java)
        }
    }

    private fun openNewPage(page: Class<*>) {
        val intent = Intent(requireContext(), page).apply {
            putExtra(Constants.KEY_USER, roomNumber)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "UserMainFragment"

        fun newInstance(roomNumber: String): Fragment {
            val fragment = UserMainFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_USER, roomNumber)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}