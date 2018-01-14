package controller

import UsefulUtils
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.stage.FileChooser
import operator.ParserExecutor
import java.io.File
import java.net.URL
import java.util.*
import java.util.concurrent.Executors
import kotlin.system.exitProcess

class MainUIController : Initializable, ParserExecutor.Callback {

    @FXML
    private lateinit var mainParent: Parent
    @FXML
    private lateinit var menuItemOpen: MenuItem
    @FXML
    private lateinit var menuItemClose: MenuItem
    @FXML
    private lateinit var mainLabel: Label
    @FXML
    private lateinit var executeButton: Button

    private val fileChooser = FileChooser()
    private var lastPath: String? = null
    private var file: File? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        menuItemOpen.setOnAction {
            println("Open Click")
            fileExplorer()
        }
        menuItemClose.setOnAction {
            exitProcess(100)
        }
        executeButton.setOnAction {
            // executeGenerator(resourcePath)
            when {
                file != null -> {
                    file?.let {
                        executeGenerator(file!!)
                    }
                }
            }
        }
    }

    private fun fileExplorer() {
        kotlin.run {
            configureFileChooser(fileChooser)
            file = fileChooser.showOpenDialog(mainParent.scene.window)
            file?.let {
                lastPath = file!!.parentFile.path
                val resourcePath = UsefulUtils.normalizeFilePath(file!!.path)
                mainLabel.text = resourcePath
                // openFile(file = file)
            }
        }
    }

    private fun configureFileChooser(fileChooser: FileChooser) {
        fileChooser.title = "Open..."
        when (lastPath) {
            null -> fileChooser.initialDirectory = File(System.getProperty("user.home"))
            else -> fileChooser.initialDirectory = File(lastPath)
        }
        fileChooser.extensionFilters.addAll(arrayOf(FileChooser.ExtensionFilter("xml", "*.xml")))
    }

/*    private fun executeGenerator(path: String?) {
        path?.let {
            Platform.runLater({
                mainLabel.text = "Waiting..."
                executeButton.isDisable = true
            })

            val service = Executors.newSingleThreadExecutor()
            service.execute({
                // XmlConverter(path = path, callback = this).start()
            })
            service.shutdown()
        }
    }*/

    private fun executeGenerator(f: File) {
        mainLabel.text = "Waiting..."
        executeButton.isDisable = true
        val service = Executors.newSingleThreadExecutor()
        service.execute({
            val parserExecutor = ParserExecutor(f)
            parserExecutor.setCallback(this)
            parserExecutor.start()
        })
        service.shutdown()
    }

    override fun onCreateFinish() {
        Platform.runLater({
            file = null
            mainLabel.text = "Finish !!"
            executeButton.isDisable = false
        })
    }

/*    override fun onConvertFinish(status: String) {
        Platform.runLater({
            mainLabel.text = status
            executeButton.isDisable = false
        })
    }*/
}