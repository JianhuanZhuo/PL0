package com.e23.compiler;

public abstract class Item implements Cloneable {

	protected String name;
	protected ObjectKind kind;

	public enum ObjectKind {
		CONST, VAR, PROC
	}

	public Item(ObjectKind kind, String name) {

		this.kind = kind;
		this.name = name;
	}

	protected abstract Item clone();

	public ObjectKind getKind() {
		return kind;
	}

	public boolean matchID(String ID) {
		return name.equals(ID);
	}

	@Override
	public abstract String toString();

}
