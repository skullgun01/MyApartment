package th.co.appman.myapartment.view.user.exit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.ActivityExitRoomBinding

class ExitRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExitRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExitRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.layoutBack.setOnClickListener {
            finish()
        }
    }
}