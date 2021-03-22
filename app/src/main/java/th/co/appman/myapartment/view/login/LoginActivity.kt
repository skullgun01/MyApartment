package th.co.appman.myapartment.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.databinding.ActivityLoginBinding
import th.co.appman.myapartment.view.user.menu.UserMenuActivity
import th.co.appman.myapartment.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    private val vm: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
        initData()
        init()
    }

    private fun observeData() {
        vm.checkLoginLiveData.observe(this, Observer {
            if (it == "user") {
                val intent = Intent(this, UserMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
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

    private fun initData() {
        val userData = resources.assets.open("mockUser.json").bufferedReader().readText()
        vm.insertUserData(userData)

        val addressData = resources.assets.open("mockAddress.json").bufferedReader().readText()
        vm.insertAddressData(addressData)

        val roomData = resources.assets.open("mockRoom.json").bufferedReader().readText()
        vm.insertRoomData(roomData)

        val tenantData = resources.assets.open("mockTenant.json").bufferedReader().readText()
        vm.insertTenantData(tenantData)
    }

    private fun init() {
        binding.btnLogin.setOnClickListener {
            vm.checkLogin(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
