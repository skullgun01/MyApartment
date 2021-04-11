package th.co.myapartment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.myapartment.base.BaseViewModel
import th.co.myapartment.base.Constants
import th.co.myapartment.model.PaymentEntity
import th.co.myapartment.model.RoomEntity
import th.co.myapartment.model.TenantEntity
import th.co.myapartment.room.ApartmentDao

class AdminPaymentViewModel(private val apartmentDao: ApartmentDao) : BaseViewModel() {

    val tenantLiveData: MutableLiveData<TenantEntity> = MutableLiveData()
    val updateTenantLiveData: MutableLiveData<Unit> = MutableLiveData()
    val paymentLiveData: MutableLiveData<PaymentEntity> = MutableLiveData()
    val paymentEmptyLiveData: MutableLiveData<Unit> = MutableLiveData()
    val sumOverdueLiveData: MutableLiveData<String> = MutableLiveData()
    val sumRoomPriceLiveData: MutableLiveData<String> = MutableLiveData()
    val savePaymentRoomLiveData: MutableLiveData<Unit> = MutableLiveData()
    val updateStatusLiveData: MutableLiveData<Unit> = MutableLiveData()
    val roomDetailLiveData: MutableLiveData<RoomEntity> = MutableLiveData()
    val clearTenantDataLiveData: MutableLiveData<Unit> = MutableLiveData()
    val addContractRoomLiveData: MutableLiveData<Unit> = MutableLiveData()

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
                val _overduePrice = overduePrice.toIntOrNull() ?: 0

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
                    this.rawPrice = (sumPrice.toInt() - _overduePrice).toString()

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
                val _overduePrice = overduePrice.toIntOrNull() ?: 0

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
                    sumPrice,
                    (sumPrice.toInt() - _overduePrice).toString()
                )
                savePaymentRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun updateOverdueRoom(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apartmentDao.updateOverdueStatus(roomNumber, true)
            } catch (e: Exception) {
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

    fun getRoomDetail(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getRoomDetail(roomNumber)
                roomDetailLiveData.postValue(result)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun deleteExitRoom(roomNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.updateStatusPaymentByRoom(roomNumber, true)
                apartmentDao.deleteTenant(roomNumber)
                apartmentDao.clearRoomData(roomNumber, "", false, "", false)
                clearTenantDataLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun addContractRoom(roomNumber: String, contractRoom: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                apartmentDao.addContractTenant(roomNumber, contractRoom)
                addContractRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }
}
