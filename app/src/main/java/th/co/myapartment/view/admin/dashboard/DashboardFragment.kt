package th.co.myapartment.view.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.koin.androidx.viewmodel.ext.android.viewModel
import th.co.myapartment.R
import th.co.myapartment.alert.AlertMessageDialogFragment
import th.co.myapartment.databinding.FragmentDashboardBinding
import th.co.myapartment.view.admin.dashboard.adapter.SpinnerAdapter
import th.co.myapartment.viewmodel.DashboardViewModel
import java.text.DecimalFormat


class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    private val vm: DashboardViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        init()
    }

    private fun observeData() {
        vm.spinnerDateLiveData.observe(viewLifecycleOwner, Observer {
            binding.spinner.adapter = SpinnerAdapter(it)
        })

        vm.resultPaymentLiveData.observe(viewLifecycleOwner, Observer {
            val (resultIncome, resultWaterExpenses, resultElectricExpenses) = it
            val formatter = DecimalFormat("#,###,###")

            val dataList: MutableList<PieEntry> = mutableListOf()
            dataList.apply {
                add(
                    PieEntry(
                        resultIncome.toFloat(),
                        getString(R.string.label_income_pie, formatter.format(resultIncome).toString())
                    )
                )
                add(
                    PieEntry(
                        resultWaterExpenses.toFloat(),
                        getString(R.string.label_water_expenses_pie, formatter.format(resultWaterExpenses).toString())
                    )
                )
                add(
                    PieEntry(
                        resultElectricExpenses.toFloat(),
                        getString(
                            R.string.label_electric_expenses_pie,
                            formatter.format(resultElectricExpenses).toString()
                        )
                    )
                )
            }

            val pieDataSet = PieDataSet(dataList, null).apply {
                selectionShift = 10F
                valueTextColor = 14
                setColors(MATERIAL_COLORS)
            }

            binding.picChart.data = PieData(pieDataSet)
            binding.picChart.notifyDataSetChanged()
            binding.picChart.invalidate()

            binding.tvSumIncome.text =
                getString(R.string.label_sum_income, formatter.format(resultIncome).toString())
            binding.tvSumWater.text =
                getString(
                    R.string.label_sum_water_expenses,
                    formatter.format(resultWaterExpenses).toString()
                )
            binding.tvSumElectric.text =
                getString(
                    R.string.label_sum_electric_expenses,
                    formatter.format(resultElectricExpenses).toString()
                )
            binding.tvNetIncome.text = getString(
                R.string.label_net_income,
                formatter.format(((resultIncome - resultWaterExpenses) - resultElectricExpenses))
                    .toString()
            )
        })

        vm.loadingLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        vm.errorLiveData.observe(viewLifecycleOwner, Observer {
            alertDialog(it)
        })
    }

    private fun init() {
        vm.getAllPayment()

        binding.tvSumIncome.text = getString(R.string.label_sum_income, "0")
        binding.tvSumWater.text = getString(R.string.label_sum_water_expenses, "0")
        binding.tvSumElectric.text = getString(R.string.label_sum_electric_expenses, "0")
        binding.tvNetIncome.text = getString(R.string.label_net_income, "0")

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.spinner.selectedItem.toString()
                vm.getDataPaymentByDate(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.picChart.apply {
            holeRadius = 30F
            transparentCircleRadius = 40F
            animateY(1000)
            description.isEnabled = false
            legend.isEnabled = false
            setEntryLabelTextSize(16F)
            setEntryLabelColor(R.color.black)
        }
    }

    private fun alertDialog(message: String) {
        AlertMessageDialogFragment.Builder()
            .setMessage(message)
            .build()
            .show(childFragmentManager, TAG)
    }

    companion object {
        private const val TAG = "DashboardFragment"

        private val MATERIAL_COLORS = mutableListOf(
            ColorTemplate.rgb("#2ecc71"),
            ColorTemplate.rgb("#f1c40f"),
            ColorTemplate.rgb("#e74c3c"),
            ColorTemplate.rgb("#3498db")
        )

        private val BLACK_COLORS = mutableListOf(
            ColorTemplate.rgb("#000000"),
        )

        fun newInstance(): Fragment = DashboardFragment()
    }
}