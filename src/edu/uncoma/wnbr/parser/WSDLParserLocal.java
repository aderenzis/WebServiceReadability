package edu.uncoma.wnbr.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.schema.*;
import com.predic8.wsdl.*;

import edu.uncoma.wnbr.dom.OperationConcepts;
import edu.uncoma.wnbr.dom.WSDLConcepts;
import edu.uncoma.wnbr.util.ReadabilityUtil;

public class WSDLParserLocal {
	
	private static Vector<Vector<String>> concepts = new Vector<Vector<String>>();
	
	static Map<String,String> types_table = new HashMap<String, String>();

	public static void initializeTable (Map<String,String> types_table) {
		
		types_table.put("string", "String");
		types_table.put("String", "String");
		types_table.put("java.lang.String", "String");
		types_table.put("integer", "java.math.BigInteger");
		types_table.put("int", "int");
		types_table.put("long", "long");
		types_table.put("short", "short");
		types_table.put("decimal", "java.math.BigDecimal");
		types_table.put("float", "float");
		types_table.put("double", "double");
		types_table.put("boolean", "boolean");
		types_table.put("byte", "byte");
		types_table.put("dateTime", "java.util.Calendar");
		types_table.put("date", "java.util.Date");
		types_table.put("base64Binary", "byte[]");
		types_table.put("hexBinary", "byte[]");
		types_table.put("base64", "byte[]");
		types_table.put("anyType", "Object");	
		
	}
	
	

	
	private static void addIdentifierToConcepts(String identifier,int count) {
		if(identifier.length()>0){
			Vector<String> terms = ReadabilityUtil.separarTerminosAuxFine(identifier);
			String term;
			Vector<String> cncepts;
			if(concepts.size()<count+1){
				cncepts = new Vector<String>();
				concepts.add(cncepts);
			}
			else{
				cncepts = concepts.get(count);
			}
			Iterator<String> termsIterator = terms.iterator();
			while(termsIterator.hasNext())
			{
				 term = termsIterator.next();
				 if(!cncepts.contains(term))
					 cncepts.add(term);
			}
		}
	}
	
