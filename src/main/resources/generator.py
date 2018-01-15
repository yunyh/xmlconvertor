# coding=utf-8
# -*- coding: utf-8 -*-
from shutil import copyfile
from xml.etree.ElementTree import Element, SubElement, dump, ElementTree, Comment
from sys import argv
from os import path, mkdir, makedirs
import xml.sax

# default 360dp
# xhdpi xxhdpi w360_xxxhdpi 360
# l, m, h 320
# xxxhdpi 420
# w410dp 410

ltohdpi = 320.0 / 360.0
xxxhdpi = 420.0 / 360.0
w410dp = 410.0 / 360.0

resourceList = []
exception_list = []


class ParseHandler(xml.sax.ContentHandler):
    def __init__(self):
        self.CurrentData = ""
        self.name = ""

    def startElement(self, name, attrs):
        print("startElement " + name)
        if name == "dimen":
            self.CurrentData = name
            self.name = attrs["name"]
        if name == "string":
            self.CurrentData = name
            self.name = attrs["name"]

    def endElement(self, name):
        print("endElement " + name)
        if name == "dimen":
            self.CurrentData = name
        if name == "string":
            self.CurrentData = name

    def characters(self, content):
        if self.name is not "":
            value = content.lstrip()
            if value:
                print("characters " + value)
                attr = [self.CurrentData, self.name, value]
                resourceList.append(attr)


def parser(path):
    parser = xml.sax.make_parser()
    handler = ParseHandler()
    parser.setFeature(xml.sax.handler.feature_namespaces, 0)
    parser.setContentHandler(handler)
    parser.parse(path)
    return resourceList


def generator_handler(path):
    ltohdpi_resource = Element('resources')
    xxxhdpi_resource = Element('resources')
    w410dp_resource = Element('resources')
    parser_list = parser(path=path)

    ldpi_path = path[:path.find("/values")] + "/values-ldpi/dimens.xml"
    mdpi_path = path[:path.find("/values")] + "/values-mdpi/dimens.xml"
    hdpi_path = path[:path.find("/values")] + "/values-hdpi/dimens.xml"
    ltohdpi_path = [ldpi_path, mdpi_path, hdpi_path]
    convert_ltohdpi(list=parser_list, resource=ltohdpi_resource, file_path=ltohdpi_path)

    xxxhdpi_path = path[:path.find("/values")] + "/values-xxxhdpi/dimens.xml"
    convert_xxxhdpi(list=parser_list, resource=xxxhdpi_resource, file_path=xxxhdpi_path)

    w410dp_path = path[:path.find("/values")] + "/values-w410dp-xxhdpi/dimens.xml"
    convert_w410dp(list=parser_list, resource=w410dp_resource, file_path=w410dp_path)
    return True


def convert_ltohdpi(list, resource, file_path):
    for k in list:
        parsing_type = k[0]
        parsing_name = k[1]
        parsing_value = k[2]
        dimen = SubElement(resource, parsing_type)
        dimen.attrib["name"] = parsing_name
        comment = None

        if k[0] == "px_1":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
            resource.append(comment)
            comment = Comment(" noti ")
        elif k[0] == "noti_main_balance":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
        elif k[0] == "noti_divider":
            comment = Comment(" START Apple Offer ")
        elif k[0] == "cpi_term_and_condition_container_height":
            comment = Comment(" END Apple Offer ")

        if comment is not None:
            resource.append(comment)

        if parsing_type == "string" or parsing_type == u'string':
            dimen.text = parsing_value
        if parsing_type == "dimen" or parsing_type == u'string':
            value = parsing_value[:-2]
            unit = parsing_value[-2:]
            if unit == "dp" or unit == u'dp':
                dp = round(float(value) * ltohdpi, 2)
                result = str(dp) + "dp"
                dimen.text = result
            elif unit == "px" or unit == u'px':
                px = round(float(value) * ltohdpi)
                result = str(px) + "px"
                dimen.text = result

    indent(resource)
    dump(resource)
    doc = ElementTree(resource)

    for destination_path in file_path:
        if not path.isdir(destination_path[:destination_path.find("/dimens.xml")]):
            mkdir(destination_path[:destination_path.find("/dimens.xml")])
        doc.write(destination_path, encoding="utf-8")


