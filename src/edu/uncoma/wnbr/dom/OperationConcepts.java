package edu.uncoma.wnbr.dom;

import java.util.Vector;

public class OperationConcepts {
	
	private Vector<String> operationNameConcepts;
	private Vector<String> inputMessageConcepts;
	private Vector<String> ouputMessageConcepts;
	private FaultConcepts operationFaults;
	
	public OperationConcepts(){
		this.inputMessageConcepts = new Vector<String>();
		this.operationFaults= new FaultConcepts();
		this.ouputMessageConcepts= new Vector<String>();
		this.operationNameConcepts=new Vector<String>();
	}
	

	public Vector<String> getOperationNameConcepts() {
		return operationNameConcepts;
	}


	public void setOperationNameConcepts(Vector<String> operationNameConcepts) {
		this.operationNameConcepts = operationNameConcepts;
	}


	public Vector<String> getInputMessageConcepts() {
		return inputMessageConcepts;
	}
	public void setInputMessageConcepts(Vector<String> inputMessageConcepts) {
		this.inputMessageConcepts = inputMessageConcepts;
	}
	public Vector<String> getOuputMessageConcepts() {
		return ouputMessageConcepts;
	}
	public void setOuputMessageConcepts(Vector<String> ouputMessageConcepts) {
		this.ouputMessageConcepts = ouputMessageConcepts;
	}
	public FaultConcepts getOperationFaults() {
		return operationFaults;
	}
	public void setOperationFaults(FaultConcepts operationFaults) {
		this.operationFaults = operationFaults;
	}
	
	
	
	
	

}
