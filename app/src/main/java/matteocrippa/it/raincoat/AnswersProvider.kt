package matteocrippa.it.raincoat

import android.content.Context
import android.util.Log
import org.json.JSONObject

/**
 * Created by matteocrippa on 27/12/2017.
 */

class AnswersProvider(private val context: Context, private val projectToken: String) : ProviderType {

    override var className = Class.forName("com.mixpanel.android.mpmetrics.MixpanelAPI")
    override var classInstance = className.getDeclaredMethod("getInstance", Context::class.java, String::class.java)
    override var classFunction = className.getDeclaredMethod("track", String::class.java, JSONObject::class.java)

    init {
        classInstance?.let {
            // get context
            try {
                it.invoke(className, context, projectToken)
            } catch (e: Exception) {
                Log.e("Raincoat", e.localizedMessage)
            }
        }
    }

    override fun log(eventName: String, parameters: HashMap<String, Any>?) {
        try {
            classFunction.invoke(classInstance, eventName, JSONObject(parameters))
        } catch (e: Exception) {
            Log.e("Raincoat", e.localizedMessage)
        }
    }
}