def convert_w410dp(list, resource, file_path):
    for k in list:
        parsing_type = k[0]
        parsing_name = k[1]
        parsing_value = k[2]
        dimen = SubElement(resource, parsing_type)
        dimen.attrib["name"] = parsing_name
        comment = None
        if k[0] == "px_1":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
            resource.append(comment)
            comment = Comment(" noti ")
        elif k[0] == "noti_main_balance":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
        elif k[0] == "noti_divider":
            comment = Comment(" START Apple Offer ")
        elif k[0] == "cpi_term_and_condition_container_height":
            comment = Comment(" END Apple Offer ")

        if comment is not None:
            resource.append(comment)

        if parsing_type == "string" or parsing_type == u'string':
            dimen.text = parsing_value
        if parsing_type == "dimen" or parsing_type == u'string':
            value = parsing_value[:-2]
            unit = parsing_value[-2:]
            if unit == "dp" or unit == u'dp':
                dp = round(float(value) * w410dp, 2)
                result = str(dp) + "dp"
                dimen.text = result
            elif unit == "px" or unit == u'px':
                px = round(float(value) * w410dp)
                result = str(px) + "px"
                dimen.text = result

    indent(resource)
    dump(resource)

    if not path.isdir(file_path[:file_path.find("/dimens.xml")]):
        mkdir(file_path[:file_path.find("/dimens.xml")])

    doc = ElementTree(resource)
    doc.write(file_path, encoding="utf-8")


def convert_xxdhpi(list, resource, file_path):
    for k in list:
        parsing_type = k[0]
        parsing_name = k[1]
        parsing_value = k[2]
        dimen = SubElement(resource, parsing_type)
        dimen.attrib["name"] = parsing_name
        comment = None
        if k[0] == "px_1":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
            resource.append(comment)
            comment = Comment(" noti ")
        elif k[0] == "noti_main_balance":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
        elif k[0] == "noti_divider":
            comment = Comment(" START Apple Offer ")
        elif k[0] == "cpi_term_and_condition_container_height":
            comment = Comment(" END Apple Offer ")

        if comment is not None:
            resource.append(comment)

        if parsing_type == "string" or parsing_type == u'string':
            dimen.text = parsing_value
        if parsing_type == "dimen" or parsing_type == u'string':
            value = parsing_value[:-2]
            unit = parsing_value[-2:]
            if unit == "dp" or unit == u'dp':
                dp = round(float(value) * xxdhpi, 2)
                result = str(dp) + "dp"
                dimen.text = result
            elif unit == "px" or unit == u'px':
                px = round(float(value) * xxdhpi)
                result = str(px) + "px"
                dimen.text = result

    indent(resource)
    dump(resource)
    doc = ElementTree(resource)
    if not path.isdir(file_path[:file_path.find("/dimens.xml")]):
        mkdir(file_path[:file_path.find("/dimens.xml")])
    doc.write(file_path, encoding="utf-8")
    xdhpi_path = file_path[:file_path.find("/values-xxhdpi")] + "/values-xhdpi/dimens.xml"
    if not path.isdir(xdhpi_path[:file_path.find("/dimens.xml")]):
        mkdir(xdhpi_path[:file_path.find("/dimens.xml")])
    doc.write(xdhpi_path, encoding="utf-8")


def convert_w360dp_xxxhdpi(list, resource, file_path):
    for k in list:
        parsing_type = k[0]
        parsing_name = k[1]
        parsing_value = k[2]
        dimen = SubElement(resource, parsing_type)
        dimen.attrib["name"] = parsing_name
        comment = None
        if k[0] == "px_1":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
            resource.append(comment)
            comment = Comment(" noti ")
        elif k[0] == "noti_main_balance":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
        elif k[0] == "noti_divider":
            comment = Comment(" START Apple Offer ")
        elif k[0] == "cpi_term_and_condition_container_height":
            comment = Comment(" END Apple Offer ")

        if comment is not None:
            resource.append(comment)

        if parsing_type == "string" or parsing_type == u'string':
            dimen.text = parsing_value
        if parsing_type == "dimen" or parsing_type == u'string':
            value = parsing_value[:-2]
            unit = parsing_value[-2:]
            if unit == "dp" or unit == u'dp':
                dp = round(float(value) * xxdhpi, 2)
                result = str(dp) + "dp"
                dimen.text = result
            elif unit == "px" or unit == u'px':
                px = round(float(value) * xxdhpi)
                result = str(px) + "px"
                dimen.text = result

    indent(resource)
    dump(resource)
    doc = ElementTree(resource)
    if not path.isdir(file_path[:file_path.find("/dimens.xml")]):
        mkdir(file_path[:file_path.find("/dimens.xml")])
    doc.write(file_path, encoding="utf-8")
    xdhpi_path = file_path[:file_path.find("/values-xxhdpi")] + "/values-xhdpi/dimens.xml"
    if not path.isdir(xdhpi_path[:file_path.find("/dimens.xml")]):
        mkdir(xdhpi_path[:file_path.find("/dimens.xml")])
    doc.write(xdhpi_path, encoding="utf-8")


