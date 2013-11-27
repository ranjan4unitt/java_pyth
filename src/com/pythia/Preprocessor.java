package com.pythia;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.hades.data.Packet;
import com.hades.service.DataServiceImpl;

public class Preprocessor {
	
	 private static Logger logger=Logger.getLogger("Preprocessor");
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the source end point:: ");
		String source = scanner.nextLine();
		System.out.println("Enter the destination end point:: ");
		String destination = scanner.nextLine();
		System.out.println("Enter the num of minutes to probe :: ");
		int numOfMins = scanner.nextInt();
		scanner.nextLine();
		scanner.close();
		
		long currenEpochTime = System.currentTimeMillis()/1000;
		long startTimeMillis = currenEpochTime - numOfMins * 60  ;
		String end =  ""+currenEpochTime+"";
		String start = ""+startTimeMillis+"";
		DataServiceImpl ds = new DataServiceImpl();
		List<Packet> pkts = ds.getDataPackets(source, destination, start, end); 
		//"Budapest_GEANT", "Budapest_HUNGARNET"
		logger.info("source = " + source + " detination = " + destination);
		logger.info("start time of probe " + start);
		logger.info("end time of probe " + end);
		logger.info("number of pkts " + pkts.size());
		for (Packet packet : pkts) {
			logger.info("seq = " + packet.getSeqnr() + " send time "+ packet.getSendTime() +" delay = " + packet.getDelay());
		}
	}
}
