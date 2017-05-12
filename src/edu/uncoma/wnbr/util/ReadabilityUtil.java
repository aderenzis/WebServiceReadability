package edu.uncoma.wnbr.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import edu.uncoma.wnbr.parser.WSDLParserLocal;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;


import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.*;
import edu.uncoma.wnbr.dom.OperationConcepts;
import edu.uncoma.wnbr.dom.WSDLConcepts;
import edu.uncoma.wnbr.main.Main;

public class ReadabilityUtil {
		
		/**
			 * @param args
			 */
			
			public static double MAX_TREE_DEPTH =16;
			static IDictionary dictionary = null;
			static String path = System.getenv("WNHOME")+"dict";
			static URL url = null;
			static int MAXV=1000;
			private static net.didion.jwnl.dictionary.Dictionary dictionaryJWNL;
			
			private static PrintWriter wr;
			
			
			
			public static boolean isHiperonym(String word1, String word2) {
				Vector<String> hiperonimosWord2 = getHyperonyms(word2);
				if (hiperonimosWord2.contains(word1) ) {
					return true;
				/*}*else {
					Vector<String> hiperonimosWord2 = getHyperonyms(word2);
					if (hiperonimosWord2.contains(word1)) {
						return true;
					}*/
				}
				else
					return false;
			}
			
			public static void initilizePrintWriter(PrintWriter pw){
				wr=pw;
			}
			public static void removeStopWords(Vector<String> v) throws IOException
			{
				BufferedReader entrada = new BufferedReader(new FileReader(Main.workspacePath+"stoplistReadability.txt"));
				String renglon;
				Vector<String> vStopWords = new Vector<String>();
				while ((renglon = entrada.readLine()) != null) {
					vStopWords.add(renglon);
				}
				v.removeAll(vStopWords);
				entrada.close();

			}
			
			public static boolean isHiponym(String word1, String word2) {
				Vector<String> hiponimosWord2 = getHyponyms(word2);
				if (hiponimosWord2.contains(word1) ) {
					return true;
				}
				else
					return false;
			}
			
			public static Vector<String> getHyperonyms(String wordQuery) { //Obtiene Hyperonimos de todos los sinonimos de la palabra
				Vector<String> resultado = new Vector<String>();
				IIndexWord idxWord = dictionary.getIndexWord(wordQuery, POS.NOUN);
				
				if (idxWord == null) {
					idxWord = dictionary.getIndexWord(wordQuery, POS.VERB);
				if (idxWord == null) {
						idxWord = dictionary.getIndexWord(wordQuery, POS.ADJECTIVE);
					}
				if (idxWord == null) {
					idxWord = dictionary.getIndexWord(wordQuery, POS.ADVERB);
				}
				if (idxWord == null)
					return resultado;
				}
				List<IWordID> words = idxWord.getWordIDs();		
				for (Iterator<IWordID> iterator = words.iterator(); iterator.hasNext();) {
					IWordID wordID = iterator.next();			
					IWord word = dictionary.getWord(wordID);
					ISynset synset = dictionary.getSynset(word.getSynset().getID());
					
					
					List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
					List<IWord> wordsList = null;
					for (ISynsetID sid : hypernyms) {				
						wordsList = dictionary.getSynset(sid).getWords();
						for (Iterator<IWord> i = wordsList.iterator(); i.hasNext();) {					
							String lexemaString = i.next().getLemma();
							if(!resultado.contains(lexemaString))
								resultado.add(lexemaString);
						}
					}
				}
				return resultado;
			}
			
			public static POS getPOS(String word){
				POS pos = null;
				word=word.trim();
			//	System.out.println(word);
				if(word.length()>0){
					IIndexWord idxWord = dictionary.getIndexWord(word, POS.NOUN);
					
					if (idxWord != null){
						return POS.NOUN;
					}
					else{
						idxWord = dictionary.getIndexWord(word, POS.VERB);
						if (idxWord != null){
							return POS.VERB;
						}
						else{
							idxWord = dictionary.getIndexWord(word, POS.ADJECTIVE);
							if (idxWord != null){
								return POS.ADJECTIVE;
							}
							else{
								idxWord = dictionary.getIndexWord(word, POS.ADVERB);
								if (idxWord != null){
									return POS.ADVERB;
								}
							}
						}
					}
				}
				return pos;
			}
			public static Vector<String> getHyperonyms(String wordQuery, POS pos) { //Obtiene Hyperonimos de todos los sinonimos de la palabra
				Vector<String> resultado = new Vector<String>();
				IIndexWord idxWord = dictionary.getIndexWord(wordQuery, pos);
				if (idxWord == null)
					return resultado;
				List<IWordID> words = idxWord.getWordIDs();		
				for (Iterator<IWordID> iterator = words.iterator(); iterator.hasNext();) {
					IWordID wordID = iterator.next();			
					IWord word = dictionary.getWord(wordID);
					ISynset synset = dictionary.getSynset(word.getSynset().getID());
					
					
					List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
					List<IWord> wordsList = null;
					for (ISynsetID sid : hypernyms) {
						wordsList = dictionary.getSynset(sid).getWords();
						for (Iterator<IWord> i = wordsList.iterator(); i.hasNext();) {					
							String lexemaString = i.next().getLemma();
							if(!resultado.contains(lexemaString))
								resultado.add(lexemaString);
						}
					}
				}
				return resultado;
			}
			
