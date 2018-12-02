import java.net.URL

object ResourceURL {
    val MAIN_UI: URL = javaClass.getResource("main.fxml")!!
    val SETTING_UI: URL = javaClass.getResource("settings.fxml")!!
    val PRESET_XML: URL = javaClass.getResource("preset.xml")!!
}