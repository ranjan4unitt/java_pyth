package com.hades.factory;

import javax.xml.bind.JAXBElement;

import org.ggf.ns.nmwg.base._2.Data;
import org.ggf.ns.nmwg.base._2.Message;
import org.ggf.ns.nmwg.base._2.Metadata;
import org.ggf.ns.nmwg.base._2.ObjectFactory;
import org.ggf.ns.nmwg.base._2.Parameter;
import org.ggf.ns.nmwg.ops.select._2.Parameters;
import org.ggf.ns.nmwg.tools.hades.Subject;
import org.ggf.ns.nmwg.topology._2.Dst;
import org.ggf.ns.nmwg.topology._2.EndPointPair;
import org.ggf.ns.nmwg.topology._2.Src;

public class MessageFactory {
	public static Message createMetaDataKeyRequest(String source, String destination,
			String startTime, String endTime){
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
		Src src = new Src();
		src.setType("IFName");
		src.setValue(source);
		Dst dst = new Dst();
		dst.setType("IFName");
		dst.setValue(destination);
		ep.setSrc(src);
		ep.setDst(dst);
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
		param1.setContent(startTime);

		Parameter param2 = new Parameter();
		param2.setName("endTime");
		param2.setContent(endTime);

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
		return msg;
	}
}
