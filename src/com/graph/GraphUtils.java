package com.graph;

import java.util.ArrayList;
import java.util.List;

import com.hades.data.Packet;

public class GraphUtils {
	public static String toJsonStringForGraph(List<RowData> table, String label ){
		String header = "{  \"cols\": [{\"id\":\"A\", \"label\":\"X\", \"type\":\"number\"},{\"id\":\"B\", \"label\":\""+label+"\", \"type\":\"number\"}],  \"rows\": [";
		StringBuilder json = new StringBuilder();
		for (RowData p : table) {
			json.append("{\"c\":[{\"v\":" + p.col1Value + ", \"f\":\""
					+ p.col1Value + "\"}, {\"v\":" + p.col2Value
					+ ", \"f\":\"" + p.col2Value + "\"}]},");
		}
		json.deleteCharAt(json.length() - 1);
		String footer = "]}";

		return header + json.toString() + footer;

	}

	public static List<RowData> fromPacketToTimeSeriesRowData(List<Packet> pkts) {
		if(pkts.isEmpty())
			return null;
		List<RowData> toReturn = new ArrayList<>();
		double firstVal = pkts.get(0).getSendTimeMilli();
		for (Packet p : pkts) {
			RowData row = new RowData();
			row.col1Value = p.getSendTimeMilli() - firstVal +"";
			row.col2Value = p.getDelay()+"";
			
			toReturn.add(row);
		}
		return toReturn;
	}
}
