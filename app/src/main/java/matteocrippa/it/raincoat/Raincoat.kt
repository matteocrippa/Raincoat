package matteocrippa.it.raincoat

import java.lang.reflect.Method


/**
 * Created by matteocrippa on 26/12/2017.
 */


interface EventType {
    fun name(provider: ProviderType): String?
    fun parameters(provider: ProviderType): HashMap<String, Any>?
}

interface AnalyticsType<T> {
    fun register(provider: ProviderType)
    fun log(event: T)
}

interface ProviderType {
    var className: Class<*>
    var classInstance: Method?
    var classFunction: Method

    fun log(eventName: String, parameters: HashMap<String, Any>?)
}

class Raincoat<T : EventType> : AnalyticsType<T> {

    private var providers = arrayListOf<ProviderType>()

    init {
    }

    override fun register(provider: ProviderType) {
        this.providers.add(provider)
    }

    override fun log(event: T) {
        this.providers.forEach { provider ->
            event.name(provider)?.let { name ->
                val parameters = event.parameters(provider)
                provider.log(name, parameters)
            }
        }
    }
}

sealed class MyEvent : EventType {

    class SignUp(username: String) : MyEvent()

    override fun name(provider: ProviderType): String? {
        when (this) {
            is SignUp -> return "signUp"
        }
    }

    override fun parameters(provider: ProviderType): HashMap<String, Any>? {
        when (this) {
            is SignUp -> {
                return hashMapOf()
            }
        }
    }
}