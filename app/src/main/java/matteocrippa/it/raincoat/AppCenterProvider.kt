package matteocrippa.it.raincoat

import android.content.Context
import android.util.Log

/**
 * Created by matteocrippa on 27/12/2017.
 */
class AppCenterProvider(private val context: Context, private val apiKey: String) : ProviderType {

    private var classAnalytics = Class.forName("com.microsoft.appcenter.analytics.Analytics")


    override var className = Class.forName("com.microsoft.appcenter.AppCenter")
    override var classInstance = className.getDeclaredMethod("start", Context::class.java, String::class.java, classAnalytics::class.java)
    override var classFunction = className.getDeclaredMethod("trackEvent", String::class.java, HashMap::class.java)

    init {
        classInstance?.let {
            // get context
            try {
                it.invoke(className, context, apiKey, classAnalytics)
            } catch (e: Exception) {
                Log.e("Raincoat", e.localizedMessage)
            }
        }
    }

    override fun log(eventName: String, parameters: HashMap<String, Any>?) {
        try {
            classFunction.invoke(classInstance, eventName, parameters)
        } catch (e: Exception) {
            Log.e("Raincoat", e.localizedMessage)
        }
    }
}