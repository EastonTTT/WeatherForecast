package com.myweather.android.logic

import android.content.Context
import androidx.lifecycle.liveData
import com.myweather.android.logic.dao.PlaceDao
import com.myweather.android.logic.model.Place
import com.myweather.android.logic.model.Weather
import com.myweather.android.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO){
        val placeResponse = WeatherNetwork.searchPlaces(query)
        if(placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response statuus is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng:String,lat:String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async{
                WeatherNetwork.getRealtimeWeather(lng,lat)
            }
            val deferredDaily = async {
                WeatherNetwork.getDailyWeather(lng,lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if(realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                        "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }
    private fun <T> fire(context: CoroutineContext,block:suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try{
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}