package edu.uncoma.wnbr.dom;

import java.util.Vector;

public class FaultConcepts {
	
	private Vector<String> nameConcepts;
	private Vector<String> messageConcepts;
	
	public FaultConcepts (){
		nameConcepts=new Vector<String>();
		messageConcepts = new Vector<String>();
	}

	public Vector<String> getNameConcepts() {
		return nameConcepts;
	}
	public void setNameConcepts(Vector<String> nameConcepts) {
		this.nameConcepts = nameConcepts;
	}

	public Vector<String> getMessageConcepts() {
		return messageConcepts;
	}

	public void setMessageConcepts(Vector<String> messageConcepts) {
		this.messageConcepts = messageConcepts;
	}
	
	
	

}
