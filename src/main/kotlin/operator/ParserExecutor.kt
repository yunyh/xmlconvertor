package operator

import DimensRatio
import `interface`.Executor
import javafx.application.Platform
import model.DimenDataModel
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors
import javax.xml.parsers.DocumentBuilderFactory

class ParserExecutor(file: File) : Executor, MyOperator() {

    private val PATTERN_DP: String = "dp"
    private val PATTERN_PX: String = "px"

    private lateinit var inputStream: InputStream
    private lateinit var parentPath: String
    private var callback: Callback? = null

    private val parseArray = ArrayList<DimenDataModel>()

    init {
        initialize(file)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun initialize(doing: Any) {
        if (doing is File) {
            inputStream = ByteArrayInputStream(doing.readBytes())
            parentPath = doing.parentFile.parentFile.path
        }
    }

    override fun start() {

        val service = Executors.newSingleThreadExecutor()
        service.execute({
            val doc = parserXML(inputStream)
            doc?.let {
                val nodeList = doc.getElementsByTagName("dimen")
                (0..nodeList.length)
                        .map { nodeList.item(it) }
                        .forEach {
                            it?.let {
                                val element = it as Element
                                val elementAttr = element.getAttribute("name")
                                println("$elementAttr : ${it.firstChild.textContent}")
                                parseArray.add(DimenDataModel(elementAttr, it.firstChild.textContent))
                            }
                        }
                buildXMLFile(doc.documentElement.nodeName)
                return@execute
            }
            println("Error parser")
        })
        service.shutdown()
    }

    override fun finish() {
        inputStream.close()
        Platform.runLater {
            callback?.let {
                callback!!.onCreateFinish()
            }
            println("Finish")
        }
    }

    private fun buildXMLFile(docNodeName: String) {
        for (ratio in DimensRatio.values()) {
            val export = ExportDimenXML(parentPath, ratio.pathName())
            val rootElement = export.createRootElement(docNodeName)
            for (model in parseArray) {
                val nodeValueToConvert = calculatorToString(model.value, ratio.getRatio())
                export.createChildNode(rootElement, "dimen", "name", model.name, nodeValueToConvert)
            }
            export.exportXMLFile()
        }
        finish()
    }

    private fun parserXML(inputStream: InputStream): Document? {
        try {
            val instance = DocumentBuilderFactory.newInstance()
            val objectBuilder = instance.newDocumentBuilder()
            return objectBuilder.parse(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun calculatorToString(objectString: String, ratio: Float): String {
        if (objectString.contains(PATTERN_PX)) {
            println("$objectString : contains px")
            return objectString
        }
        val value = objectString.replace(PATTERN_DP, "").toFloat() * ratio
        return (Math.round(value * 100.0) / 100.0).toString() + PATTERN_DP
    }

    @FunctionalInterface
    interface Callback {
        fun onCreateFinish()
    }
}