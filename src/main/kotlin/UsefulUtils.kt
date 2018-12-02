import java.util.prefs.Preferences

fun String.normalizeFilePath() = this.replace("\\", "/")

fun putPreference(key: String, obj: Any?) = Preferences.userRoot().let {
    System.out.println("put preference")
    println(obj.toString())
    when (obj) {
        is String -> it.put(key, obj)
        is Float -> it.putFloat(key, obj)
        null -> it.remove(key)
        else -> {
            println("what?")
            return@let
        }
    }
}

fun getPreference(key: String) = Preferences.userRoot().run {
    System.out.println(get(key, "empty"))
}
