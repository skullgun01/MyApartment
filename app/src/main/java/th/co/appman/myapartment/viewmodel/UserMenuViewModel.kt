package th.co.appman.myapartment.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.appman.myapartment.base.BaseViewModel
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.model.AddressEntity
import th.co.appman.myapartment.model.PaymentEntity
import th.co.appman.myapartment.model.TenantEntity
import th.co.appman.myapartment.room.ApartmentDao

class UserMenuViewModel(private val mContext: Context, private val apartmentDao: ApartmentDao) :
    BaseViewModel() {

    val apartmentDetailLiveData: MutableLiveData<AddressEntity> = MutableLiveData()
    val tenantDetailLiveData: MutableLiveData<TenantEntity> = MutableLiveData()
    val paymentDetailLiveData: MutableLiveData<PaymentEntity> = MutableLiveData()

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
                paymentDetailLiveData.postValue(result)

                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }
}
