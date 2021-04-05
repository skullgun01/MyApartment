package th.co.appman.myapartment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.appman.myapartment.base.BaseViewModel
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.model.PaymentEntity
import th.co.appman.myapartment.model.TenantEntity
import th.co.appman.myapartment.room.ApartmentDao

class AdminPaymentViewModel(private val apartmentDao: ApartmentDao) : BaseViewModel() {

    val tenantLiveData: MutableLiveData<TenantEntity> = MutableLiveData()
    val updateTenantLiveData: MutableLiveData<Unit> = MutableLiveData()
    val paymentLiveData: MutableLiveData<PaymentEntity> = MutableLiveData()
    val paymentEmptyLiveData: MutableLiveData<Unit> = MutableLiveData()
    val sumOverdueLiveData: MutableLiveData<String> = MutableLiveData()
    val sumRoomPriceLiveData: MutableLiveData<String> = MutableLiveData()
    val savePaymentRoomLiveData: MutableLiveData<Unit> = MutableLiveData()
    val updateStatusLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun getTenantData(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getTenantDetail(roomNumber)
                result.tenantName?.let {
                    tenantLiveData.postValue(result)
                } ?: errorLiveData.postValue(Constants.APPLICATION_ERROR)

                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun updateTenantData(roomNumber: String, tenantName: String, tenantTel: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.updateTenantDetail(roomNumber, tenantName, tenantTel)
                updateTenantLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun getPaymentData(roomNumber: String, paymentDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getPaymentDataByRoom(roomNumber, paymentDate)
                result.paymentDate?.let {
                    paymentLiveData.postValue(result)
                } ?: errorLiveData.postValue(Constants.APPLICATION_ERROR)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                getPaymentOverdue(roomNumber)
            }
        }
    }

    private fun getPaymentOverdue(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getPaymentOverdueByRoom(roomNumber, false)
                sumOverdueLiveData.postValue(result.sumPrice)
                paymentEmptyLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                paymentEmptyLiveData.postValue(Unit)
            }
        }
    }

    fun sumPriceRoom(
        roomPrice: Int,
        waterPrice: Int,
        electricPrice: Int,
        overduePrice: Int
    ) {
        viewModelScope.launch {
            val sumRoomPrice = roomPrice + waterPrice + electricPrice + overduePrice
            sumRoomPriceLiveData.postValue(sumRoomPrice.toString())
        }
    }

    fun savePaymentPriceRoom(
        transectionNumber: String,
        roomNumber: String,
        roomPrice: String,
        waterPoint: String,
        waterPrice: String,
        electricityPoint: String,
        electricityPrice: String,
        overduePrice: String,
        paymentDate: String,
        tenantName: String,
        sumPrice: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val paymentEntity = PaymentEntity().apply {
                    this.transectionNumber = transectionNumber
                    this.roomNumber = roomNumber
                    this.roomPrice = roomPrice
                    this.waterPoint = waterPoint
                    this.waterPrice = waterPrice
                    this.electricityPoint = electricityPoint
                    this.electricityPrice = electricityPrice
                    this.overduePrice = overduePrice
                    this.paymentDate = paymentDate
                    this.tenantName = tenantName
                    this.paymentStatus = false
                    this.sumPrice = sumPrice
                }

                apartmentDao.insertSavePaymentRoom(paymentEntity)
                savePaymentRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun updatePaymentPriceRoom(
        transectionNumber: String,
        waterPoint: String,
        waterPrice: String,
        electricityPoint: String,
        electricityPrice: String,
        overduePrice: String,
        paymentDate: String,
        tenantName: String,
        sumPrice: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.updatePaymentRoom(
                    transectionNumber,
                    waterPoint,
                    waterPrice,
                    electricityPoint,
                    electricityPrice,
                    overduePrice,
                    paymentDate,
                    tenantName,
                    false,
                    sumPrice
                )
                savePaymentRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun updateStatusPayment(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.updateStatusPaymentByRoom(roomNumber, true)
                updateStatusLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }
}
