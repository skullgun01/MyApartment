package th.co.appman.myapartment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import th.co.appman.myapartment.model.*

@Dao
interface ApartmentDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUserData(userEntity: UserEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAddressData(addressEntity: AddressEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertRoomData(roomEntity: RoomEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertTenantData(tenantEntity: TenantEntity)

    @Query("SELECT * FROM User WHERE roomNumber = :username AND password = :password")
    suspend fun checkLogin(username: String, password: String): UserEntity

    @Query("SELECT * FROM Room WHERE roomNumber = :roomNumber")
    suspend fun getRoomDetail(roomNumber: String): RoomEntity

    @Query("SELECT * FROM Address")
    suspend fun getApartmentDetail(): AddressEntity

    @Query("SELECT * FROM Tenant WHERE roomNumber = :roomNumber")
    suspend fun getTenantDetail(roomNumber: String): TenantEntity

    @Insert
    suspend fun insertPayment(paymentEntity: PaymentEntity)

    @Query("SELECT * FROM Payment WHERE roomNumber = :roomNumber AND paymentStatus = :paymentStatus")
    suspend fun getPaymentDetail(roomNumber: String, paymentStatus: Boolean): PaymentEntity

    @Query("UPDATE Room SET roomExitDate = :exitDate WHERE roomNumber = :roomNumber")
    suspend fun updateExitDate(roomNumber: String, exitDate: String)

    @Query("UPDATE Address SET announceMessage = :announceMessage WHERE addressNumber = :addressNumber")
    suspend fun saveAnnounceMessage(addressNumber: String, announceMessage: String)

    @Query("UPDATE Address SET ruleMessage = :ruleMessage WHERE addressNumber = :addressNumber")
    suspend fun saveRuleMessage(addressNumber: String, ruleMessage: String)

    @Query("SELECT * FROM Room")
    suspend fun getAllRoom(): MutableList<RoomEntity>

    @Query("UPDATE Tenant SET tenantName = :tenantName, tenantTel = :tenantTel WHERE roomNumber = :roomNumber")
    suspend fun updateTenantDetail(
        roomNumber: String,
        tenantName: String,
        tenantTel: String
    )

    @Query("SELECT * FROM Payment WHERE roomNumber = :roomNumber AND paymentDate = :paymentDate")
    suspend fun getPaymentDataByRoom(
        roomNumber: String,
        paymentDate: String
    ): PaymentEntity

    @Query("SELECT * FROM Payment WHERE roomNumber = :roomNumber AND paymentStatus = :paymentStatus")
    suspend fun getPaymentOverdueByRoom(
        roomNumber: String,
        paymentStatus: Boolean
    ): PaymentEntity

    @Insert
    suspend fun insertSavePaymentRoom(paymentEntity: PaymentEntity)

    @Query("UPDATE Payment SET waterPoint = :waterPoint , waterPrice = :waterPrice , electricityPoint = :electricityPoint , electricityPrice = :electricityPrice , overduePrice = :overduePrice , paymentDate = :paymentDate , tenantName = :tenantName , paymentStatus = :paymentStatus , sumPrice = :sumPrice WHERE transectionNumber = :transectionNumber")
    suspend fun updatePaymentRoom(
        transectionNumber: String,
        waterPoint: String,
        waterPrice: String,
        electricityPoint: String,
        electricityPrice: String,
        overduePrice: String,
        paymentDate: String,
        tenantName: String,
        paymentStatus: Boolean,
        sumPrice: String
    )

    @Query("UPDATE Payment SET paymentStatus = :paymentStatus WHERE roomNumber = :roomNumber")
    suspend fun updateStatusPaymentByRoom(
        roomNumber: String,
        paymentStatus: Boolean
    )
}