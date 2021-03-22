package th.co.appman.myapartment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import th.co.appman.myapartment.model.AddressEntity
import th.co.appman.myapartment.model.RoomEntity
import th.co.appman.myapartment.model.TenantEntity
import th.co.appman.myapartment.model.UserEntity

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
    suspend fun loginCheckStatusRoom(roomNumber: String): RoomEntity
}