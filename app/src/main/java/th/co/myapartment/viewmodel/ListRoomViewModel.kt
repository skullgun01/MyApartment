package th.co.myapartment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.myapartment.base.BaseViewModel
import th.co.myapartment.model.RoomEntity
import th.co.myapartment.room.ApartmentDao

class ListRoomViewModel(private val apartmentDao: ApartmentDao) : BaseViewModel() {

    private lateinit var roomEntity: MutableList<RoomEntity>

    val allRoomLiveData: MutableLiveData<Unit> = MutableLiveData()
    val statusRoomLiveData: MutableLiveData<Triple<String, String, String>> = MutableLiveData()
    val amountFloorLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    val roomByFloorLiveData: MutableLiveData<MutableList<RoomEntity>> = MutableLiveData()
    val updateStatusRoomLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun getAllRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getAllRoom()
                roomEntity = result
                allRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun callUpdateStatusRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                val result = apartmentDao.getAllRoom()
                roomEntity = result
                updateStatusRoomLiveData.postValue(Unit)
                loadingLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun getAmountFloor() {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            val resultRoom = roomEntity.distinctBy { it.roomFloor }.toMutableList()
            val resultFloor = mutableListOf<String>()
            resultRoom.forEach {
                resultFloor.add(it.roomFloor)
            }

            loadingLiveData.postValue(false)
            amountFloorLiveData.postValue(resultFloor)
        }
    }

    fun getStatusRoomByFloor(floor: String) {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            val resultRoom = roomEntity.filter { it.roomFloor == floor }.toMutableList()
            val resultEmptyRoom = resultRoom.count { !it.roomStatus }.toString()
            val resultOverdue = resultRoom.count { it.roomOverdue }.toString()
            val resultExitStatus = resultRoom.count { it.roomExitDate.isNotEmpty() }.toString()

            statusRoomLiveData.postValue(Triple(resultEmptyRoom, resultOverdue, resultExitStatus))
            roomByFloorLiveData.postValue(resultRoom)
            loadingLiveData.postValue(false)
        }
    }
}
