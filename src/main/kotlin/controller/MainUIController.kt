package controller

import UsefulUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.input.TransferMode
import javafx.stage.FileChooser
import operator.ParserExecutor
import java.io.File
import java.net.URL
import java.util.*
import kotlin.system.exitProcess
import Properties

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
            executeGenerator(when (file) {
                null -> {
                    mainLabel.text = "Open dimens.xml file first"
                    return@setOnAction
                }
                else -> checkNotNull(file)
            })
        }

        with(mainParent) {
            setOnDragOver { event ->
                val board = event.dragboard
                when (board.hasFiles()) {
                    true -> event.acceptTransferModes(TransferMode.COPY)
                    false -> event.consume()
                }
            }
            setOnDragDropped { event ->
                if (event.dragboard.hasFiles()) {
                    setDestinationFile(event.dragboard.files[0])
                    event.isDropCompleted = true
                }
                event.consume()
            }
        }
    }

    private fun setDestinationFile(file: File) {
        this.file = file
        lastPath = file.parentFile.path
        mainLabel.text = UsefulUtils.normalizeFilePath(file.path)
    }

    private fun fileExplorer() {
        run {
            configureFileChooser(fileChooser)
            fileChooser.showOpenDialog(mainParent.scene.window)?.let {
                setDestinationFile(it)
            }
        }
    }

    private fun configureFileChooser(fileChooser: FileChooser) {
        with(fileChooser) {
            title = "Open..."
            initialDirectory = when (lastPath) {
                null, "" -> File(System.getProperty(Properties.System.ROOT))
                else -> File(lastPath)
            }
            Properties.System.XML_EXTENSION.let { extensionFilters.addAll(arrayOf(FileChooser.ExtensionFilter(it, "*.$it"))) }
        }
    }

    private fun executeGenerator(f: File) {
        mainLabel.text = "Waiting..."
        executeButton.isDisable = true
        ParserExecutor(f).apply {
            setCallback(this@MainUIController)
            start()
        }
    }

    override fun onCreateFinish() {
        file = null
        mainLabel.text = "Finish !!"
        executeButton.isDisable = false
    }
}