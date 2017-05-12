package edu.uncoma.wnbr.main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

import edu.uncoma.wnbr.dom.OperationConcepts;
import edu.uncoma.wnbr.dom.WSDLConcepts;
import edu.uncoma.wnbr.parser.WSDLParserLocal;
import edu.uncoma.wnbr.util.ReadabilityUtil;

public class Main {

	/**
	 * @param args
	 */
	
public static String workspacePath;	


private static Vector<String> concepts = new Vector<String>();
	
	//Construyo la tabla de mapeo entre tipos de java y wsdl
	
	
	
	
	public static void runExperimento(String sourcePath, String workspacePath)
	{
		//Creacion de archivo result.txt en carpeta results
 		File fResult;
		fResult = new File(workspacePath+"results"+File.separator+"result"+(new Date())+".txt");
		//Escritura
		try{
			FileWriter w = new FileWriter(fResult);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);  
	
			
			FileWriter wx = new FileWriter(workspacePath+"results"+File.separator+"errors"+new Date()+".txt");
			BufferedWriter bwx = new BufferedWriter(wx);
			PrintWriter wrx = new PrintWriter(bwx);  
			
			
			String wsdlFolder = sourcePath;
			
			
			File dir = new File(wsdlFolder);	
		
			String[] files = dir.list();
			
			if (files == null)
				  System.out.println("No files into the specified folder");
			else { 
				int countErrors=0;
				ReadabilityUtil.initializeDictionary();
				File[] fileList = dir.listFiles();
				ReadabilityUtil.initilizePrintWriter(wr);
				ReadabilityUtil.initializeDictionaryJWNL();
				for (int i = 0; i < fileList.length; i++){
					
					concepts =  new Vector<String>();
					File fileAux = fileList[i];
					System.out.println("Analizando "+(i+1)+File.separator+fileList.length + "  --- Errores: "+countErrors+" -- "+fileAux.getName());
					try{
						WSDLConcepts wsdlConcepts = new WSDLConcepts();
						
						WSDLParserLocal.extractConceptsFromWSDL(concepts,fileAux.getAbsolutePath(),wsdlConcepts);
						wr.append(fileAux.getName()+";");
						ReadabilityUtil.getCRS(concepts,fileAux.getName(),wsdlConcepts);
					}catch(Exception e){
						
						e.printStackTrace();
						countErrors++;
						wrx.append(fileAux.getName()+"  -  "+e.getStackTrace()+"\n");
					}
					
				}
					System.out.println("Errores: "+countErrors);
			 }
				wr.close();
				bw.close();
				wrx.close();
				bwx.close();
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		//	String sourcePath=args[1];
			workspacePath=args[0];
		//	runExperimento(sourcePath, workspacePath);
		ReadabilityUtil.initializeDictionary();
		ReadabilityUtil.initializeDictionaryJWNL();
		ReadabilityUtil.getReadabilityValue("/home/aderenzis/Auxiliares/wsdlDataset/absolutedrinks.wsdl");

		}
	
	
	
	public static void countWordNetRatio(String sourcePath, String workspacePath)
	{
		//Creacion de archivo result.txt en carpeta results
 		File fResult;
		fResult = new File(workspacePath+"results"+File.separator+"result"+(new Date())+".txt");
		double totalWords=0;
		double wnWords=0;
		//Escritura
		try{
			FileWriter w = new FileWriter(fResult);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);  
	
			
			FileWriter wx = new FileWriter(workspacePath+"results"+File.separator+"errors"+new Date()+".txt");
			BufferedWriter bwx = new BufferedWriter(wx);
			PrintWriter wrx = new PrintWriter(bwx);  
			
			
			String wsdlFolder = sourcePath;
			
			
			File dir = new File(wsdlFolder);	
		
			String[] files = dir.list();
			
			if (files == null)
				  System.out.println("No files into the specified folder");
			else { 
				int countErrors=0;
				ReadabilityUtil.initializeDictionary();
				File[] fileList = dir.listFiles();
				ReadabilityUtil.initilizePrintWriter(wr);
				ReadabilityUtil.initializeDictionaryJWNL();
				
				for (int i = 0; i < fileList.length; i++){
					
					concepts =  new Vector<String>();
					File fileAux = fileList[i];
					System.out.println("Analizando "+(i+1)+File.separator+fileList.length + "  --- Errores: "+countErrors+" -- "+fileAux.getName());
					try{
						WSDLConcepts wsdlConcepts = new WSDLConcepts();
						
						WSDLParserLocal.extractConceptsFromWSDL(concepts,fileAux.getAbsolutePath(),wsdlConcepts);
						
						for(OperationConcepts operationConcepts: wsdlConcepts.getOperationConcepts()){
							Vector<String> operationInputConcepts = operationConcepts.getInputMessageConcepts();
							ReadabilityUtil.removeStopWords(operationInputConcepts); 
							for(String str:operationInputConcepts){
								if(ReadabilityUtil.isWord(str)){
									wnWords++;
								}
								totalWords++;
							}
							Vector<String> operationOutputConcepts = operationConcepts.getOuputMessageConcepts();
							ReadabilityUtil.removeStopWords(operationOutputConcepts); 
							for(String str:operationOutputConcepts){
								if(ReadabilityUtil.isWord(str)){
									wnWords++;
								}
								totalWords++;
							}
							Vector<String> operationNameConcepts = operationConcepts.getOperationNameConcepts();
							ReadabilityUtil.removeStopWords(operationNameConcepts); 
							for(String str:operationNameConcepts){
								if(ReadabilityUtil.isWord(str)){
									wnWords++;
								}
								totalWords++;
							}
						}
					}catch(Exception e){
						
						e.printStackTrace();
						countErrors++;
						wrx.append(fileAux.getName()+"  -  "+e.getStackTrace()+"\n");
					}
					
				}
					System.out.println("Errores: "+countErrors);
			 }
				wr.close();
				bw.close();
				wrx.close();
				bwx.close();
		
		}catch(IOException e){
			e.printStackTrace();
		}
		
		System.out.println("TotalWords: "+totalWords);
		System.out.println("WNWords: "+wnWords);
		System.out.println("ratio:"+ wnWords/totalWords);
	}
	



}
				

			

