package controller

import ROOT
import ResourceURL
import XML_EXTENSION
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.input.TransferMode
import javafx.stage.FileChooser
import javafx.stage.Stage
import normalizeFilePath
import operator.parser.ParserExecutor
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
    @FXML
    private lateinit var menuItemSetting: MenuItem

    private val fileChooser: FileChooser by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FileChooser().apply {
            title = "Open..."
            initialDirectory = File(System.getProperty(ROOT))
            XML_EXTENSION.let { extensionFilters.addAll(arrayOf(FileChooser.ExtensionFilter(it, "*.$it"))) }
        }
    }

    private var selectFile: Pair<File?, String?> = Pair(null, null)

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
            executeGenerator(checkNotNull(selectFile.first) {
                mainLabel.text = "Open ../dimens.xml file first"
                return@setOnAction
            })
        }

        menuItemSetting.setOnAction {
            val root = FXMLLoader.load(ResourceURL.SETTING_UI, resources) as Parent
            Stage().apply {
                scene = Scene(root)
                show()
            }
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

    private fun setDestinationFile(file: File?) {
        file?.let {
            selectFile = Pair(file, file.parentFile.path)
            //lastPath = file.parentFile.path
            mainLabel.text = file.path.normalizeFilePath()
        }
    }

    private fun fileExplorer() {
        fileChooser.run {
            initialDirectory = if (selectFile.second.isNullOrEmpty()) {
                File(System.getProperty(ROOT))
            } else {
                File(selectFile.second)
            }

            setDestinationFile(showOpenDialog(mainParent.scene.window))
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
      //  file = null
        mainLabel.text = "Finish !!"
        executeButton.isDisable = false
    }
}
