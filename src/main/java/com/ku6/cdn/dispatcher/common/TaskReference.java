package com.ku6.cdn.dispatcher.common;

public class TaskReference {
	
	public enum Reference {
		OK_REF,
		COMPLETE_REF
	}
	
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
	
	public synchronized TaskReference incref(Reference reference) {
		switch (reference) {
		case OK_REF:
			this.okRef++;
			break;
		case COMPLETE_REF:
			this.completeRef++;
			break;
		default:
			break;
		}
		return this;
	}
	
	public synchronized TaskReference decref(Reference reference) {
		switch (reference) {
		case OK_REF:
			this.okRef--;
			break;
		case COMPLETE_REF:
			this.completeRef--;
			break;
		default:
			break;
		}
		return this;
	}
	
	public synchronized TaskReference clear() {
		this.okRef = 0;
		this.completeRef = 0;
		return this;
	}
}
