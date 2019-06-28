package wsdl;

import java.util.List;

import javax.xml.namespace.QName;

import org.reficio.ws.SoapContext;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;

public class SoapClient {

	public static String getSoapMessage(String wsdlUrl, String operationName) {
		Wsdl wsdl = Wsdl.parse(wsdlUrl);
		List<QName> bindings = wsdl.getBindings();
		QName serviceBinding = bindings.get(0);
		String serviceBindingName = serviceBinding.getLocalPart();

		SoapBuilder builder = wsdl.binding().localPart(serviceBindingName).find();
		SoapOperation operation = builder.operation().name(operationName).find();
		SoapContext context = SoapContext.builder().alwaysBuildHeaders(true).buildOptional(true).exampleContent(false)
				.typeComments(false).build();
		String soapRequestMessage = builder.buildInputMessage(operation, context);
		return soapRequestMessage;
	}

}
