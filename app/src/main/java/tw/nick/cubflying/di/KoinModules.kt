package tw.nick.cubflying.di

import android.app.Service.LAYOUT_INFLATER_SERVICE
import android.app.Service.WINDOW_SERVICE
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import tw.nick.cubflying.BuildConfig
import tw.nick.cubflying.api.CurrencyApiService
import tw.nick.cubflying.api.FlyingApiService
import tw.nick.cubflying.api.OkHttpBuilder
import tw.nick.cubflying.api.RetrofitBuilder
import tw.nick.cubflying.ui.flying.FlyingViewModel
import tw.nick.cubflying.viewmodel.MainViewModel

object KoinModules {

    fun initKoin(applicationContext: Context) {
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    apiServiceModule,
                    viewModelModule,
                    systemModule
                )
            )
        }
    }

    /**
     *  For retrofit service
     */
    private val apiServiceModule = module {
        single { OkHttpBuilder().build() }
        single { RetrofitBuilder(BuildConfig.BASE_URL).build() }
        single { get<Retrofit>().create(FlyingApiService::class.java) }
        single {
            RetrofitBuilder(BuildConfig.CURRENCY_URL).build().create(CurrencyApiService::class.java)
        }
    }


    private val viewModelModule = module {
        viewModel { MainViewModel(get(), get()) }
        viewModel { FlyingViewModel(get()) }
    }

    private val systemModule = module {
        factory { androidContext().getSystemService(WINDOW_SERVICE) as WindowManager }
        factory { androidContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater }
    }


}