package com.zyf.activiti.engine.elTest;


import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.junit.Test;

public class ElExpressionTest {

	@Test
	public void testString() {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext sc = new SimpleContext();
		sc.setVariable("GA73832428051890512", factory.createValueExpression(sc, "普通员工", String.class));
		sc.setVariable("GA73832428093833552", factory.createValueExpression(sc, "6", String.class));
		ValueExpression e = factory.createValueExpression(sc, "${GA73832428051890512 != '普通员工' || GA73832428093833552 > 3}", boolean.class);
//		String result = (String) e.getValue(sc);
		System.out.println(e.getValue(sc));
	}
	
	@Test
	public void test2() {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		context.setVariable("count", factory.createValueExpression(1002.00, String.class));
		context.setVariable("APPROVE1", factory.createValueExpression(1000000.00, String.class));
		ValueExpression e = factory.createValueExpression(context, "${(count-APPROVE1)>=0.00}", boolean.class);
		Object result = e.getValue(context);
		System.out.println(result);
	}
	
}
