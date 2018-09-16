import javax.xml.transform.OutputKeys

object Properties {
    const val APP_TITLE = "Xml Converter"

    object XmlPropertyKeys {
        const val INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount"
        const val INDENT = OutputKeys.INDENT
        const val OMIT_XML_DECLARATION = OutputKeys.OMIT_XML_DECLARATION
    }

    object XmlPropertyValue {
        const val YES = "yes"
        const val INDENT_VAULE: String = "4"
    }

    object System {
        const val ROOT = "user.home"
        const val XML_EXTENSION = "xml"
    }
}