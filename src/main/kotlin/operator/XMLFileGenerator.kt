package operator

import DimensRatio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import model.DimenDataModel
import operator.parser.ExportDimensXML
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object XMLFileGenerator : CoroutineScope {

    private const val PATTERN_DP = "dp"
    private const val PATTERN_PX = "px"
    private const val ELEMENT_NAME_DIMEN = "dimen"
    private const val ATTR_NAME_NAME = "name"

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    suspend fun fileGeneratorAsync(fileParentPath: String,
                                   docNodeName: String,
                                   parseArray: List<DimenDataModel>) = suspendCoroutine<ResultType> {
        DimensRatio.values().forEach { ratio ->
            ExportDimensXML(fileParentPath, ratio.pathName()).run {
                createRootElement(docNodeName).let {
                    parseArray.forEach { attrModel ->
                        createChildNode(it, ELEMENT_NAME_DIMEN, ATTR_NAME_NAME, attrModel.name, ratio.calculateToString(attrModel.value))
                    }
                }
                try {
                    exportXMLFile()
                  //  it.resume(ResultType(true))
                } catch (e: IOException) {
                    it.resumeWithException(e)
                    return@suspendCoroutine
                }
            }
        }
        it.resume(ResultType(true))
    }

    private fun DimensRatio.calculateToString(value: String) =
            if (value.contains(PATTERN_PX)) {
                println("$value : contains px")
                value
            } else {
                (value.replace(PATTERN_DP, "").toFloat() * getRatio()).run {
                    (Math.round(this * 100.0) / 100.0).toString().plus(PATTERN_DP)
                }
            }
}

data class ResultType(val isSuccess: Boolean)