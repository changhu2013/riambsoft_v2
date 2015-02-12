package com.riambsoft.core;

import org.junit.Test;

import com.riambsoft.framework.core.VariablePool;

public class TestServiceEngine {

	@Test
	public void teat1() {
		ServiceEngine engine = new ServiceEngine();
		Foo f = new Foo();
		VariablePool pool = new VariablePool();
	}

}

class Foo {
	
	@RSMethod
	public void f() {

	}

}