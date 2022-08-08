package com.panda.app.earthquakeapp.data.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.panda.app.earthquakeapp.data.common.Resource
import com.panda.app.earthquakeapp.data.database.DatabaseQuake
import com.panda.app.earthquakeapp.data.database.QuakeDatabase
import com.panda.app.earthquakeapp.data.database.toQuake
import com.panda.app.earthquakeapp.data.dto.QuakeDto
import com.panda.app.earthquakeapp.data.dto.toQuake
import com.panda.app.earthquakeapp.data.remote.QuakeApi
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.domain.repository.QuakeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class QuakeRepositoryImpl @Inject constructor(
    private val api: QuakeApi,
    private val database: QuakeDatabase
) : QuakeRepository {

    override suspend fun getQuakes(): List<Quake> {
        return  database.dao.getQuakes().map { it.toQuake() }

    }

    override suspend fun refreshQuakes() {
        withContext(Dispatchers.IO) {
            val quakeList = api.getQuakes()
            database.dao.insertAll(quakeList.features.map{ it.toQuake()})
        }
    }

}

