package com.hades;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;

import org.ggf.ns.nmwg.base._2.Data;
import org.ggf.ns.nmwg.base._2.Message;
import org.ggf.ns.nmwg.base._2.Metadata;
import org.ggf.ns.nmwg.base._2.ObjectFactory;
import org.ggf.ns.nmwg.base._2.Parameter;
import org.ggf.ns.nmwg.ops.select._2.Parameters;
import org.ggf.ns.nmwg.tools.hades.Subject;
import org.ggf.ns.nmwg.topology._2.EndPointPair;

public class TestRequestCreation {

	public static void main(String[] args) {

		Message msg = new Message();
		msg.setId("id1");
		msg.setType("MetadataKeyRequest");

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<String> evntType1 = objectFactory
				.createEventType("http://ggf.org/ns/nmwg/tools/owd/raw/");

		Metadata md1 = objectFactory.createMetadata();
		md1.setId("meta1");

		Subject sub = new Subject();
		sub.setId("subject1");

		EndPointPair ep = new EndPointPair();
		sub.setEndPointPair(ep);

		md1.getContent().add(evntType1);
		md1.getContent().add(sub);

		Metadata md2 = new Metadata();
		md2.setId("meta2");

		org.ggf.ns.nmwg.ops.select._2.Subject sub2 = new org.ggf.ns.nmwg.ops.select._2.Subject();
		sub2.setId("subject2");
		sub2.setMetadataIdRef("meta1");

		Parameters params = new Parameters();
		params.setId("param1");
		Parameter param1 = new Parameter();
		param1.setName("startTime");
		param1.setContent("1384035156");

		Parameter param2 = new Parameter();
		param2.setName("endTime");
		param2.setContent("1384035756");

		params.getParameter().add(param1);
		params.getParameter().add(param2);

		JAXBElement<String> evntType2 = objectFactory
				.createEventType("http://ggf.org/ns/nmwg/ops/select/");

		md2.getContent().add(sub2);
		md2.getContent().add(evntType2);
		md2.getContent().add(params);

		Data dt = new Data();
		dt.setId("data1");
		dt.setMetadataIdRef("meta2");

		msg.getMetadata().add(md1);
		msg.getMetadata().add(md2);
		msg.getData().add(dt);

		try {

			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage message = mf.createMessage();
			SOAPBody body = message.getSOAPBody();

			JAXBContext context = JAXBContext.newInstance(Message.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			m.marshal(msg, body);
			message.saveChanges();
			// m.marshal(msg, System.out);
			message.writeTo(System.out);

			ByteArrayOutputStream b = new ByteArrayOutputStream();
			message.writeTo(b);
			URL url = new URL(
					"http://wizbit.rrze.uni-erlangen.de:8090/services/MA/HADES/GEANT");
			HttpURLConnection rc = (HttpURLConnection) url.openConnection();
			// System.out.println("Connection opened " + rc );
			rc.setRequestMethod("POST");
			rc.setDoOutput(true);
			rc.setDoInput(true);
			rc.setRequestProperty("Content-Type",
					"text/xml; charset=ISO-8859-1");
			// String reqStr = reqT.getRequest(); // the filled out template of
			// SOAP message
			int len = b.size();
			rc.setRequestProperty("Content-Length", Integer.toString(len));
			rc.setRequestProperty("SOAPAction", "");
			rc.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					rc.getOutputStream());
			out.write(b.toString(), 0, len);
			out.flush();
			System.out.println("Request sent, reading response ");
			InputStreamReader read = new InputStreamReader(rc.getInputStream());
			// dumpHeaders( rc, System.out );
			// note that Content-Length is available at this point
			StringBuilder sb = new StringBuilder();
			int ch = read.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = read.read();
			}
			System.out.println(sb.toString());
			read.close();
			rc.disconnect();
			String responseStr = sb
					.toString()
					.replaceAll(
							"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body>",
							"")
					.replaceAll("</SOAP-ENV:Body></SOAP-ENV:Envelope>", "");
			javax.xml.bind.Unmarshaller um = context.createUnmarshaller();
			Message response = (Message) um.unmarshal(new StringReader(
					responseStr));

			System.out.println("done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
