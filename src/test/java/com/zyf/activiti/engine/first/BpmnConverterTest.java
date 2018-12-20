package com.zyf.activiti.engine.first;

import java.io.InputStream;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.impl.util.io.StreamSource;
import org.junit.Test;

public class BpmnConverterTest {

	@Test
	public void convertToBpmnModelTest() {
		InputStream xmlStream = this.getClass().getClassLoader()
				.getResourceAsStream("com/zyf/activiti/diagrams/cp-test_10_595024.bpmn");
		StreamSource xmlSource = new InputStreamSource(xmlStream);
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(xmlSource, false, false, "UTF-8");
		System.out.println("processName: " + bpmnModel.getMainProcess().getName());
		for (FlowElement element : bpmnModel.getMainProcess().getFlowElements()) {
			System.out.println(element.getId());
			if(element instanceof SequenceFlow) {
				SequenceFlow flow = (SequenceFlow)element;
				System.out.println(flow.getId() + " 的源：" + flow.getSourceRef());
				System.out.println(flow.getId() + " 的目的地：" + flow.getSourceRef());
			}
		}
		
	}
}
