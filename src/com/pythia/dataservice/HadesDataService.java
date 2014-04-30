package com.pythia.dataservice;

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

public class HadesDataService implements DataService {
	private static final String URL = "http://62.40.105.148:8090/services/MA/HADES/GEANT" ;//" http://wizbit.rrze.uni-erlangen.de:8090/services/MA/HADES/GEANT";
	private static Logger logger = Logger.getLogger("DataServiceImpl");

	@Override
	public List<Packet> getDataPackets(String source, String destination,
			String startTime, String endTime) {
		Message metaDataKeyRequest = MessageFactory.createMetaDataKeyRequest(
				source, destination, startTime, endTime);
		SOAPMessage soap = MessageCreationUtils
				.createSoapMessageFromHadesMessage(metaDataKeyRequest);
		HadesMessageSender sender = new HadesMessageSender();
		Message response = sender.send(URL, soap);
		List<Data> data = response.getData();
		logger.info("no. of data fields = " + data.size());
		List<Datum> datums = null;
		if (data != null && data.get(0) != null) {
			for (int i = 0; i < data.size(); i++)
				datums = data.get(i).getDatum();
		}
		List<Packet> packets = new ArrayList<Packet>();
		for (Datum d : datums) {
			Packet p = new Packet(d.getRecvtimeSec(), d.getSenttimeSec(),
					d.getSeqnr(), d.getRecvtimeNsec(), d.getSenttimeNsec(),
					null);
			packets.add(p);
		}

		return packets;
	}

}
