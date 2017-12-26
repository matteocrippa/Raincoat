# 🧥 Raincoat
Analytics abstraction layer for Kotlin inspired by [Umbrella](https://github.com/devxoul/Umbrella).

## Table of Contents

* [Getting Started](#getting-started)
    * [Example](#example)
    * [Providers](#providers)
        * [Custom Provider](#custom-provider)
    * [Creating Custom Providers](#creating-custom-providers)
* [Installation](#installation)
* [License](#license)

## Getting Started

As you can see it is really simple to log with different analytics with a simple function:

```kotlin
val analytics = Raincoat<MyEvent>()
analytics.register(FirebaseProvider(context))
analytics.register(MixpanelProvider(context))
analytics.log(MyEvent.SignUp("username"))
```

### Example

Using `Kotlin` sealed class we can easily setup all things that we will have to track:

```kotlin
sealed class MyEvent : EventType {

    class SignUp(username: String) : MyEvent()
    class Register(username: String, password: String) : MyEvent()

    override fun name(provider: ProviderType): String? {
        return when (this) {
            is SignUp -> "signUp"
            is Register -> "register"
        }
    }

    override fun parameters(provider: ProviderType): HashMap<String, Any>? {
        return when (this) {
            is SignUp -> {
                hashMapOf()
            }
            is Register -> {
                hashMapOf("username" to this.username, "password" to this.password))
            }
        }
    }
}
```

### Providers
At this very moment we developed the following built-in providers:

- [Firebase Analytics](https://firebase.google.com/docs/analytics/)

In order to use them, you have to add to your project the library of the provider, otherwise the build will fail.

#### Custom Provider
You can easily create your own providers, you can use this for reference:

```kotlin
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
```

## Installation

Add to your `build.gradle`:

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

then add the repository

```aidl
compile 'com.github.matteocrippa:Raincoat:0.0.1'
```

## License
Raincoat is under MIT license. See the [LICENSE](LICENSE) file for more info.