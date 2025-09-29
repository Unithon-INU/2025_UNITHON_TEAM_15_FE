package unithon.helpjob.data.analytics

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

class AndroidAnalyticsService : AnalyticsService {
    private val firebase = Firebase.analytics

    override fun logEvent(name: String, params: Map<String, Any>?) {
        if (params == null) {
            firebase.logEvent(name, null)
        } else {
            firebase.logEvent(name, Bundle().apply {
                params.forEach { (k, v) ->
                    when (v) {
                        is String -> putString(k, v)
                        is Int -> putInt(k, v)
                        is Boolean -> putBoolean(k, v)
                    }
                }
            })
        }
    }
}