			public static Vector<String> getHyponyms(String wordQuery) { //Obtiene Hyponimos de todos los sinonimos de la palabra
				Vector<String> resultado = new Vector<String>();
				IIndexWord idxWord = dictionary.getIndexWord(wordQuery, POS.NOUN);
				
				if (idxWord == null) {
					idxWord = dictionary.getIndexWord(wordQuery, POS.VERB);
				if (idxWord == null) {
						idxWord = dictionary.getIndexWord(wordQuery, POS.ADJECTIVE);
					}
				if (idxWord == null) {
					idxWord = dictionary.getIndexWord(wordQuery, POS.ADVERB);
				}
				if (idxWord == null)
					return resultado;
				}
				List<IWordID> words = idxWord.getWordIDs();		
				for (Iterator<IWordID> iterator = words.iterator(); iterator.hasNext();) {
					IWordID wordID = iterator.next();			
					IWord word = dictionary.getWord(wordID);
					ISynset synset = dictionary.getSynset(word.getSynset().getID());
					
					
					List<ISynsetID> hyponyms = synset.getRelatedSynsets(Pointer.HYPONYM);
					List<IWord> wordsList = null;
					for (ISynsetID sid : hyponyms) {				
						wordsList = dictionary.getSynset(sid).getWords();
						for (Iterator<IWord> i = wordsList.iterator(); i.hasNext();) {					
							String lexemaString = i.next().getLemma();
							if(!resultado.contains(lexemaString))
								resultado.add(lexemaString);
						}
					}
				}
				return resultado;
			}
			
			

			
	

			public static Vector<String> separarTerminosAuxFine(String term)
			{
				Vector<String> vec = new Vector<String>();
				if(term.length()>0)
				{
					boolean mayus =false;
					String ret="";
					String retMayus="";
					char lastMayus=0;
					char charAux;
					if(term.charAt(0)>=65 && term.charAt(0)<=90) // Si es mayuscula la 1er letra
					{
						charAux= (char) (term.charAt(0)+32); // guarda la minuscula
						ret=Character.toString(charAux); // ret almaceno la letra
						retMayus=Character.toString(charAux); // retMayus almaceno
						mayus=true; 
					}
					else
						ret=Character.toString(term.charAt(0)); // si no es mayuscula se almacena el char en ret
					for(int i=1;i< term.length();i++)
					{
						if(term.charAt(i)>=65 && term.charAt(i)<=90) // Si es una mayuscula
						{
							//if(ret.length()>0 || retMayus.length()>0)
								
								if(!mayus) //Es la primer Mayuscula
								{
									if(retMayus.length()>1) // Ya existia anteriormente una seguidilla de mayusculas
									{
										if(isWord(lastMayus+ret))//es una palabra la ultima mayuscula + minusculas
										{
											vec.add(retMayus.substring(0, retMayus.length()-1));
											vec.add(lastMayus+ret);
											lastMayus=0;
											retMayus="";
											ret="";
										}
										else
										{
											vec.add(retMayus);
											vec.add(ret);
											lastMayus=0;
											retMayus="";
											ret="";
										}
									}
									else // No existia anteriormente una seguidilla de mayusculas
										if(ret.length()>0)
											vec.add(ret);
									
									mayus=true;
									charAux= (char) (term.charAt(i)+32);
									ret=Character.toString(charAux);
									retMayus=Character.toString(charAux);
								} 
								else //No es la primer mayuscula consecutiva
								{
									charAux= (char) (term.charAt(i)+32);
									retMayus = retMayus+charAux;
									ret="";
								}
							
							
						}
						else //No es una Mayuscula
						{
							if(term.charAt(i) == 45 || term.charAt(i)== 95 || esNumero(term.charAt(i)) || !esLetra(term.charAt(i))) //  Si es _ o -
							{
								if(ret.length()>0) // si el guion esta despues de una acumulacion de Minusculas
								{
									vec.add(ret);
									ret="";
									retMayus="";
								}
								else if(retMayus.length()>0) // si el guion esta despues de una acumulacion de Mayusculas
								{
									vec.add(retMayus);
									retMayus="";
								}
									
								mayus=false;
							} // No es mayuscula ni _ ni - ni Numero// es una letra minuscula
							else
							{
								if(mayus) // la Letra anterior era una mayuscula
								{
									lastMayus= (char) (term.charAt(i-1)+32);
									ret=ret+term.charAt(i);
									mayus=false;
								}
								else // la letra anterior no era mayuscula
								{
									ret=ret+term.charAt(i);
								}
								
							}
						}
					}
					if(ret.length()>0 | retMayus.length()>1)
					{
						if(retMayus.length()>1) // Ya existia anteriormente una seguidilla de mayusculas
						{
							if(lastMayus != 0 && ret.length()>0 && isWord(lastMayus+ret)) // Es un && porque si lastMayus es 0 no debe entrar al metodo isWord.
							{
								vec.add(retMayus.substring(0, retMayus.length()-1)); 
								vec.add(lastMayus+ret);
								lastMayus=0;
								retMayus="";
								ret="";
							}
							else
							{
								if(retMayus.length()>1);
									vec.add(retMayus);
								if(ret.length()>0)
									vec.add(ret);
								lastMayus=0;
								retMayus="";
								ret="";
							}
						}
						else
							vec.add(ret);
					}
				}
				return vec;
			}
			
