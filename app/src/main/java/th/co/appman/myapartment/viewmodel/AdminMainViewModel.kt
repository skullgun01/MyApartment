package th.co.appman.myapartment.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.appman.myapartment.base.BaseViewModel
import th.co.appman.myapartment.base.Constants
import th.co.appman.myapartment.model.AddressEntity
import th.co.appman.myapartment.room.ApartmentDao
import kotlin.Exception

class AdminMainViewModel(private val apartmentDao: ApartmentDao) :
    BaseViewModel() {

    val apartmentDetailLiveData: MutableLiveData<AddressEntity> = MutableLiveData()
    val saveAnnounceLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val saveRuleLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getAnnounceAndRule() {
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

    fun callSaveAnnounce(addressNumber: String, announceMessage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                loadingLiveData.postValue(false)
                val result = apartmentDao.saveAnnounceMessage(addressNumber, announceMessage)
                result?.let {
                    saveAnnounceLiveData.postValue(true)
                } ?: saveAnnounceLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }

    fun callSaveRule(addressNumber: String, ruleMessage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                loadingLiveData.postValue(false)
                val result = apartmentDao.saveRuleMessage(addressNumber, ruleMessage)
                result?.let {
                    saveRuleLiveData.postValue(true)
                } ?: saveRuleLiveData.postValue(false)
            } catch (e: Exception) {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(Constants.APPLICATION_ERROR)
            }
        }
    }
}
