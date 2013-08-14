package com.ku6.cdn.dispatcher.common.collection;

public interface Pair<T1, T2> {

	public T1 first();
	
	public T2 second();
	
	public void setFirst(T1 first);
	
	public void setSecond(T2 second);
	
}
