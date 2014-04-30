package com.pythia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hades.data.Packet;
import com.pythia.dataservice.HadesDataService;

public class PythiaInputFileCreator {
	private static final String DATA_DICTIONARY = "/home/ranjan/pythia_test_data/dataDictionary.txt";

	public static void main(String[] args) {
		HadesDataService ds = new HadesDataService();

		String[] endpoints = new String[] {  "Athens_GRNET","Prague_GEANT" 
				//,"Prague_GEANT"
//				, "Athens_GRNET", "Prague_GEANT", "Budapest_GEANT", "Budapest_HUNGARNET"
				, "Geneva_GEANT" ,"Frankfurt_GEANT", "Budapest_GEANT"
				//,
				};
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter no. of intervals to measure");
		int intervals = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter length of intervals in mins");
		int intervalSize = scanner.nextInt();
		scanner.nextLine();
		scanner.close();
		
		long currenEpochTime = System.currentTimeMillis()/1000;
		Long startTime = new Long(currenEpochTime - 5 * 24 * 60 * 60 );
		
		Long endTime = startTime;
		List<PythiaTestParams> params = new ArrayList<PythiaTestParams>();
		try {
			for (String src : endpoints) {
				for (String dest : endpoints) {
					if (src.equals(dest))
						continue;
					String fileName = null;
					Long time = startTime;
					for (int i = 0; i < intervals; i++) {
						List<Packet> pkts = ds.getDataPackets(src, dest,
								time.toString(), new Long(time + intervalSize * 60).toString());
						System.out.println("size = " + pkts.size());
						fileName = "/home/ranjan/pythia_test_data/" + src + dest
								+ time.toString() + ".owp";
						
						params.add(new PythiaTestParams(fileName, time.toString(), new Long(time + intervalSize * 60).toString()));
						time = new Long(time + intervalSize * 60);
						//writePacketsToFile(pkts, fileName);
					}
					endTime = time;
			//		writeToDataDictionary(fileName, startTime.toString(),
				//			endTime.toString());
				}
			}
			
			//executeTestScript(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<Packet> pkts = ds.getDataPackets("Budapest_GEANT",
		// "Budapest_HUNGARNET", "1384035156", "1384035256");
		// for (Packet packet : pkts) {
		// StringBuilder sb = new StringBuilder();
		// sb.append("seq_no=");
		// sb.append(packet.getSeqnr());
		// sb.append(" delay=");
		// sb.append(packet.getDelay().doubleValue() * .000001);
		// sb.append(" ms (sync, err=1.53 ms) sent=");
		// sb.append(packet.getSendTime());
		// sb.append(" recv=");
		// sb.append(packet.getRecvTime());
		// // System.out.println(sb.toString());
		//
		// }
		// seq_no=0 delay=4.963875e+00 ms (sync, err=1.53 ms)
		// sent=1383771934.198319 recv=1383771934.203283

	}

	public static void executeTestScript(List<PythiaTestParams> params) throws IOException, InterruptedException {
		
		for (PythiaTestParams p : params) {
			StringBuilder command = new StringBuilder();
			command.append("/home/ranjan/pythia/test_pythia.sh ");
			command.append(p.getFilename() + " ");
			command.append(p.getStartTime() + " ");
			command.append(p.getEndTime());
					
			Process pr = Runtime.getRuntime().exec(command.toString());
			pr.waitFor();
		}
		
		
	}

	public static void writeToDataDictionary (String filename, String start,
			String end) throws IOException {
		File file = new File(DATA_DICTIONARY);
		if (!file.exists())
			file.createNewFile();

		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("filename = " + filename + " startime = " +  start + " endtime = " + end);
		bw.close();
	}

	public static void writePacketsToFile(List<Packet> pkts, String fileName)
			throws IOException {
		File file = new File(fileName); // "/home/ranjan/pythia_test_data/dataDictionary.txt"
		if (!file.exists())
			file.createNewFile();

		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);

		for (Packet packet : pkts) {
			//System.out.println(packet.getDelay());
			StringBuilder sb = new StringBuilder();
			sb.append("seq_no=");
			sb.append(packet.getSeqnr());
			sb.append(" delay=");
			sb.append(packet.getDelay());
			sb.append(" ms (sync, err=1.53 ms) sent=");
			sb.append(packet.getSendTime());
			sb.append(" recv=");
			sb.append(packet.getRecvTime());

			bw.write(sb.toString());
			bw.write("\n");
		}
		bw.close();

	}

}
