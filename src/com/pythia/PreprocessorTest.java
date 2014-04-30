package com.pythia;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.hades.data.Packet;
import com.pythia.dataservice.HadesDataService;

public class PreprocessorTest {
	
	 private static Logger logger=Logger.getLogger("Preprocessor");
	public static void main(String[] args) throws IOException {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Enter the source end point:: ");
//		String source = scanner.nextLine();
//		System.out.println("Enter the destination end point:: ");
//		String destination = scanner.nextLine();
//		System.out.println("Enter the num of minutes to probe :: ");
//		int numOfMins = scanner.nextInt();
//		scanner.nextLine();
//		scanner.close();
		
//		long currenEpochTime = System.currentTimeMillis()/1000;
//		long startTimeMillis = currenEpochTime - numOfMins * 60  ;
//		String end =  ""+currenEpochTime+"";
//		String start = ""+startTimeMillis+"";
		HadesDataService ds = new HadesDataService();
		List<Packet> pkts = ds.getDataPackets("Athens_GRNET", "Prague_GEANT", "1385064455", "1385068055"); 
		
		//"Budapest_GEANT", "Budapest_HUNGARNET"
//		logger.info("source = " + source + " detination = " + destination);
//		logger.info("start time of probe " + start);
//		logger.info("end time of probe " + end);
//		logger.info("number of pkts " + pkts.size());
//		for (Packet packet : pkts) {
//			logger.info("seq = " + packet.getSeqnr() + " send time "+ packet.getSendTime() +" delay = " + packet.getDelay());
//		}
		
		PythiaInputFileCreator.writePacketsToFile(pkts, "/home/ranjan/pythia_test_data/param_.1millisec/test.txt");
	}
	
}
