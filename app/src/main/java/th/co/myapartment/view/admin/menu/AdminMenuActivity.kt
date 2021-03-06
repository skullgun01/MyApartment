package th.co.myapartment.view.admin.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import th.co.myapartment.databinding.ActivityAdminMenuBinding
import th.co.myapartment.view.admin.dashboard.DashboardFragment
import th.co.myapartment.view.admin.main.AdminMainFragment
import th.co.myapartment.view.admin.room.ListRoomFragment
import th.co.myapartment.view.login.LoginActivity

class AdminMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        replaceFragment(AdminMainFragment.newInstance())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(AdminMainFragment.newInstance())
                    }
                    1 -> {
                        replaceFragment(ListRoomFragment.newInstance())
                    }
                    2 -> {
                        replaceFragment(DashboardFragment.newInstance())
                    }
                    3 -> {
                        val intent = Intent(this@AdminMenuActivity, LoginActivity::class.java)
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
}
