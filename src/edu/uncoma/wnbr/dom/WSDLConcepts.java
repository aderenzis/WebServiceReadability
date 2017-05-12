package edu.uncoma.wnbr.dom;

import java.util.Vector;

public class WSDLConcepts {
	
	private String name;
	private Vector<OperationConcepts> operationConcepts;
	public WSDLConcepts() {
		super();
		this.operationConcepts=new Vector<OperationConcepts>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector<OperationConcepts> getOperationConcepts() {
		return operationConcepts;
	}
	public void setOperationConcepts(Vector<OperationConcepts> operationConcepts) {
		this.operationConcepts = operationConcepts;
	}
	
	

}
