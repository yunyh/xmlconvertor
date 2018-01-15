package operator

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ExportDimenXML(private val parentPath: String, private val exportPath: String) {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val document: Document
    private val transformer = TransformerFactory.newInstance().newTransformer()

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
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        val source = DOMSource(document)
        val destinationPath = "$parentPath/$exportPath"
        File(destinationPath).mkdir()
        val streamResult = StreamResult("$destinationPath/dimens.xml")
        transformer.transform(source, streamResult)
    }
}