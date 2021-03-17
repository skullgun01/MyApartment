package th.co.appman.myapartment.view.user.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.ActivityMyPaymentBinding

class MyPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }

        binding.tvTitleWaterPrice.text = getString(R.string.label_title_water_price, "20")
        binding.tvTitleElectricPrice.text = getString(R.string.label_title_electric_price, "20")
    }
}