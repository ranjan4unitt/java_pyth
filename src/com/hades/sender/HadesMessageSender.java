package com.hades.sender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.ggf.ns.nmwg.base._2.Message;

public class HadesMessageSender {
	private static Logger logger=Logger.getLogger("HadesMessageSender");
	public Message send(String urlString, SOAPMessage msg){
		Message response = null;
		try {
			URL url = new URL(urlString);
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			msg.writeTo(b);
			HttpURLConnection rc = (HttpURLConnection) url.openConnection();
			// System.out.println("Connection opened " + rc );
			rc.setRequestMethod("POST");
			rc.setDoOutput(true);
			rc.setDoInput(true);
			rc.setRequestProperty("Content-Type",
					"text/xml; charset=ISO-8859-1");
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
			//System.out.println(sb.toString());
			read.close();
			rc.disconnect();
			String responseStr = sb
					.toString()
					.replaceAll(
							"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body>",
							"")
					.replaceAll("</SOAP-ENV:Body></SOAP-ENV:Envelope>", "");
			JAXBContext context = JAXBContext.newInstance(Message.class);
			Unmarshaller um = context.createUnmarshaller();
			response = (Message) um.unmarshal(new StringReader(
					responseStr));
			logger.info(sb.toString());

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
}
