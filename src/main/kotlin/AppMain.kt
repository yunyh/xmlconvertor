import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class AppMain : Application() {

    private val root: Parent by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FXMLLoader.load(ResourceURL.MAIN_UI) as Parent
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.run {
            scene = Scene(root)
            title = Properties.APP_TITLE
            show()
        }
    }
}