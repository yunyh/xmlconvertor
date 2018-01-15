package operator

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ExportDimenXML(private val parentPath: String, private val exportPath: Array<String>) {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val document: Document

    init {
        document = docBuilder.newDocument()
    }

    fun createRootElement(tagName: String): Element {
        val rootElement = document.createElement(tagName)
        document.appendChild(rootElement)
        return rootElement
    }

    fun createChildNode(rootElement: Element, elementName: String, attrName: String, attrValue: String, nodeValue: String) {
        val dimenElement = document.createElement(elementName)
        val attr = document.createAttribute(attrName)
        attr.value = attrValue
        dimenElement.setAttributeNode(attr)
        dimenElement.appendChild(document.createTextNode(nodeValue))
        rootElement.appendChild(dimenElement)
    }

    fun exportXMLFile() {
        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val source = DOMSource(document)
        (0 until exportPath.size).map { exportPath[it] }.forEach {
            val destinationPath = "$parentPath/$it"
            File(destinationPath).mkdir()
            val streamResult = StreamResult("$destinationPath/dimens.xml")
            transformer.transform(source, streamResult)
        }
    }
}