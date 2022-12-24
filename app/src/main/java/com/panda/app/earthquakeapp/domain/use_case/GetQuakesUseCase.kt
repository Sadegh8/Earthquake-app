package com.panda.app.earthquakeapp.domain.use_case

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.panda.app.earthquakeapp.data.common.Resource
import com.panda.app.earthquakeapp.data.database.QuakeDatabase
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.domain.repository.QuakeRepository
import com.panda.app.earthquakeapp.utils.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetQuakesUseCase @Inject constructor(
    private val repository: QuakeRepository,
    private val quakeDatabase: QuakeDatabase,
    private val context: Application
) {
    operator fun invoke(): Flow<Resource<List<Quake>>> = flow {
        var quakes = repository.getQuakes()
        if (quakes.isEmpty()) {
            emit(Resource.Loading())
        }else {
            emit(Resource.Success(quakes))
        }
        try {
            val isNetworkConnected = context.checkNetwork
            if (Utils.shouldRefresh(quakes) && isNetworkConnected) {
                emit(Resource.Loading())
                repository.refreshQuakes()
                quakes = repository.getQuakes()
                if (quakes.isNotEmpty()) {
                    emit(Resource.Success(quakes))
                    quakeDatabase.dao.clearOldData(quakes[quakes.size - 1].time)
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error!"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private val Context.checkNetwork: Boolean
        get() {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            else
                @Suppress("DEPRECATION")
                manager.activeNetworkInfo?.isConnectedOrConnecting == true
        }


}