package com.ku6.cdn.dispatcher.common;

public class TaskReference {
	
	private int okRef;
	private int completeRef;
	private int type;
	
	public int getOkRef() {
		return okRef;
	}
	
	public TaskReference setOkRef(int okRef) {
		this.okRef = okRef;
		return this;
	}
	
	public int getCompleteRef() {
		return completeRef;
	}
	
	public TaskReference setCompleteRef(int completeRef) {
		this.completeRef = completeRef;
		return this;
	}
	
	public int getType() {
		return type;
	}
	
	public TaskReference setType(int type) {
		this.type = type;
		return this;
	}
	
}
