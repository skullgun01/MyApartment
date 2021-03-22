package th.co.appman.myapartment.view.user.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.R
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.databinding.ActivityMyRoomBinding
import th.co.appman.myapartment.view.user.menu.UserMenuActivity
import th.co.appman.myapartment.viewmodel.UserMenuViewModel

class MyRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyRoomBinding

    private val vm: UserMenuViewModel by viewModel()

    private var roomNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            roomNumber = it.getString(Constants.KEY_USER, "")
        }

        observeData()
        init()
    }

    private fun observeData() {
        vm.tenantDetailLiveData.observe(this, Observer {
            binding.tvName.text = it.tenantName
        })

        vm.apartmentDetailLiveData.observe(this, Observer {
            binding.tvAddress.text = it.addressDetail
            binding.tvTel.text = it.addressTel
        })

        vm.loadingLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        vm.errorLiveData.observe(this, Observer {
            AlertMessageDialogFragment.Builder()
                .setMessage(it)
                .build()
                .show(supportFragmentManager, TAG)
        })
    }

    private fun init() {
        binding.tvRoomNumber.text = roomNumber

        vm.getDetailApartment()
        vm.getDetailTenant(roomNumber)

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val TAG = "MyRoomActivity"
    }
}