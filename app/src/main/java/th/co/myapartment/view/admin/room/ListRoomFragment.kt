package th.co.myapartment.view.admin.room

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.myapartment.R
import th.co.myapartment.alert.AlertMessageDialogFragment
import th.co.myapartment.databinding.FragmentListRoomBinding
import th.co.myapartment.model.RoomEntity
import th.co.myapartment.view.admin.main.AdminMainFragment
import th.co.myapartment.view.admin.payment.AdminPaymentActivity
import th.co.myapartment.view.login.LoginActivity
import th.co.myapartment.view.user.payment.MyPaymentActivity
import th.co.myapartment.viewmodel.ListRoomViewModel

class ListRoomFragment : Fragment(), ListRoomAdapter.OnClickListener {

    lateinit var binding: FragmentListRoomBinding
    lateinit var listRoomAdapter: ListRoomAdapter
    private val vm: ListRoomViewModel by viewModel()
    private var page = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRoomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setAdapter()
        init()
    }

    private fun init() {
        vm.getAllRoom()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                page = tab?.text.toString()
                vm.getStatusRoomByFloor(page)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setAdapter() {
        listRoomAdapter = ListRoomAdapter()
        listRoomAdapter.setListener(this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = listRoomAdapter
        }
    }

    private fun observeData() {
        vm.allRoomLiveData.observe(viewLifecycleOwner, Observer {
            vm.getAmountFloor()
        })

        vm.statusRoomLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvEmptyRoom.text = getString(R.string.label_empty_status_room, it.first)
            binding.tvOverdue.text = getString(R.string.label_overdue_status, it.second)
            binding.tvExitStatus.text = getString(R.string.label_exit_status, it.third)
        })

        vm.amountFloorLiveData.observe(viewLifecycleOwner, Observer { floorData ->
            floorData.forEach {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it))
            }
        })

        vm.roomByFloorLiveData.observe(viewLifecycleOwner, Observer {
            listRoomAdapter.setListData(it)
        })

        vm.updateStatusRoomLiveData.observe(viewLifecycleOwner, Observer {
            vm.getStatusRoomByFloor(page)
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

    companion object {
        private const val TAG = "ListRoomFragment"
        const val KEY_ROOM_ENTITY = "KEY_ROOM_ENTITY"

        fun newInstance(): Fragment = ListRoomFragment()
    }

    override fun openDetailRoom(roomEntity: RoomEntity) {
        val intent = Intent(requireContext(), AdminPaymentActivity::class.java).apply {
            putExtra(KEY_ROOM_ENTITY, roomEntity)
        }
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            vm.callUpdateStatusRoom()
        }
    }
}
