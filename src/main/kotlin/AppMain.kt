import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class AppMain : Application() {

    private val root: Parent by lazy {
        FXMLLoader.load<Parent>(ResourceURL.MAIN_UI)
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.run {
            scene = Scene(root)
            title = APP_TITLE
            show()
        }
    }
}