package com.e23.compiler;

import java.util.Stack;

class RunningStack extends Stack<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int indexOf(Object o) {
		for (int i = 0 ; i < elementCount ; i++)
            if (o == elementData[i])
                return i;
		return -1;
	}
}
