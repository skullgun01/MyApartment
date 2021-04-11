package th.co.myapartment.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import th.co.myapartment.room.ApartmentDatabase
import th.co.myapartment.viewmodel.*

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

    viewModel {
        val database: ApartmentDatabase = get()
        UserMenuViewModel(androidContext(), database.apartmentDao())
    }

    viewModel {
        val database: ApartmentDatabase = get()
        AdminMainViewModel(database.apartmentDao())
    }

    viewModel {
        val database: ApartmentDatabase = get()
        ListRoomViewModel(database.apartmentDao())
    }

    viewModel {
        val database: ApartmentDatabase = get()
        AdminPaymentViewModel(database.apartmentDao())
    }

    viewModel {
        val database: ApartmentDatabase = get()
        DashboardViewModel(androidContext(), database.apartmentDao())
    }
}