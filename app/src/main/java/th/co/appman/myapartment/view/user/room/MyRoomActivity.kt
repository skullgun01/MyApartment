package th.co.appman.myapartment.view.user.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import th.co.appman.myapartment.R
import th.co.appman.myapartment.databinding.ActivityMyRoomBinding

class MyRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}