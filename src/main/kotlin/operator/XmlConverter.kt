/*
package operator

import ResourceURL
import `interface`.Executor
import org.python.core.PyBoolean
import org.python.util.PythonInterpreter
import java.io.File
import java.io.InputStreamReader
import java.util.*

class XmlConverter(path: String, callback: Callback) : Executor {

    private var callback: Callback?

    init {
        initialize(path)
        this.callback = callback
    }

    override fun initialize(doing: Any) {
        if (doing is String) {
            val properties = Properties()
            properties.setProperty("python.path", UsefulUtils.normalizeFilePath(ResourceURL.GENERATOR.path!!))
            println(doing)
            PythonInterpreter.initialize(System.getProperties(), properties, arrayOf("", doing))
        }
    }

    override fun start() {
        pyInterpreter()
    }

    override fun finish() {
        println("Convert finish")
        callback?.let {
            callback!!.onConvertFinish("Finish")
        }
    }

    private fun pyInterpreter() {
        val interpreter = PythonInterpreter()
        // val py = InputStreamReader()
        interpreter.execfile(javaClass.classLoader.getResourceAsStream(("generator.py")))
        //interpreter.execfile(UsefulUtils.normalizeFilePath(file.path))
        if ((interpreter.eval("param") as PyBoolean).booleanValue) {
            finish()
        }
    }

    interface Callback {
        fun onConvertFinish(status: String)
    }
}*/
