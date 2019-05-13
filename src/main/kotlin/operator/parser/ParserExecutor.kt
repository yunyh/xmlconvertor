package operator.parser

import DimensRatio
import `interface`.Executor
import javafx.application.Platform
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import model.DimenDataModel
import operator.MyOperator
import operator.XMLFileGenerator
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.coroutines.CoroutineContext


object ParserExecutor : Executor, MyOperator<File>(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private const val PATTERN_DP = "dp"
    private const val PATTERN_PX = "px"
    private const val ELEMENT_NAME_DIMEN = "dimen"
    private const val ATTR_NAME_NAME = "name"

    private lateinit var inputStream: InputStream
    private lateinit var parentPath: String
    private var callback: Callback? = null

    // private val parseArray = ArrayList<DimenDataModel>()

    private lateinit var job: CompletableJob

    fun setCallback(callback: Callback) {
        this@ParserExecutor.callback = callback
    }

    override fun initialize(doing: File) {
        inputStream = ByteArrayInputStream(doing.readBytes())
        parentPath = doing.parentFile.parentFile.path
        //     parseArray.clear()
    }


    //@Throws(IOException::class)
    private val parserXML: (inputStream: InputStream) -> Document? = {
        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
        } catch (e: IOException) {
            null
        }
    }

    override fun start() {
        job = Job().apply { coroutineContext + this }
        launch {
            parserXML(inputStream)?.run {
                getElementsByTagName(ELEMENT_NAME_DIMEN).run convert@{
                    filterElement().map {
                        with(it) {
                            getAttribute(ATTR_NAME_NAME).run {
                                println("$this : ${firstChild.textContent}")
                                DimenDataModel(this, firstChild.textContent)
                            }
                        }
                    }
                }.run list@{
                    XMLFileGenerator.fileGeneratorAsync(parentPath, documentElement.nodeName, this@list)
                }
            }?.apply {
                when (isSuccess) {
                    true -> finish()
                    else -> println("Error parser")
                }
            }
        }
    }

    private fun NodeList.filterElement(): List<Element> = (0..length).map { item(it) }.filterIsInstance<Element>()

    override fun finish() {
        inputStream.close()
        callback?.onCreateFinish()
        if (::job.isInitialized) {
            job.complete()
        }
        println("Finish")
    }

    private fun buildXMLFile(docNodeName: String, parseArray: List<DimenDataModel>) {
        for (ratio: DimensRatio in DimensRatio.values()) {
            ExportDimensXML(parentPath, ratio.pathName()).run {
                createRootElement(docNodeName).let {
                    for (model: DimenDataModel in parseArray) {
                        createChildNode(it, ELEMENT_NAME_DIMEN, ATTR_NAME_NAME, model.name, calculatorToString(model.value, ratio.getRatio()))
                    }
                }
                exportXMLFile()
            }
        }
        finish()
    }

    private fun calculatorToString(objectString: String, ratio: Float): String =
            if (objectString.contains(PATTERN_PX)) {
                println("$objectString : contains px")
                objectString
            } else {
                (objectString.replace(PATTERN_DP, "").toFloat() * ratio).run {
                    (Math.round(this * 100.0) / 100.0).toString().plus(PATTERN_DP)
                }
            }


    @FunctionalInterface
    interface Callback {
        fun onCreateFinish()
    }
}