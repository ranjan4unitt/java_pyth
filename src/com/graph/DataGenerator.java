package com.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import com.hades.data.Packet;
import com.pythia.dataservice.HadesDataService;

public class DataGenerator {

	// private static PrintWriter dd ;

	public static void main(String[] args) {
		String DATA_DIRECTORY = "/home/ranjan/Documents/charts/generated_data/timeseries/";
		String FILENAME_PREFIX = "section_";
		String DATA_DICTIONARY = "/home/ranjan/Documents/charts/generated_data/dictionary.txt";
		Scanner scanner = new Scanner(System.in);
		// System.out.println("Enter the source end point:: ");
		// String source = scanner.nextLine();
		// System.out.println("Enter the destination end point:: ");
		// String destination = scanner.nextLine();
		System.out.println("Enter the num of minutes to probe :: ");
		int numOfMins = scanner.nextInt();
		scanner.nextLine();
		scanner.close();
		// "Athens_GRNET","Prague_GEANT" 
		//"Budapest_GEANT", "Budapest_HUNGARNET"
		Long currenEpochTime = System.currentTimeMillis() / 1000;
		Long startTime = currenEpochTime - numOfMins * 60;
		List<Packet> pkts = getHadesData("Budapest_GEANT", "Budapest_HUNGARNET" ,
				startTime, currenEpochTime);
		System.out.println("size = " + pkts.size());
		try {
			generateGraphData(GraphUtils.fromPacketToTimeSeriesRowData(pkts),
					DATA_DIRECTORY, FILENAME_PREFIX, DATA_DICTIONARY, "Delay (in ms)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String end = ""+currenEpochTime+"";
		// String start = ""+startTimeMillis+"";

	}
	
	public static List<Packet> getHadesData(String src, String dest, Long startTime, Long endTime){
		HadesDataService ds = new HadesDataService();
		System.out.println("starttime = " + startTime);
		System.out.println("endtime = " + endTime);
		//return ds.getDataPackets(src, dest, startTime.toString(), endTime.toString());
		//1390314369, 
		//"1384035156", "1384035256"
		//"1391178369", "1391179369"
		return ds.getDataPackets(src, dest, "1395496628", "1395496928");
//		return ds.getDataPackets("Budapest_GEANT",
//				 "Budapest_HUNGARNET", "1384035156", "1384035256");
	}

	public static void generateGraphData(List<RowData> table,
			String DATA_DIRECTORY, String FILENAME_PREFIX,
			String DATA_DICTIONARY, String label) throws IOException {
		if(table == null || table.isEmpty())
			return;
		int start = 0 ;
		while (start < table.size()) {
			int end = start + 100 > table.size() ? table.size() : start + 100;
			String filename = createGraphDataFile(table.subList(start, end), table.get(end - 1).getCol1Value(),
					DATA_DIRECTORY, FILENAME_PREFIX, label);
			addToDataDictionary(DATA_DICTIONARY, filename);
			start += 100;
		}
	}

	private static String createGraphDataFile(List<RowData> pkts, String id,
			String DATA_DIRECTORY, String FILENAME_PREFIX, String label) {
		String filename = generateFilename(id, DATA_DIRECTORY,
				FILENAME_PREFIX);
		File file = new File(filename);
		// if file doesnt exists, then create it
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter fl = new PrintWriter(new FileWriter(
					file.getAbsoluteFile()));
			fl.println(createJson(pkts, label));
			fl.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filename;
	}

	private static void addToDataDictionary(String DATA_DICTIONARY,
			String filename) throws IOException {
		File dictionary = new File(DATA_DICTIONARY);
		if (!dictionary.exists()) {
			System.out.println("file exists. not creating");
			dictionary.createNewFile();
		}
		PrintWriter dd = new PrintWriter(new FileWriter(dictionary, true));
		dd.println(filename);
		// dd.println(filename);
		dd.close();
	}

	private static String generateFilename(String fileId, String DATA_DIRECTORY,
			String FILENAME_PREFIX) {
		return DATA_DIRECTORY + FILENAME_PREFIX + (fileId + "");
	}

	private static String createJson(List<RowData> table, String label) {
		return GraphUtils.toJsonStringForGraph(table, label);
	}

}
