package controller

import Properties
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.input.TransferMode
import javafx.stage.FileChooser
import normalizeFilePath
import operator.ParserExecutor
import java.io.File
import java.net.URL
import java.util.*
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

    private val fileChooser: FileChooser by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FileChooser().apply {
            title = "Open..."
            initialDirectory = File(System.getProperty((Properties.System.ROOT)))
            Properties.System.XML_EXTENSION.let { extensionFilters.addAll(arrayOf(FileChooser.ExtensionFilter(it, "*.$it"))) }
        }
    }
    private var lastPath: String? = null
    private var file: File? = null

    init {
        ParserExecutor.setCallback(this@MainUIController)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        menuItemOpen.setOnAction {
            println("Open Click")
            fileExplorer()
        }

        menuItemClose.setOnAction {
            exitProcess(100)
        }

        executeButton.setOnAction {
            executeGenerator(checkNotNull(file) {
                mainLabel.text = "Open ../dimens.xml file first"
                return@setOnAction
            })
        }

        with(mainParent) {
            setOnDragOver { e ->
                when (e.dragboard.hasFiles()) {
                    true -> e.acceptTransferModes(TransferMode.COPY)
                    false -> e.consume()
                }
            }
            setOnDragDropped { e ->
                if (e.dragboard.hasFiles()) {
                    setDestinationFile(e.dragboard.files[0])
                    e.isDropCompleted = true
                }
                e.consume()
            }
        }
    }

    private fun setDestinationFile(file: File) {
        this.file = file
        lastPath = file.parentFile.path
        mainLabel.text = file.path.normalizeFilePath()
    }

    private fun fileExplorer() {
        fileChooser.run {
            initialDirectory = if (lastPath.isNullOrEmpty()) {
                File(System.getProperty((Properties.System.ROOT)))
            } else {
                File(lastPath)
            }

            setDestinationFile(checkNotNull(showOpenDialog(mainParent.scene.window)) { "File is empty" })
        }
    }

    private fun executeGenerator(f: File) {
        mainLabel.text = "Waiting..."
        executeButton.isDisable = true
        ParserExecutor.apply {
            initialize(f)
            start()
        }
    }

    override fun onCreateFinish() {
        file = null
        mainLabel.text = "Finish !!"
        executeButton.isDisable = false
    }
}
