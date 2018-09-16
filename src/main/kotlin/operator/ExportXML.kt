package operator

import Properties
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ExportDimenXML(private val parentPath: String, private val exportPath: String) {
    private val document by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        docBuilder.newDocument()
    }
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory.newInstance().newTransformer()

    fun createRootElement(tagName: String): Element = document.createElement(tagName).apply {
        document.appendChild(this)
    }


    fun createChildNode(rootElement: Element, elementName: String, attrName: String, attrValue: String, nodeValue: String) {
        document.createElement(elementName).run {
            setAttributeNode(document.createAttribute(attrName).apply { value = attrValue })
            appendChild(document.createTextNode(nodeValue))
            rootElement.appendChild(this)
        }
    }

    fun exportXMLFile() =
            with(transformer) {
                setOutputProperty(Properties.XmlPropertyKeys.INDENT, Properties.XmlPropertyValue.YES)
                setOutputProperty(Properties.XmlPropertyKeys.OMIT_XML_DECLARATION, Properties.XmlPropertyValue.YES)
                setOutputProperty(Properties.XmlPropertyKeys.INDENT_AMOUNT, Properties.XmlPropertyValue.INDENT_VAULE)
                transform(DOMSource(document), with("$parentPath/$exportPath") {
                    File(this).mkdir()
                    StreamResult("$this/dimens.xml")
                })
            }
}