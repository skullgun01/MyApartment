package th.co.myapartment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import th.co.myapartment.model.*

@Database(
    entities = [UserEntity::class, AddressEntity::class, RoomEntity::class, TenantEntity::class, PaymentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApartmentDatabase : RoomDatabase() {

    abstract fun apartmentDao(): ApartmentDao

    companion object {
        const val DB_NAME = "Apartment.db"
    }
}