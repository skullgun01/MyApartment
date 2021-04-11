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
import th.co.myapartment.model.RoomEntity
import th.co.myapartment.model.TenantEntity
import th.co.myapartment.model.UserEntity
import th.co.myapartment.room.ApartmentDao
import th.co.myapartment.utils.JsonHelper

class LoginViewModel(private val mContext: Context, private val apartmentDao: ApartmentDao) :
    BaseViewModel() {

    val checkLoginLiveData: MutableLiveData<Pair<String, String>> = MutableLiveData()

    fun insertUserData(userData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val resultData = JsonHelper.toType<MutableList<UserEntity>>(userData)
                resultData.forEach {
                    apartmentDao.insertUserData(it)
                }
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun insertAddressData(addressData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val resultData = JsonHelper.toModel(addressData, AddressEntity::class.java)
                apartmentDao.insertAddressData(resultData)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun insertRoomData(roomData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val resultData = JsonHelper.toType<MutableList<RoomEntity>>(roomData)
                resultData.forEach {
                    apartmentDao.insertRoomData(it)
                }
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun insertTenantData(tenantData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val resultData = JsonHelper.toType<MutableList<TenantEntity>>(tenantData)
                resultData.forEach {
                    apartmentDao.insertTenantData(it)
                }
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun checkLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.checkLogin(username, password)

                if (result.roomNumber == username && result.password == password) {
                    result.role?.let { roleUser ->
                        if (roleUser == Constants.KEY_USER) {
                            val resultCheckStatusRoom = apartmentDao.getRoomDetail(username)
                            if (resultCheckStatusRoom.roomStatus) {
                                checkLoginLiveData.postValue(Pair(roleUser, username))
                            } else {
                                errorLiveData.postValue(mContext.getString(R.string.label_check_room_status))
                            }
                        } else {
                            checkLoginLiveData.postValue(Pair(roleUser, ""))
                        }
                    } ?: errorLiveData.postValue(Constants.APPLICATION_ERROR)
                } else {
                    errorLiveData.postValue(mContext.getString(R.string.label_check_login))
                }
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(mContext.getString(R.string.label_check_login))
            }
        }
    }
}