			private static boolean esLetra(char charAt) {
				return (65<=charAt && charAt<=90 ) || (97<=charAt && charAt<=122 );
			}

			private static boolean esNumero(char charAt) {
				
				return (48<=charAt && charAt<=57);
			}
			
			public static boolean isWord (String wordQuery)
			{
				try{
					if(wordQuery.length()>0){
						wordQuery=wordQuery.trim();
						IIndexWord idxWord = dictionary.getIndexWord(wordQuery, POS.NOUN);
						if (idxWord != null)
							return true;
						idxWord = dictionary.getIndexWord(wordQuery, POS.VERB);
						if (idxWord != null)
							return true; 
						idxWord = dictionary.getIndexWord(wordQuery, POS.ADJECTIVE);
					    if (idxWord != null)
					    	return true;
						idxWord = dictionary.getIndexWord(wordQuery, POS.ADVERB);
						if (idxWord != null)
							return true;
						return false;
					}
					else
						return false;
				}
				catch(IllegalArgumentException e){
					return false;
				}
					
			}
						
			public static Vector<String> getVectorSteaming(Vector<String> vec)
			{
				WordnetStemmer wns= new WordnetStemmer(dictionary); 
				int length = vec.size();
				List<String> stems1;
				Vector<String> ret=new Vector<String>();
				int aux;
				for (int i=0;i<length;i++)
				{
					stems1 = wns.findStems((String) vec.get(i),POS.NOUN);
					if (stems1.size()==0)
						stems1 = wns.findStems((String) vec.get(i),POS.VERB);
					if (stems1.size()==0)
						stems1 = wns.findStems((String) vec.get(i),POS.ADVERB);
					if(stems1.size()==0) // Si la palabra no existe en el diccionario la agrega a la lista de stems. Posible abrebiatura o Sigla
					{
						if(!ret.contains(vec.get(i)))
							ret.add(vec.get(i));
					}
					//stems2 = wns.findStems((String) vec.get(i), POS.ADJECTIVE); //Con Los adjetivos no funciona BIEN.
					for (aux=0;aux<stems1.size();aux++)
						if(!ret.contains(stems1.get(aux)))
							ret.add(stems1.get(aux));
					/*for (aux=0;aux<stems2.size();aux++)
						if(!ret.contains(stems2.get(aux)))
							ret.add(stems2.get(aux));*/
				}
				return ret;
			}
			
