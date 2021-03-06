package th.co.myapartment.view.admin.payment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.myapartment.R
import th.co.myapartment.alert.AlertExitRoomDialogFragment
import th.co.myapartment.alert.AlertMessageDialogFragment
import th.co.myapartment.databinding.ActivityAdminPaymentBinding
import th.co.myapartment.model.RoomEntity
import th.co.myapartment.utils.randomNumber
import th.co.myapartment.view.admin.room.ListRoomFragment
import th.co.myapartment.viewmodel.AdminPaymentViewModel
import java.util.*

class AdminPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminPaymentBinding
    private val vm: AdminPaymentViewModel by viewModel()

    private var roomEntity = RoomEntity()

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

            if (it.paymentStatus) {
                binding.btnConfirmPayment.isEnabled = false

                binding.tvPriceWater.apply {
                    isEnabled = false
                    setText("")
                }

                binding.tvSumWater.text = ""

                binding.tvPriceElectric.apply {
                    isEnabled = false
                    setText("")
                }

                binding.tvSumElectric.text = ""

                binding.tvOverduePrice.text = ""

                binding.btnSaveDataPrice.isEnabled = false

                binding.btnEditDataPrice.isEnabled = false
            } else {
                binding.btnConfirmPayment.isEnabled = true

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
            }
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
            binding.btnConfirmPayment.isEnabled = true
            binding.tvOverduePrice.text = it
        })

        vm.sumRoomPriceLiveData.observe(this, Observer {
            binding.tvSum.text = it
        })

        vm.savePaymentRoomLiveData.observe(this, Observer {
            binding.btnConfirmPayment.isEnabled = true
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

        vm.addContractRoomLiveData.observe(this, Observer {
            alertDialog(getString(R.string.alert_contract_room_success))
        })

        vm.confirmPaymentLiveData.observe(this, Observer {
            alertDialog(getString(R.string.btn_confirm_payment))

            binding.btnConfirmPayment.isEnabled = false

            binding.tvPriceWater.apply {
                isEnabled = false
                setText("")
            }

            binding.tvSumWater.text = ""

            binding.tvPriceElectric.apply {
                isEnabled = false
                setText("")
            }

            binding.tvSumElectric.text = ""

            binding.tvOverduePrice.text = ""

            binding.btnSaveDataPrice.isEnabled = false
            binding.btnEditDataPrice.isEnabled = false

            initPaymentDate()
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

        initPaymentDate()

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

        binding.btnUploadContact.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_GET_CONTENT
                                    type = "application/pdf"
                                }
                                startActivityForResult(
                                        Intent.createChooser(intent, "Select photo from"),
                                        REQUEST_GALLERY
                                )
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>,
                                token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
        }

        binding.btnConfirmPayment.setOnClickListener {
            vm.callConfirmPayment(roomEntity.roomNumber)
        }

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }

        binding.btnCancelContact.isEnabled = roomEntity.roomExitDate.isNotEmpty()
    }

    private fun initPaymentDate() {
        val calendar = Calendar.getInstance()
        mMonth = calendar.get(Calendar.MONTH)
        mYear = calendar.get(Calendar.YEAR)

        val paymentData = "1/${mMonth + 1}/$mYear"
        binding.etDataPay.setText(paymentData)
        vm.getPaymentData(roomEntity.roomNumber, paymentData)
    }

    private fun alertDialog(message: String) {
        AlertMessageDialogFragment.Builder()
                .setMessage(message)
                .build()
                .show(supportFragmentManager, TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            data?.let {
                val uriPdf = it.data

                try {
                    val pdf = uriPdf?.let { it1 -> contentResolver.openInputStream(it1) }
                    val base64 = Base64.encodeToString(pdf?.readBytes(), Base64.DEFAULT)
                    vm.addContractRoom(roomEntity.roomNumber, base64)
                } catch (e: Exception) {
                    alertDialog(getString(R.string.alert_contract_room_fail))
                }
            } ?: alertDialog(getString(R.string.alert_contract_room_fail))
        }
    }

    companion object {
        private const val REQUEST_GALLERY = 101
        private const val TAG = "AdminPaymentActivity"
    }
}
