package th.co.appman.myapartment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import th.co.appman.myapartment.model.AddressEntity
import th.co.appman.myapartment.model.RoomEntity
import th.co.appman.myapartment.model.TenantEntity
import th.co.appman.myapartment.model.UserEntity

@Database(
    entities = [UserEntity::class, AddressEntity::class, RoomEntity::class, TenantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApartmentDatabase : RoomDatabase() {

    abstract fun apartmentDao(): ApartmentDao

    companion object {
        const val DB_NAME = "Apartment.db"
    }
}