package th.co.appman.myapartment.view.user.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import th.co.appman.myapartment.databinding.ActivityUserMenuBinding
import th.co.appman.myapartment.view.login.LoginActivity
import th.co.appman.myapartment.view.user.detail.UserDetailFragment
import th.co.appman.myapartment.view.user.main.UserMainFragment

class UserMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        replaceFragment(UserDetailFragment.newInstance())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(UserDetailFragment.newInstance())
                    }
                    1 -> {
                        replaceFragment(UserMainFragment.newInstance())
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
}
