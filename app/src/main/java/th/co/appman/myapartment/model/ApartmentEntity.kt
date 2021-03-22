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
    val roomFloor: String? = "",

    @SerializedName("tenantName")
    val tenantName: String? = "",

    @SerializedName("roomStatus")
    val roomStatus: Boolean = false,

    @SerializedName("roomExitDate")
    val roomExitDate: String? = "",

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
    @PrimaryKey(autoGenerate = true)
    @SerializedName("transectionNumber")
    val transectionNumber: Int = 0,

    @SerializedName("roomNumber")
    val roomNumber: String = "",

    @SerializedName("roomPrice")
    val roomPrice: String? = "",

    @SerializedName("waterPoint")
    val waterPoint: String? = "",

    @SerializedName("waterPrice")
    val waterPrice: String? = "",

    @SerializedName("electricityPoint")
    val electricityPoint: String? = "",

    @SerializedName("electricityPrice")
    val electricityPrice: String? = "",

    @SerializedName("overduePrice")
    val overduePrice: String? = "",

    @SerializedName("paymentDate")
    val paymentDate: String? = "",

    @SerializedName("tenantName")
    val tenantName: String? = "",

    @SerializedName("paymentStatus")
    val paymentStatus: Boolean = false
)
