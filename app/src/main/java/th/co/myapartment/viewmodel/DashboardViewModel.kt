package th.co.myapartment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import th.co.myapartment.R
import th.co.myapartment.base.BaseViewModel
import th.co.myapartment.base.Constants
import th.co.myapartment.model.PaymentEntity
import th.co.myapartment.room.ApartmentDao

class DashboardViewModel(private val mContext: Context, private val apartmentDao: ApartmentDao) :
    BaseViewModel() {

    private val datePaymentList: MutableList<String> = mutableListOf()

    val spinnerDateLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    val resultPaymentLiveData: MutableLiveData<Triple<Int, Int, Int>> = MutableLiveData()

    fun getAllPayment() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getAllPayment()
                if (result.any()) {
                    getMonth(result)
                }
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    private fun getMonth(paymentEntity: MutableList<PaymentEntity>) {
        viewModelScope.launch {
            val arrayMonth = mContext.resources.getStringArray(R.array.month)

            val resultDatePayment = paymentEntity.distinctBy { it.paymentDate }.toMutableList()
            resultDatePayment.forEach {
                val splitDate = it.paymentDate.split("/")

                val resultDate = "${arrayMonth[splitDate[1].toInt() - 1]} ${splitDate[2]}"
                datePaymentList.add(resultDate)
            }

            spinnerDateLiveData.postValue(datePaymentList)
            loadingLiveData.postValue(false)
        }
    }

    fun getDataPaymentByDate(dateString: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadingLiveData.postValue(true)

                val arrayMonth = mContext.resources.getStringArray(R.array.month)
                val dateSplit = dateString.split(" ")

                var monthInt = ""
                arrayMonth.forEachIndexed { index, month ->
                    if (month == dateSplit[0]) {
                        monthInt = (index + 1).toString()
                        return@forEachIndexed
                    }
                }

                val dateResult = "$monthInt/${dateSplit[1]}"

                try {
                    val result = apartmentDao.getPaymentDataByDate(dateResult)

                    var resultIncome = 0
                    var resultWaterExpenses = 0
                    var resultElectricExpenses = 0

                    result.forEach {
                        resultIncome += it.rawPrice.toInt()
                        resultWaterExpenses += it.waterPrice.toInt()
                        resultElectricExpenses += it.electricityPrice.toInt()
                    }

                    resultPaymentLiveData.postValue(
                        Triple(
                            resultIncome,
                            resultWaterExpenses,
                            resultElectricExpenses
                        )
                    )

                    loadingLiveData.postValue(false)
                } catch (e: Exception) {
                    loadingLiveData.postValue(false)
                    errorLiveData.postValue(Constants.APPLICATION_ERROR)
                }
            }
        }
    }
}
