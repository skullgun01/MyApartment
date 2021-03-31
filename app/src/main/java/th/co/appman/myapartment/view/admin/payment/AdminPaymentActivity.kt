package th.co.appman.myapartment.view.admin.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.ActivityAdminPaymentBinding

class AdminPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
