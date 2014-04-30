package com.hades.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.ggf.ns.nmwg.base._2.Message;

public class MessageCreationUtils {
	public static SOAPMessage createSoapMessageFromHadesMessage(Message msg){
		SOAPMessage message = null;
		try {
			MessageFactory mf = MessageFactory.newInstance();
		    message = mf.createMessage();
			SOAPBody body = message.getSOAPBody();
			JAXBContext context = JAXBContext.newInstance(Message.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			m.marshal(msg, body);
			message.saveChanges();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;

	}
}
