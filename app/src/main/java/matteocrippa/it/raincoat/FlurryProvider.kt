package matteocrippa.it.raincoat

import android.content.Context
import android.util.Log

/**
 * Created by matteocrippa on 27/12/2017.
 */
class FlurryProvider(private val context: Context, private val apiKey: String) : ProviderType {

    override var className = Class.forName("com.flurry.android.FlurryAgent")
    override var classInstance = className.getDeclaredMethod("build", Context::class.java, String::class.java)
    override var classFunction = className.getDeclaredMethod("logEvent", String::class.java, HashMap::class.java)

    init {

        val builder = className.getDeclaredMethod("Builder")
        try {
            builder.invoke(className)
        } catch (e: Exception) {
            Log.e("Raincoat", e.localizedMessage)
        }
        try {
            classInstance.invoke(builder, context, apiKey)
        } catch (e: Exception) {
            Log.e("Raincoat", e.localizedMessage)
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
