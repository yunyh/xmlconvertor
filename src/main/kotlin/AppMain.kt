import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class AppMain : Application() {

    override fun start(primaryStage: Stage?) {
        when {
            primaryStage != null -> with(primaryStage) {
                val root: Parent? = FXMLLoader.load(ResourceURL.MAIN_UI)
                val scene = Scene(root)
                this.scene = scene
                title = "Xml Converter"
                show()
            }
        }
    }
}