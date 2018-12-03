package operator.parser

import INDENT
import INDENT_AMOUNT
import INDENT_VALUE
import OMIT_XML_DECLARATION
import YES
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ExportDimensXML(private val parentPath: String, private val exportPath: String) {
    private val document by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        docBuilder.newDocument()
    }
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory.newInstance().newTransformer()

    fun createRootElement(tagName: String): Element = document.createElement(tagName).apply {
        document.appendChild(this)
    }


    fun createChildNode(rootElement: Element, elementName: String, attrName: String, attrValue: String, nodeValue: String) {
        rootElement.appendChild(document.createElement(elementName).apply {
            setAttributeNode(document.createAttribute(attrName).apply { value = attrValue })
            appendChild(document.createTextNode(nodeValue))
        })
    }

    fun exportXMLFile(): Unit =
            with(transformer) {
                setOutputProperty(INDENT, YES)
                setOutputProperty(OMIT_XML_DECLARATION, YES)
                setOutputProperty(INDENT_AMOUNT, INDENT_VALUE)
                transform(DOMSource(document), with("$parentPath/$exportPath") {
                    File(this).mkdir()
                    StreamResult("$this/dimens.xml")
                })
            }
}