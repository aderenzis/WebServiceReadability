# Readability on Web Service Description


# How to run it 

* Clone the git repository.
* Within the project there is a file called file_properties.xml. In the line 42 of this file, you have to indicate the WordNet dictionary path. Example: <param name="dictionary_path" value="/home/username/tools/WordNet-3.0/dict/"/>.

Download link for WordNet dictionary: http://wordnetcode.princeton.edu/3.0/WordNet-3.0.tar.gz - automatic!

* Configure an enviroment variable called WNHOME with the WordNet instalation directory. 
	Example: WNHOME = /home/username/tools/WordNet-3.0/


* Getting readability value of a document.
```java
ReadabilityUtil.initializeDictionary();
ReadabilityUtil.initializeDictionaryJWNL();
ReadabilityUtil.getReadabilityValue("wsdlPath");
```
* Getting readability values of a set of WSDL documents

	1. Run the Main of the project. This file is located in  the package edu.uncoma.wnbr.main
	1. It is necessary to run the application including these parameters:
		* The first parameter is the project path. Example: /home/username/workspace/WNBasedWSDLReadability/
		* The second parameter is the path where the WSDL documents are located. It is necessary that the WSDL documents are located in a single folder.
	1. Finally the result file will be generated in the result folder inside the project. 







	

