package com.pythia.dataservice;

import java.util.List;

import com.hades.data.Packet;

public interface DataService {
		List<Packet> getDataPackets(String source, String destination, String startTime, String endTime);
}
