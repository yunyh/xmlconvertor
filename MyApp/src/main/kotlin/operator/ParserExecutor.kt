package operator

import `interface`.Executor
import model.DimenDataModel
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.exp

class ParserExecutor(file: File) : Executor {

    private val ratio: Float = 320f / 360f
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
        val doc = parserXML(inputStream)
        doc?.let {
            //println(doc.documentElement.nodeName)
            //  val exportPathArray = ArrayList<String>()
            //     DimensRatio.values().mapTo(exportPathArray) { it.pathName() }

            val export = ExportDimenXML(parentPath, arrayOf(DimensRatio.HDPI.pathName(), DimensRatio.LDPI.pathName(), DimensRatio.MDPI.pathName()))
            val rootElement = export.createRootElement(doc.documentElement.nodeName)

            val nodeList = doc.getElementsByTagName("dimen")
            (0..nodeList.length)
                    .map { nodeList.item(it) }
                    .forEach {
                        it?.let {
                            val element = it as Element
                            val elementAttr = element.getAttribute("name")
                            parseArray.add(DimenDataModel(elementAttr, it.firstChild.textContent))
                        }
                    }
            for (model in parseArray) {
                val nodeValueToConvert = calculatorToString(model.value, DimensRatio.HDPI.getRatio())
                export.createChildNode(rootElement, "dimen", "name", model.name, nodeValueToConvert)
            }
            export.exportXMLFile()
            finish()
            return
        }
        println("Error parser")
    }

    override fun finish() {
        inputStream.close()
        callback?.let {
            callback!!.onCreateFinish()
        }
        println("finish")
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
            return objectString
        }
        val value = objectString.replace(PATTERN_DP, "").toFloat() * ratio
        return (Math.round(value * 100) / 100f).toString() + PATTERN_DP
    }

    @FunctionalInterface
    interface Callback {
        fun onCreateFinish()
    }
}