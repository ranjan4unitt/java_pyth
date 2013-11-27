package com.hades.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.ggf.ns.nmwg.base._2.Data;
import org.ggf.ns.nmwg.base._2.Message;
import org.ggf.ns.nmwg.tools.owd.raw.Datum;

import com.hades.data.Packet;
import com.hades.factory.MessageFactory;
import com.hades.sender.HadesMessageSender;
import com.hades.utils.MessageCreationUtils;

public class DataServiceImpl implements DataService {
	private static final String URL = " http://wizbit.rrze.uni-erlangen.de:8090/services/MA/HADES/GEANT";
	//private static final BigDecimal delayThreshold = new BigDecimal(.00001);
	private static Logger logger=Logger.getLogger("DataServiceImpl");
	@Override
	public List<Packet> getDataPackets(String source, String destination,
			String startTime, String endTime) {
		Message metaDataKeyRequest = MessageFactory.createMetaDataKeyRequest(source, destination, startTime, endTime);
		SOAPMessage soap = MessageCreationUtils.createSoapMessageFromHadesMessage(metaDataKeyRequest);
		HadesMessageSender sender = new HadesMessageSender();
		Message response = sender.send(URL, soap);
		List<Data> data = response.getData();
		logger.info("no. of data fields = " + data.size());
		List<Datum> datums = null;
		if(data != null && data.get(0) != null){
			for(int i = 0; i < data.size() ; i++)
			datums = data.get(i).getDatum();
		}
		List<Packet> packets = new ArrayList<Packet>();
		for (Datum d : datums) {
			Packet p = new Packet(d.getRecvtimeSec(), d.getSenttimeSec(), d.getSeqnr(), d.getRecvtimeNsec(), d.getSenttimeNsec());
			packets.add(p);
		}
//		Collections.sort(packets, new Comparator<Packet>(){
//			@Override
//			public int compare(Packet o1, Packet o2) {
//				return o1.getSeqnr().compareTo(o2.getSeqnr());
//			}
//			});
//		for(int i = 1; i < packets.size(); i++){
//			if(packets.get(i).getDelay().subtract(packets.get(i-1).getDelay()).doubleValue() < delayThreshold.doubleValue()){
//				packets.get(i).setDelay(packets.get(i-1).getDelay());
//			}
//		}
		
		return packets;
	}

}
