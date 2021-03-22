package th.co.appman.myapartment.view.user.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.appman.myapartment.alert.AlertMessageDialogFragment
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.databinding.ActivityUserMenuBinding
import th.co.appman.myapartment.view.login.LoginActivity
import th.co.appman.myapartment.view.user.detail.UserDetailFragment
import th.co.appman.myapartment.view.user.main.UserMainFragment
import th.co.appman.myapartment.viewmodel.UserMenuViewModel

class UserMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserMenuBinding

    private val vm: UserMenuViewModel by viewModel()

    private var roomNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            roomNumber = it.getString(Constants.KEY_USER, "")
        }

        observeData()
        init()
    }

    private fun observeData() {
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
        vm.getDetailApartment()

        replaceFragment(UserDetailFragment.newInstance())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(UserDetailFragment.newInstance())
                    }
                    1 -> {
                        replaceFragment(UserMainFragment.newInstance(roomNumber))
                    }
                    2 -> {
                        val intent = Intent(this@UserMenuActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, fragment)
            .commit()
    }

    companion object {
        private const val TAG = "UserMenuActivity"
    }
}
