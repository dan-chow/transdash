// DashXmlToJava.cpp : main project file.

#include "stdafx.h"
#using <System.xml.dll>

using namespace System;
using namespace System::Diagnostics;
using namespace System::IO;
using namespace System::Text;
using namespace System::Xml;
using namespace System::Xml::Schema;

void xmlToXsd(String^ xmlFile, String^ xsdFile);
void xsdToJava(String^ xsdFile, String^ javaDir);

int main(array<System::String ^> ^args)
{
	String^ xmlFile = "d:\\result.mpd";
	String^ xsdFile = "d:\\result.xsd";
	xmlToXsd(xmlFile, xsdFile);

	String^ javaDir = "d:\\result";
	xsdToJava(xsdFile, javaDir);

	return 0;
}

void xmlToXsd(String^ xmlFile, String^ xsdFile) {
	XmlReader^ reader = XmlReader::Create(xmlFile);
	XmlSchemaSet^ schemaSet = gcnew XmlSchemaSet();

	XmlSchemaInference^ inference = gcnew XmlSchemaInference();
	schemaSet = inference->InferSchema(reader);

	FileStream^ file = gcnew FileStream(xsdFile, FileMode::Create, FileAccess::ReadWrite);
	XmlTextWriter^ writer = gcnew XmlTextWriter(file, gcnew UTF8Encoding());

	for each (XmlSchema^ schema in schemaSet->Schemas())
	{
		schema->Write(file);
	}
}

void xsdToJava(String^ xsdFile, String^ javaDir) {
	if (!Directory::Exists(javaDir)) {
		Directory::CreateDirectory(javaDir);
	}

	String^ cmd = "Xjc";
	String^ args = String::Format("-d {0} -p chow.dan.mpd {1}", javaDir, xsdFile);

	Process::Start(cmd, args);
}