package com.ku6.cdn.dispatcher.common.util.builder.impl;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import com.ku6.cdn.dispatcher.common.TaskReference;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class TaskReferenceBuilder implements Builder<TaskReference> {
	
	private int _okRef = 0;
	private int _completeRef = 0;
	private int _type = REPORT_ERROR;

	@Override
	public TaskReference build() {
		TaskReference ref = new TaskReference();
		ref.setOkRef(_okRef);
		ref.setCompleteRef(_completeRef);
		ref.setType(_type);
		return ref;
	}
	
	public TaskReferenceBuilder okRef(int okRef) {
		this._okRef = okRef;
		return this;
	}
	
	public TaskReferenceBuilder completeRef(int completeRef) {
		this._completeRef = completeRef;
		return this;
	}
	
	public TaskReferenceBuilder type(int type) {
		this._type = type;
		return this;
	}

}
