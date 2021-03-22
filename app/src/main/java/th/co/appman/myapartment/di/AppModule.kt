package th.co.appman.myapartment.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import th.co.appman.myapartment.room.ApartmentDatabase
import th.co.appman.myapartment.viewmodel.LoginViewModel

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), ApartmentDatabase::class.java, ApartmentDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val viewModelModule = module {
    viewModel {
        val database: ApartmentDatabase = get()
        LoginViewModel(androidContext(), database.apartmentDao())
    }
}