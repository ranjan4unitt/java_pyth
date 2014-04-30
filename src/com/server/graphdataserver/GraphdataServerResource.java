package com.server.graphdataserver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.graph.DataGenerator;
import com.graph.GraphUtils;
import com.hades.data.Packet;

public class GraphdataServerResource extends ServerResource {
	String DATA_DIRECTORY = "/home/ranjan/Documents/charts/generated_data/timeseries/";
	String FILENAME_PREFIX = "section_";
	String DATA_DICTIONARY = "/home/ranjan/Documents/charts/generated_data/timeseries/dictionary.txt";
	@Get
    public Representation randomName() throws Exception {
        // Create a JSON object structure similar to a map
		String source = (String) getRequest().getAttributes().get("source");
		String destination = (String) getRequest().getAttributes().get("destination");
		String numMins = (String) getRequest().getAttributes().get("numMins");
		
		Long numOfMins = Long.parseLong(numMins);
		Long currenEpochTime = System.currentTimeMillis() / 1000;
		Long startTime = currenEpochTime - numOfMins * 60;
		
		List<Packet> pkts = DataGenerator.getHadesData(source, destination, startTime, currenEpochTime);
		System.out.println("source = " + source);
		System.out.println("destination = " + destination);
		System.out.println("number of packets = " + pkts.size());
		DataGenerator.generateGraphData(GraphUtils.fromPacketToTimeSeriesRowData(pkts),
				DATA_DIRECTORY, FILENAME_PREFIX, DATA_DICTIONARY, "Delay (in ms)");
        String str = new String("{\"status\":\"done\"}"); 
        		//new String("{  \"cols\": [{\"id\": \"A\", \"label\": \"Year\", \"type\": \"date\"},         {\"id\": \"B\", \"label\": \"Sales\", \"type\": \"number\"}],  \"rows\": [{\"c\":[{\"v\": new Date(2004), \"f\": \"2004\"}, {\"v\": 1000, \"f\": \"1000\"}, {\"v\": 400}]}" + "]}");
        		 //,         {c:[{v: new Date(2005), f: '2005'}, {v: 1170, f: '1170'}, {v: 460}]},         {c:[{v: new Date(2006), f: '2006'}, {v: 660, f: '660'}, {v: 1120}]}        ]}");
        
        return new JsonRepresentation(str);
    }

    @Put
    public void store(JsonRepresentation mailRep) throws JSONException {
        // Parse the JSON representation to get the mail properties
        JSONObject mailElt = mailRep.getJsonObject();

        // Output the JSON element values
        System.out.println("Status: " + mailElt.getString("status"));
        System.out.println("Subject: " + mailElt.getString("subject"));
        System.out.println("Content: " + mailElt.getString("content"));
        System.out.println("Account URI: " + mailElt.getString("accountRef"));
    }
}
