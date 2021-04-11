package th.co.myapartment.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.myapartment.R
import th.co.myapartment.base.BaseViewModel
import th.co.myapartment.base.Constants
import th.co.myapartment.model.AddressEntity
import th.co.myapartment.model.PaymentEntity
import th.co.myapartment.model.TenantEntity
import th.co.myapartment.room.ApartmentDao

class UserMenuViewModel(private val mContext: Context, private val apartmentDao: ApartmentDao) :
    BaseViewModel() {

    val apartmentDetailLiveData: MutableLiveData<AddressEntity> = MutableLiveData()
    val tenantDetailLiveData: MutableLiveData<TenantEntity> = MutableLiveData()
    val paymentDetailLiveData: MutableLiveData<PaymentEntity> = MutableLiveData()
    val updateExitRoomLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun getDetailApartment() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getApartmentDetail()
                apartmentDetailLiveData.postValue(result)

                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun getDetailTenant(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getTenantDetail(roomNumber)
                tenantDetailLiveData.postValue(result)

                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun getPaymentRoom(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getPaymentDetail(roomNumber, false)
                result?.let {
                    paymentDetailLiveData.postValue(result)
                } ?: errorLiveData.postValue(mContext.getString(R.string.label_have_not_payment))

                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun updateExitDateRoom(roomNumber: String, exitDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.updateExitDate(roomNumber, exitDate)
                updateExitRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }
}
