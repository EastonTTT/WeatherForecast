import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import com.myweather.android.logic.Repository
import com.myweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    // 假设你的 Repository.searchPlaces(query) 是挂起函数并直接返回 Place 的列表
    val placeLiveData = searchLiveData.switchMap { query ->
//        liveData(Dispatchers.IO) {
//            val places = Repository.searchPlaces(query)
//            emit(places)  // 发送数据到观察者
//        }
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}
