package operator.preset

import operator.MyOperator
import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class PresetOperator : MyOperator<File>() {

    override fun initialize(doing: File) {
        parserXML(ByteArrayInputStream(doing.readBytes()))

    }

    @Throws(IOException::class)
    private fun parserXML(inputStream: InputStream): Document? = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)

}

object PresetManager {
    init {
        PresetOperator().initialize(File(ResourceURL.PRESET_XML.toURI()))
    }
}