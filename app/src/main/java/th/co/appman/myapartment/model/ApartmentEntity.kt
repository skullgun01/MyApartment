package th.co.appman.myapartment.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("roomNumber")
    val roomNumber: String = "",

    @SerializedName("password")
    val password: String? = "",

    @SerializedName("role")
    val role: String? = ""
)

@Entity(tableName = "Address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("addressNumber")
    val addressNumber: String = "",

    @SerializedName("addressDetail")
    val addressDetail: String? = "",

    @SerializedName("addressTel")
    val addressTel: String? = "",

    @SerializedName("announceMessage")
    val announceMessage: String? = "",

    @SerializedName("ruleMessage")
    val ruleMessage: String? = ""
)

@Entity(tableName = "Room")
data class RoomEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("roomNumber")
    val roomNumber: String = "",

    @SerializedName("roomFloor")
    val roomFloor: String = "",

    @SerializedName("tenantName")
    val tenantName: String = "",

    @SerializedName("roomStatus")
    val roomStatus: Boolean = false,

    @SerializedName("roomExitDate")
    val roomExitDate: String = "",

    @SerializedName("roomOverdue")
    val roomOverdue: Boolean = false,

    @SerializedName("roomPrice")
    val roomPrice: String? = ""
)

@Entity(tableName = "Tenant")
data class TenantEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("roomNumber")
    val roomNumber: String = "",

    @SerializedName("tenantName")
    val tenantName: String? = "",

    @SerializedName("tenantIDCard")
    val tenantIDCard: String? = "",

    @SerializedName("tenantTel")
    val tenantTel: String? = "",

    @SerializedName("contractRoom")
    val contractRoom: String? = ""
)

@Entity(tableName = "Payment")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("transectionNumber")
    var transectionNumber: Int = 0,

    @SerializedName("roomNumber")
    var roomNumber: String = "",

    @SerializedName("roomPrice")
    var roomPrice: String? = "",

    @SerializedName("waterPoint")
    var waterPoint: String? = "",

    @SerializedName("waterPrice")
    var waterPrice: String? = "",

    @SerializedName("electricityPoint")
    var electricityPoint: String? = "",

    @SerializedName("electricityPrice")
    var electricityPrice: String? = "",

    @SerializedName("overduePrice")
    var overduePrice: String? = "",

    @SerializedName("paymentDate")
    var paymentDate: String? = "",

    @SerializedName("tenantName")
    var tenantName: String? = "",

    @SerializedName("paymentStatus")
    var paymentStatus: Boolean = false,

    @SerializedName("sumPrice")
    var sumPrice: String? = ""
)
