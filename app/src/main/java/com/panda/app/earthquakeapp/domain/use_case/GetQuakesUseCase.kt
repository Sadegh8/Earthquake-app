package com.panda.app.earthquakeapp.domain.use_case

import android.app.Application
import android.content.Context
import android.util.Log
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
            if (Utils.shouldRefresh(quakes, context)) {
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

}