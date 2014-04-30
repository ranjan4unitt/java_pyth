package com.pythia.preprocessor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.hades.data.Packet;

public class Preprocessor {
	
	private static final int WINDOW_RANGE = 5;
	
	public static List<List<Packet>> divideIntoWindows(List<Packet> pkts, int range){
		Packet startPkt = pkts.get(0);
		BigInteger startTime = startPkt.getSendTime();//.subtract(BigInteger.valueOf(100));
		List<List<Packet>> toReturn = new ArrayList<>();
		List<Packet> current = new ArrayList<>();
		current.add(pkts.get(0));
		for(int i=1; i < pkts.size(); i++){
			if (pkts.get(i) != null && pkts.get(i).getSendTime() != null) { // When packets are lost there is no sent time. Hence this check
				if (pkts.get(i).getSendTime().subtract(startTime)
						.compareTo(BigInteger.valueOf(range)) < 0) {
					current.add(pkts.get(i));
				} else {
					toReturn.add(current);
					current = new ArrayList<>();
					current.add(pkts.get(i));
					startTime = pkts.get(i).getSendTime();
				}
			}
		}
		
		return toReturn;
	}
	
	public static List<List<Packet>> divideIntoWindows(List<Packet> pkts){
		return divideIntoWindows(pkts, WINDOW_RANGE);
	}
	
}
