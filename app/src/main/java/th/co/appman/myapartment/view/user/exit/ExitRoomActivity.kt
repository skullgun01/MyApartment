package th.co.appman.myapartment.view.user.exit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.R
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.databinding.ActivityExitRoomBinding
import th.co.appman.myapartment.model.ProductPriceModel
import th.co.appman.myapartment.utils.JsonHelper
import th.co.appman.myapartment.view.user.exit.adapter.ExitRoomAdapter
import th.co.appman.myapartment.viewmodel.UserMenuViewModel
import java.util.*

class ExitRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExitRoomBinding
    private val vm: UserMenuViewModel by viewModel()
    private lateinit var exitRoomAdapter: ExitRoomAdapter

    private var roomNumber = ""

    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExitRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            roomNumber = it.getString(Constants.KEY_USER, "")
        }

        observeData()
        setAdapter()
        init()
    }

    private fun observeData() {
        vm.updateExitRoomLiveData.observe(this, Observer {
            AlertMessageDialogFragment.Builder()
                .setMessage(getString(R.string.label_noti_exit_room))
                .setCallback {
                    finish()
                }
                .build()
                .show(supportFragmentManager, TAG)
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

        val resultMock = resources.assets.open("mockProductPrice.json").bufferedReader().readText()
        val rawData = JsonHelper.toType<MutableList<ProductPriceModel>>(resultMock)
        exitRoomAdapter.setItem(rawData)

        binding.etExitDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            mDay = calendar.get(Calendar.DAY_OF_MONTH)
            mMonth = calendar.get(Calendar.MONTH)
            mYear = calendar.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = "$dayOfMonth/$month/$year"
                    binding.etExitDate.setText(date)
                }, mYear, mMonth, mDay
            )

            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        binding.btnConfirmExit.setOnClickListener {
            if (binding.etExitDate.text.toString().trim().isNotEmpty()) {
                vm.updateExitDateRoom(roomNumber, binding.etExitDate.text.toString().trim())
            } else {
                AlertMessageDialogFragment.Builder()
                    .setMessage(getString(R.string.label_please_select_date))
                    .build()
                    .show(supportFragmentManager, TAG)
            }
        }

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }
    }

    private fun setAdapter() {
        exitRoomAdapter = ExitRoomAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = exitRoomAdapter
        }
    }

    companion object {
        private const val TAG = "ExitRoomActivity"
    }
}