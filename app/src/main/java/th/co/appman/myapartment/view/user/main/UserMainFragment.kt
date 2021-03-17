package th.co.appman.myapartment.view.user.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.co.appman.myapartment.databinding.FragmentUserMainBinding
import th.co.appman.myapartment.view.user.payment.MyPaymentActivity
import th.co.appman.myapartment.view.user.room.MyRoomActivity

class UserMainFragment : Fragment() {

    lateinit var binding: FragmentUserMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.btnPayment.setOnClickListener {
            openNewPage(MyPaymentActivity::class.java)
        }

        binding.btnMyRoom.setOnClickListener {
            openNewPage(MyRoomActivity::class.java)
        }
    }

    private fun openNewPage(page: Class<*>) {
        val intent = Intent(requireContext(), page)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): Fragment = UserMainFragment()
    }
}