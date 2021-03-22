package th.co.appman.myapartment.view.user.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.R
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.databinding.ActivityMyPaymentBinding
import th.co.appman.myapartment.model.PaymentEntity
import th.co.appman.myapartment.utils.randomNumber
import th.co.appman.myapartment.view.user.room.MyRoomActivity
import th.co.appman.myapartment.viewmodel.UserMenuViewModel

class MyPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPaymentBinding

    private val vm: UserMenuViewModel by viewModel()

    private var roomNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            roomNumber = it.getString(Constants.KEY_USER, "")
        }

        observeData()
        init()
    }

    private fun observeData() {
        vm.paymentDetailLiveData.observe(this, Observer {
            binding.tvDate.text = it.paymentDate
            binding.tvRoomPrice.text = it.roomPrice

            binding.tvTitleWaterPrice.text =
                getString(R.string.label_title_water_price, it.waterPoint)
            binding.tvWaterPrice.text = getString(R.string.label_price, it.waterPrice ?: 0)

            binding.tvTitleElectricPrice.text =
                getString(R.string.label_title_electric_price, it.electricityPoint)
            binding.tvElectricPrice.text = getString(R.string.label_price, it.electricityPrice ?: 0)

            binding.tvOverduePrice.text = getString(R.string.label_price, it.overduePrice ?: 0)
            binding.tvSumPrice.text = getString(R.string.label_price, it.sumPrice ?: 0)
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
                .setCallback {
                    finish()
                }
                .build()
                .show(supportFragmentManager, TAG)
        })
    }

    private fun init() {
        binding.tvRoomNumber.text = roomNumber

        vm.getPaymentRoom(roomNumber)

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val TAG = "MyPaymentActivity"
    }
}