def convert_xxxhdpi(list, resource, file_path):
    for k in list:
        parsing_type = k[0]
        parsing_name = k[1]
        parsing_value = k[2]
        dimen = SubElement(resource, parsing_type)
        dimen.attrib["name"] = parsing_name
        comment = None
        if k[0] == "px_1":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
            resource.append(comment)
            comment = Comment(" noti ")
        elif k[0] == "noti_main_balance":
            comment = Comment(" Default screen margins, per the Android Design guidelines. ")
        elif k[0] == "noti_divider":
            comment = Comment(" START Apple Offer ")
        elif k[0] == "cpi_term_and_condition_container_height":
            comment = Comment(" END Apple Offer ")

        if comment is not None:
            resource.append(comment)

        if parsing_type == "string" or parsing_type == u'string':
            dimen.text = parsing_value
        if parsing_type == "dimen" or parsing_type == u'string':
            value = parsing_value[:-2]
            unit = parsing_value[-2:]
            if unit == "dp" or unit == u'dp':
                dp = round(float(value) * xxxhdpi, 2)
                result = str(dp) + "dp"
                dimen.text = result
            elif unit == "px" or unit == u'px':
                px = round(float(value) * xxxhdpi)
                result = str(px) + "px"
                dimen.text = result

    indent(resource)
    dump(resource)
    if not path.isdir(file_path[:file_path.find("/dimens.xml")]):
        mkdir(file_path[:file_path.find("/dimens.xml")])

    doc = ElementTree(resource)
    doc.write(file_path, encoding="utf-8")


def indent(elem, level=0):
    i = "\n" + level * "    "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "    "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for elem in elem:
            indent(elem, level + 1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = i


input = argv[1]
file_path = path.join(input.rstrip())
try:
    fp = open(file_path, 'r')
    print("open")
except IOError:
    print ("error")
    raise
# ldpi = file_path[:file_path.find("/values")] + "/values-ldpi/dimens.xml"
# mdpi = file_path[:file_path.find("/values")] + "/values-mdpi/dimens.xml"
# hdpi = file_path[:file_path.find("/values")] + "/values-hdpi/dimens.xml"


# if not path.isdir(ldpi[:ldpi.find("/dimens.xml")]):
#    mkdir(ldpi[:ldpi.find("/dimens.xml")])
# if not path.isdir(mdpi[:mdpi.find("/dimens.xml")]):
#    mkdir(mdpi[:mdpi.find("/dimens.xml")])
# if not path.isdir(hdpi[:hdpi.find("/dimens.xml")]):
#    mkdir(hdpi[:hdpi.find("/dimens.xml")])

# copyfile(file_path, ldpi)
# copyfile(file_path, mdpi)
# copyfile(file_path, hdpi)

# Copy 360dp resource
xhdpi = file_path[:file_path.find("/values")] + "/values-xhdpi/dimens.xml"
xxhdpi = file_path[:file_path.find("/values")] + "/values-xxhdpi/dimens.xml"
w360dp_xxxhdpi = file_path[:file_path.find("/values")] + "/values-w360dp-xxxhdpi/dimens.xml"

if not path.isdir(xhdpi[:xhdpi.find("/dimens.xml")]):
    mkdir(xhdpi[:xhdpi.find("/dimens.xml")])
if not path.isdir(xxhdpi[:xxhdpi.find("/dimens.xml")]):
    mkdir(xxhdpi[:xxhdpi.find("/dimens.xml")])
if not path.isdir(w360dp_xxxhdpi[:w360dp_xxxhdpi.find("/dimens.xml")]):
    mkdir(w360dp_xxxhdpi[:w360dp_xxxhdpi.find("/dimens.xml")])

copyfile(file_path, xhdpi)
copyfile(file_path, xxhdpi)
copyfile(file_path, w360dp_xxxhdpi)

# 디버깅 할 때 디버그 설정에서 파라메터 넣어줘야함
if file_path[-3:] == "xml":
    if path.isfile(file_path):
        generator_handler(path=file_path)
        print("Convert Success")
        param = True
    else:
        print("Not exists xml file")
else:
    print("Convert failed")
