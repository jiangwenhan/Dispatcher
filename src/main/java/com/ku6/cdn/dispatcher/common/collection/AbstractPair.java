package com.ku6.cdn.dispatcher.common.collection;

public abstract class AbstractPair<T1, T2> implements Pair<T1, T2> {
	
	private T1 first;
	private T2 second;
	
	public AbstractPair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public T1 first() {
		return first;
	}

	@Override
	public T2 second() {
		return second;
	}

	@Override
	public void setFirst(T1 first) {
		this.first = first;
	}

	@Override
	public void setSecond(T2 second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractPair<?, ?> other = (AbstractPair<?, ?>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

}