			public static void initializeDictionary(){
				try {
					url = new URL("file", "localhost", path);
				} catch (MalformedURLException e) {
				}
				if (url == null) 
					System.out.println("Url is Null");
				//System.out.println(path);
				dictionary = new Dictionary(url);
				try {
					dictionary.open();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public static boolean isConcept(String term, POS pos) {
				if(dictionary==null){
					initializeDictionary();
				}
				IIndexWord idxWord = dictionary.getIndexWord(term, pos);
				return (idxWord!=null);
			}
			
			public static void initializeDictionaryJWNL(){
				try {
					JWNL.initialize(new FileInputStream("file_properties.xml"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (JWNLException e) {
					e.printStackTrace();
				}
				// Create dictionary object
			
				dictionaryJWNL = net.didion.jwnl.dictionary.Dictionary.getInstance();
			}
			
			public static double getDepth(String word1, String word2){
				double minDepth = 16.00;
				net.didion.jwnl.data.POS posWord1 = getPosJWNL(word1);
				net.didion.jwnl.data.POS posWord2 = getPosJWNL(word2);
				if(posWord1!=null && posWord2!=null && posWord1.equals(posWord2)){
					try {
						double minDepthAux;
						IndexWord iw1 = dictionaryJWNL.getIndexWord(posWord1, word1);
						IndexWord iw2 = dictionaryJWNL.getIndexWord(posWord2, word2);
						//System.out.println(iw1.getSenses().length);
						for (net.didion.jwnl.data.Synset senseIw1: iw1.getSenses()){
							for (net.didion.jwnl.data.Synset senseIw2: iw2.getSenses()){
								RelationshipList rel = RelationshipFinder.getInstance().findRelationships(senseIw1, senseIw2,PointerType.HYPERNYM);
							//	System.out.println("Size rel:"+rel.toArray().length);
								for(Object relation: rel.toArray()){
									minDepthAux = ((Relationship)relation).getDepth();
								//	System.out.println("Relationship: "+minDepthAux);
									if(minDepthAux<minDepth)
										minDepth= minDepthAux;
									
								}
							}
						}
						
					} catch (JWNLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
				return minDepth;
			}
			
			private static net.didion.jwnl.data.POS getPosJWNL(String word) {
				net.didion.jwnl.data.POS pos = null;
				try {
					IndexWord idxWord = dictionaryJWNL.getIndexWord(net.didion.jwnl.data.POS.NOUN, word);
					
					if (idxWord != null){
						return net.didion.jwnl.data.POS.NOUN;
					}
					else{
						idxWord = dictionaryJWNL.getIndexWord(net.didion.jwnl.data.POS.VERB, word);
						if (idxWord != null){
							return net.didion.jwnl.data.POS.VERB;
						}
						else{
							idxWord = dictionaryJWNL.getIndexWord(net.didion.jwnl.data.POS.ADJECTIVE, word);
							if (idxWord != null){
								return net.didion.jwnl.data.POS.ADJECTIVE;
							}
							else{
								
									idxWord = dictionaryJWNL.getIndexWord(net.didion.jwnl.data.POS.ADVERB, word);
								if (idxWord != null){
									return net.didion.jwnl.data.POS.ADVERB;
								}
							}
						}
					}
				} catch (JWNLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return pos;
			}

			public static double getDepthWord(String word1,POS pos) {
				Vector<String> hiperonimosWord = getHyperonyms(word1,pos);
				Vector<String> usedWords = new Vector<String>();
				usedWords.add(word1);
				double ret=0;
				while(hiperonimosWord!=null && hiperonimosWord.size()>0){
					hiperonimosWord.removeAll(usedWords);
					ret++;
					usedWords.addAll(hiperonimosWord);
					if(hiperonimosWord.size()>0)
						hiperonimosWord = getHyperonyms((String)hiperonimosWord.get(0));
				}
				return ret;
			}
			
			public static double getScoopeV2(Vector<String> concepts){
				double depthAcum = 0.0;
				POS pos;
				for(String concept: concepts){
					if(concept.length()>0){
						pos = getPOS(concept);
						if(pos!=null){
							double depth = getDepthWord(concept, pos);
							if(depth>MAX_TREE_DEPTH){
								depth=MAX_TREE_DEPTH;
							}
							depthAcum = (float) (depthAcum + depth);
						}
					}
				}
				//Systemd.out.println("depthAcum: "+ depthAcum);
				if(concepts.size()!=0)
					return (depthAcum/concepts.size())/MAX_TREE_DEPTH;
				else
					return 0.0;
			}
			
			public static float getScoope(Vector<String> concepts){
				float depthAcum = (float) 0.0;
				POS pos;
				for(String concept: concepts){
					pos = getPOS(concept);
					if(pos!=null){
						depthAcum = (float) (depthAcum + getDepthWord(concept, pos));
					}
				}
				//System.out.println("depthAcum: "+ depthAcum);
				float e=(float)Math.E;
				//System.out.println("e: "+e);
				float ret = (float) Math.pow(e, (float)-depthAcum);
				
				return ret;
			}
			
			
			
			public static double getCohesion(WSDLConcepts wsdlConcepts){
				Vector<OperationConcepts> operations = wsdlConcepts.getOperationConcepts();
				double cohesion=0.0;
				for(OperationConcepts operation:operations){
					double actualComparations =0;
					double summation =0.0;
					Vector<String> nameConcepts = operation.getOperationNameConcepts();
					Vector<String> inputConcepts = operation.getInputMessageConcepts();
					Vector<String> ouputConcepts = operation.getOuputMessageConcepts();
					for(String nameConcept: nameConcepts){
						
						for(String inputConcept:inputConcepts){
							double sim = getSim(nameConcept, inputConcept);
							summation=summation+sim;
							actualComparations++;
						}
						for(String ouputConcept: ouputConcepts){
							double sim = getSim(nameConcept, ouputConcept);
							summation=summation+sim;
							actualComparations++;
						}
					}
					if(actualComparations!=0){
//						summation=summation/nameConcepts.size();
						summation=summation/actualComparations;
					}
//					if(ouputConcepts.size()>0)
//						summation=summation/2;
					cohesion=cohesion+summation;
				}
				if(operations.size()>0)
					cohesion=cohesion/operations.size();
				return cohesion;
			}
			
			public static double getSim(String conceptA, String conceptB){
				double ret=1;
				if(!conceptA.equalsIgnoreCase(conceptB)){
					double length = getDepth(conceptA, conceptB);
					//System.out.println("Length: "+length+"  -  "+ 2*MAX_TREE_DEPTH);
					double aux = length / (2*MAX_TREE_DEPTH);
					if(aux!=0)
						ret = (-1.0) * Math.log10(aux);
					else
					{
						if(isWord(conceptA) && isWord(conceptB))
							ret=0;
						else
							ret = 0;
					}
				}
				return ret;
			}
			
			public static void removeNoConcepts(Vector<String> vConcepts){
				for(int i=0; i<vConcepts.size();i++){
						if(!isWord(vConcepts.get(i))){ 
							vConcepts.remove(i);
							
						}
					
				}
			}
			
			
			
			
			public static double getDaCw(Vector<String> concepts){
				BufferedReader entrada;
				Vector<String> vFamiliarWords = new Vector<String>();
				try {
					entrada = new BufferedReader(new FileReader(Main.workspacePath+"daleChallList.txt"));
					String renglon;
					while ((renglon = entrada.readLine()) != null) {
						vFamiliarWords.add(renglon.trim());
					}
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				int countFamilarWords=0;
				for(String concept:concepts){
					if(vFamiliarWords.indexOf(concept)!=-1){
						countFamilarWords++;
					}
					else{
					}
				}
				
				return ((double)concepts.size() - (double)countFamilarWords) *100.00 /(double)concepts.size();
			}
			
			public static double getCRS(Vector<String> vConcepts,String name, WSDLConcepts wsdlConcepts){
			//	wr.append(vConcepts.size()+";");
				double cohesion = getCohesion(wsdlConcepts);
				double scoope = getScoopeV2(vConcepts);
				double daCw = getDaCw(vConcepts);
				if(daCw < 1 ) daCw=0;
				double crs = scoope+5*cohesion+(1-(daCw/100));
//				System.out.println(cohesion+"  "+ scoope+"  "+daCw+"  "+ crs);
				System.out.println("WNBR value =  "+ crs);
				System.out.println("Scoope =  "+ scoope);
				System.out.println("Cohesion =  "+cohesion);
				System.out.println("DaCw = "+ (1-(daCw/100)) );
				System.out.println("\n");
				wr.append(crs+"\n");
				return crs;
			}

            public static double getReadabilityValue(String wsdlPath){
				Vector<String> concepts =  new Vector<String>();
				WSDLConcepts wsdlConcepts = new WSDLConcepts();
				WSDLParserLocal.extractConceptsFromWSDL(concepts,wsdlPath,wsdlConcepts);
				double cohesion = getCohesion(wsdlConcepts);
				double scoope = getScoopeV2(concepts);
				double daCw = getDaCw(concepts);
				if(daCw < 1 ) daCw=0;
				double crs = scoope+5*cohesion+(1-(daCw/100));
				System.out.println("WNBR value =  "+ crs);
				System.out.println("Scoope =  "+ scoope);
				System.out.println("Cohesion =  "+cohesion);
				System.out.println("DaCw = "+ (1-(daCw/100)) );
				System.out.println("\n");
				return crs;
			}
			
			
			public static void main(String[] args){
				initializeDictionaryJWNL();
				double result = getDepth("reservation","current");
				System.out.println(result);
			}
				
	}




		


