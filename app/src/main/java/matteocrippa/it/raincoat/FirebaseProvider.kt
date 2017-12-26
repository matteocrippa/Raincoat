package matteocrippa.it.raincoat.firebaseprovider

import android.content.Context
import android.os.Bundle
import android.util.Log
import matteocrippa.it.raincoat.ProviderType

/**
 * Created by matteocrippa on 26/12/2017.
 */
class FirebaseProvider(private val context: Context) : ProviderType {

    override var className = Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
    override var classInstance = className.getDeclaredMethod("getInstance", Context::class.java)
    override var classFunction = className.getDeclaredMethod("logEvent", String::class.java, Bundle::class.java)

    init {
        classInstance?.let {
            // get context
            try {
                it.invoke(className, context)
            } catch (e: Exception) {
                Log.e("Raincoat", e.localizedMessage)
            }
        }
    }

    override fun log(eventName: String, parameters: HashMap<String, Any>?) {
        val bundle = Bundle()
        parameters?.forEach { pair ->
            when (pair.value) {
                is Int -> {
                    bundle.putInt(pair.key, pair.value as Int)
                }
                is Float -> {
                    bundle.putFloat(pair.key, pair.value as Float)
                }
                is Double -> {
                    bundle.putDouble(pair.key, pair.value as Double)
                }
                is String -> {
                    bundle.putString(pair.key, pair.value as String)
                }
            }
        }
        try {
            classFunction.invoke(classInstance, eventName, bundle)
        } catch (e: Exception) {
            Log.e("Raincoat", e.localizedMessage)
        }
    }
}
