package th.co.appman.myapartment.view.admin.payment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.R
import th.co.appman.myapartment.alert.AlertExitRoomDialogFragment
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.databinding.ActivityAdminPaymentBinding
import th.co.appman.myapartment.model.PaymentEntity
import th.co.appman.myapartment.model.RoomEntity
import th.co.appman.myapartment.utils.randomNumber
import th.co.appman.myapartment.view.admin.room.ListRoomFragment
import th.co.appman.myapartment.viewmodel.AdminPaymentViewModel
import java.util.*

class AdminPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminPaymentBinding
    private val vm: AdminPaymentViewModel by viewModel()

    private var roomEntity = RoomEntity()

    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0

    private var updatePayment = false
    private var transectionNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            roomEntity = it.getParcelable(ListRoomFragment.KEY_ROOM_ENTITY) ?: RoomEntity()
        }

        observeData()
        init()
    }

    private fun observeData() {
        vm.tenantLiveData.observe(this, Observer {
            binding.tvName.setText(it.tenantName)
            binding.tvTel.setText(it.tenantTel)
        })

        vm.roomDetailLiveData.observe(this, Observer {
            if (it.roomOverdue) {
                binding.tvOverdue.visibility = View.VISIBLE
            } else {
                binding.tvOverdue.visibility = View.GONE
            }
        })

        vm.updateTenantLiveData.observe(this, Observer {
            binding.tvName.isEnabled = false
            binding.tvTel.isEnabled = false
            binding.btnEditDataTenant.isEnabled = true
            binding.btnSaveDataTenant.isEnabled = false
            alertDialog(getString(R.string.alert_save_success))
        })

        vm.paymentLiveData.observe(this, Observer {
            transectionNumber = it.transectionNumber

            binding.tvPriceWater.apply {
                isEnabled = false
                setText(it.waterPoint)
            }

            binding.tvSumWater.text = it.waterPrice

            binding.tvPriceElectric.apply {
                isEnabled = false
                setText(it.electricityPoint)
            }

            binding.tvSumElectric.text = it.electricityPrice

            binding.tvOverduePrice.text = it.overduePrice

            binding.btnSaveDataPrice.isEnabled = false

            binding.btnEditDataPrice.isEnabled = !it.paymentStatus

            vm.sumPriceRoom(
                binding.tvPriceRoom.text.toString().toIntOrNull() ?: 0,
                binding.tvSumWater.text.toString().toIntOrNull() ?: 0,
                binding.tvSumElectric.text.toString().toIntOrNull() ?: 0,
                binding.tvOverduePrice.text.toString().toIntOrNull() ?: 0
            )
        })

        vm.paymentEmptyLiveData.observe(this, Observer {
            binding.tvPriceWater.isEnabled = true
            binding.tvPriceElectric.isEnabled = true
            binding.btnSaveDataPrice.isEnabled = true
            binding.btnEditDataPrice.isEnabled = false

            vm.sumPriceRoom(
                binding.tvPriceRoom.text.toString().toIntOrNull() ?: 0,
                binding.tvSumWater.text.toString().toIntOrNull() ?: 0,
                binding.tvSumElectric.text.toString().toIntOrNull() ?: 0,
                binding.tvOverduePrice.text.toString().toIntOrNull() ?: 0
            )
        })

        vm.sumOverdueLiveData.observe(this, Observer {
            binding.tvOverduePrice.text = it
        })

        vm.sumRoomPriceLiveData.observe(this, Observer {
            binding.tvSum.text = it
        })

        vm.savePaymentRoomLiveData.observe(this, Observer {
            binding.tvPriceWater.isEnabled = false
            binding.tvPriceElectric.isEnabled = false
            binding.btnSaveDataPrice.isEnabled = false
            binding.btnEditDataPrice.isEnabled = true
            updatePayment = false
            vm.getRoomDetail(roomEntity.roomNumber)
            alertDialog(getString(R.string.alert_save_success))
        })

        vm.updateStatusLiveData.observe(this, Observer {
            if (updatePayment) {
                vm.updatePaymentPriceRoom(
                    transectionNumber,
                    binding.tvPriceWater.text.toString(),
                    binding.tvSumWater.text.toString(),
                    binding.tvPriceElectric.text.toString(),
                    binding.tvSumElectric.text.toString(),
                    binding.tvOverduePrice.text.toString(),
                    binding.etDataPay.text.toString(),
                    binding.tvName.text.toString(),
                    binding.tvSum.text.toString()
                )
            } else {
                transectionNumber = randomNumber().toString()
                vm.savePaymentPriceRoom(
                    transectionNumber,
                    roomEntity.roomNumber,
                    binding.tvPriceRoom.text.toString(),
                    binding.tvPriceWater.text.toString(),
                    binding.tvSumWater.text.toString(),
                    binding.tvPriceElectric.text.toString(),
                    binding.tvSumElectric.text.toString(),
                    binding.tvOverduePrice.text.toString(),
                    binding.etDataPay.text.toString(),
                    binding.tvName.text.toString(),
                    binding.tvSum.text.toString()
                )
            }
        })

        vm.clearTenantDataLiveData.observe(this, Observer {
            AlertMessageDialogFragment.Builder()
                .setMessage(getString(R.string.alert_delete_success))
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
            alertDialog(it)
        })
    }

    private fun init() {
        vm.getTenantData(roomEntity.roomNumber)
        vm.getRoomDetail(roomEntity.roomNumber)

        binding.tvRoomNumber.text = roomEntity.roomNumber

        if (roomEntity.roomOverdue) {
            binding.tvOverdue.visibility = View.VISIBLE
        }

        binding.btnEditDataTenant.setOnClickListener {
            binding.tvName.isEnabled = true
            binding.tvTel.isEnabled = true
            binding.btnEditDataTenant.isEnabled = false
            binding.btnSaveDataTenant.isEnabled = true
        }

        binding.btnSaveDataTenant.setOnClickListener {
            vm.updateTenantData(
                roomEntity.roomNumber,
                binding.tvName.text.toString(),
                binding.tvTel.text.toString()
            )
        }

        binding.etDataPay.setOnClickListener {
            val calendar = Calendar.getInstance()
            mDay = calendar.get(Calendar.DAY_OF_MONTH)
            mMonth = calendar.get(Calendar.MONTH)
            mYear = calendar.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = "$dayOfMonth/$month/$year"
                    binding.etDataPay.setText(date)

                    if (!updatePayment) {
                        binding.tvPriceWater.setText("")
                        binding.tvSumWater.text = "0"

                        binding.tvPriceElectric.setText("")
                        binding.tvSumElectric.text = "0"

                        binding.tvSum.text = ""

                        vm.getPaymentData(roomEntity.roomNumber, date)
                    }
                }, mYear, mMonth, mDay
            )

            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        binding.tvPriceWater.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvSumWater.text =
                    ((text.toString().toIntOrNull() ?: 0) * 20).toString()

                vm.sumPriceRoom(
                    binding.tvPriceRoom.text.toString().toIntOrNull() ?: 0,
                    binding.tvSumWater.text.toString().toIntOrNull() ?: 0,
                    binding.tvSumElectric.text.toString().toIntOrNull() ?: 0,
                    binding.tvOverduePrice.text.toString().toIntOrNull() ?: 0
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tvPriceElectric.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvSumElectric.text =
                    ((text.toString().toIntOrNull() ?: 0) * 5).toString()

                vm.sumPriceRoom(
                    binding.tvPriceRoom.text.toString().toIntOrNull() ?: 0,
                    binding.tvSumWater.text.toString().toIntOrNull() ?: 0,
                    binding.tvSumElectric.text.toString().toIntOrNull() ?: 0,
                    binding.tvOverduePrice.text.toString().toIntOrNull() ?: 0
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSaveDataPrice.setOnClickListener {
            binding.tvPriceWater.clearFocus()
            binding.tvPriceElectric.clearFocus()

            if (binding.tvOverduePrice.text.toString().isNotEmpty()) {
                vm.updateOverdueRoom(roomEntity.roomNumber)
            }

            vm.updateStatusPayment(roomEntity.roomNumber)
        }

        binding.btnEditDataPrice.setOnClickListener {
            binding.tvPriceWater.isEnabled = true
            binding.tvPriceElectric.isEnabled = true
            binding.btnEditDataPrice.isEnabled = false
            binding.btnSaveDataPrice.isEnabled = true

            updatePayment = true
        }

        binding.btnCancelContact.setOnClickListener {
            AlertExitRoomDialogFragment.Builder()
                .setMessage(getString(R.string.message_exit_room, roomEntity.roomNumber))
                .setCallback {
                    vm.deleteExitRoom(roomEntity.roomNumber)
                }
                .build()
                .show(supportFragmentManager, TAG)
        }

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }

        binding.btnCancelContact.isEnabled = roomEntity.roomExitDate.isNotEmpty()
    }

    private fun alertDialog(message: String) {
        AlertMessageDialogFragment.Builder()
            .setMessage(message)
            .build()
            .show(supportFragmentManager, TAG)
    }

    companion object {
        private const val TAG = "AdminPaymentActivity"
    }
}
