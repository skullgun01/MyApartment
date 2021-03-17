package th.co.appman.myapartment.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import th.co.appman.myapartment.databinding.ActivityLoginBinding
import th.co.appman.myapartment.view.user.menu.UserMenuActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, UserMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