	private static void addIdentifierToConcepts(String identifier,Vector<String> vecConcepts,Vector<String> concepts) {
		if(identifier.length()>0){
			Vector<String> terms = ReadabilityUtil.separarTerminosAuxFine(identifier);
			ReadabilityUtil.removeNoConcepts(terms);
			try {
				ReadabilityUtil.removeStopWords(terms);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vecConcepts.addAll(terms);
			concepts.addAll(terms);
		}
	}
	
	private static void addIdentifierToConcepts(String identifier, Vector<String> concepts) {
		if(identifier.length()>0){
			Vector<String> terms = ReadabilityUtil.separarTerminosAuxFine(identifier);
			try {
				ReadabilityUtil.removeStopWords(terms);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			concepts.addAll(terms);
		}
	}
	
	
	
	public static void extractConceptsFromWSDL(Vector<String> concepts, String pathWSDL,  WSDLConcepts wsdlConcepts ){
		URI uri = null;
		int count=0;
		pathWSDL = pathWSDL.replace('\\', '/');
		try {
			uri = new URI("file:///"+pathWSDL.replaceAll(" ", "%20"));
			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WSDLParser parser = new WSDLParser();
		try {
			Definitions defs = parser.parse(uri.toURL().toString());
			out("-------------- WSDL Details --------------");
	        out("TargenNamespace: \t" + defs.getTargetNamespace());
	        wsdlConcepts.setName(defs.getTargetNamespace());
	        if (defs.getDocumentation() != null) {
	            out("Documentation: \t\t" + defs.getDocumentation());
	        }
	        out("\n");
	        out("Schemas: ");
	        for (com.predic8.schema.Schema schema : defs.getSchemas()) {
	            out("  TargetNamespace: \t" + schema.getTargetNamespace());
	            List<com.predic8.schema.ComplexType> complexTypes = schema.getComplexTypes();
	            if(complexTypes!=null && complexTypes.size()>0){
	            	for(com.predic8.schema.ComplexType complexType:complexTypes){
	            		Sequence sequence = complexType.getSequence();
	            		if(sequence!=null){
	            			List<com.predic8.schema.Element> elements = sequence.getElements();
	            			if(elements!=null){
	            				for(com.predic8.schema.Element element:elements){
	            					addIdentifierToConcepts(element.getName(),concepts);
	            				}
	            			}
	            			
	            		}
	            		
	            	}
	            }
	        }
	        out("\n");
	         
	        out("Messages: ");
	        for (Message msg : defs.getMessages()) {
	        	addIdentifierToConcepts(msg.getName(), concepts);
	            out("  Message Name: " + msg.getName());
	            out("  Message Parts: ");
	            for (com.predic8.wsdl.Part part : msg.getParts()) {
	            	addIdentifierToConcepts(part.getName(), concepts);
	                out("    Part Name: " + part.getName());
	                if(part.getElement()!=null){
	                	addIdentifierToConcepts(part.getElement().getName(), concepts);
	                }
	                out("    Part Element: " + ((part.getElement() != null) ? part.getElement() : "not available!"));
	                if(part.getType()!=null && part.getType().getName()!=null){
	                	addIdentifierToConcepts(part.getType().getName(), concepts);
	                }
	                out("    Part Type: " + ((part.getType() != null) ? part.getType() : "not available!" ));
	                out("");
	            }
	        }
	        out("");
	 
	        out("PortTypes: ");
	        for (PortType pt : defs.getPortTypes()) {
	            out("  PortType Name: " + pt.getName());
            	addIdentifierToConcepts(pt.getName(), concepts);
	            out("    PortType documentation: "
	                    + pt.getDocumentation());
	            out("  PortType Operations: ");
	            for (com.predic8.wsdl.Operation op : pt.getOperations()) {
	            	OperationConcepts operationConcepts = new OperationConcepts();
	            	addIdentifierToConcepts(op.getName(), operationConcepts.getOperationNameConcepts(),concepts);
	                out("    Operation Name: " + op.getName()); 
	                out("    Operation Input Name: "
	                    + ((op.getInput()!=null && op.getInput().getName() != null) ? op.getInput().getName() : "not available!"));
	                if(op.getInput()!=null && op.getInput().getMessage()!=null && op.getInput().getMessage().getQname()!=null && op.getInput().getMessage().getQname().getLocalPart()!=null)
	                	addIdentifierToConcepts(op.getInput().getMessage().getQname().getLocalPart(),operationConcepts.getInputMessageConcepts(),concepts);
	                if(op.getOutput()!=null){
		                if(op.getOutput().getName()!=null)
		                	addIdentifierToConcepts(op.getOutput().getName(), count);
		                if(op.getOutput().getMessage()!=null)
		                	addIdentifierToConcepts(op.getOutput().getMessage().getQname().getLocalPart(), operationConcepts.getOuputMessageConcepts(),concepts);
	                }
	                out("    Operation Faults: ");
	                if (op.getFaults().size() > 0) {
	                    for (com.predic8.wsdl.Fault fault : op.getFaults()) {
	                    	addIdentifierToConcepts(fault.getName(), operationConcepts.getOperationFaults().getNameConcepts(),concepts);
	                        addIdentifierToConcepts(fault.getMessage().getQname().getLocalPart(), operationConcepts.getOperationFaults().getMessageConcepts(),concepts);
	                    }
	                } else out("      There are no faults available!");
	                
	                 count++;
	                 wsdlConcepts.getOperationConcepts().add(operationConcepts);
	            }
	            out("");
	        }
	        out("");
	 
	        out("Bindings: ");
	        for (Binding bnd : defs.getBindings()) {
	            out("  Binding Name: " + bnd.getName());
	            out("  Binding Type: " + bnd.getPortType().getName());
	            if(bnd.getBinding() instanceof AbstractSOAPBinding) out("  Style: " + (((AbstractSOAPBinding)bnd.getBinding()).getStyle()));
	            out("  Binding Operations: ");
	            for (BindingOperation bop : bnd.getOperations()) {
	                out("    Operation Name: " + bop.getName());
	                if(bnd.getBinding() instanceof AbstractSOAPBinding) {
	                    out("    Operation SoapAction: " + bop.getOperation().getSoapAction());
	                    out("    SOAP Body Use: " + bop.getInput().getBindingElements().get(0).getUse());
	                }
	            }
	            out("");
	        }
	        out("");
	 
	        out("Services: ");
	        for (com.predic8.wsdl.Service service : defs.getServices()) {
	            out("  Service Name: " + service.getName());
	            out("  Service Potrs: ");
	            for (Port port : service.getPorts()) {
	                out("    Port Name: " + port.getName());
	                out("    Port Binding: " + port.getBinding().getName());
	                out("    Port Address Location: " + port.getAddress().getLocation()
	                    + "\n");
	            }
	        }
	        out("");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	
	 private static void out(String str) {
	    }	
	
}
