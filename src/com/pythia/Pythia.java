package com.pythia;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.graph.DataGenerator;
import com.graph.GraphUtils;
import com.hades.data.Packet;
import com.pythia.dataservice.DataService;
import com.pythia.dataservice.HadesDataService;
import com.pythia.dataservice.PerfsonarFileDataService;
import com.pythia.detector.DetectionResult;
import com.pythia.detector.DetectionUtils;
import com.pythia.diagnosis.Diagnoser;
import com.pythia.preprocessor.Preprocessor;

public class Pythia {
	private static Logger logger = Logger.getLogger("Pythia");
	// private static HadesDataService ds;
	private static DataService ds;
	private static final int MAX_PROBLEM_DURATION = 10; // in seconds

	private static Diagnoser daignoser;

	public static void main(String[] args) {

		/*
		 * ds = new HadesDataService(); List<Packet> pkts =
		 * ds.getDataPackets("Athens_GRNET", "Prague_GEANT", "1385064455",
		 * "1385068055");
		 */
		daignoser = new Diagnoser();
		if (args.length == 2) {
			logger.info("Running pythia on Perfsonar data contained in file "
					+ args[1]);
			runOnPerfsonar(args);
		} else if (args.length == 5) {
			logger.info("Running pythia on Hades data for source : " + args[1]
					+ " target : " + args[2] + " starttime : " + args[3]
					+ " endtime : " + args[4]);
			runOnHades(args);
		} else {
			logger.error("Error in input format");
			logger.error("Pythia can run on 2 data sources Hades and Perfsonar. \n To run on hades data enter input as follows : \n 1 source target starttime endtime\n To run on perfsonar data enter input as follows :\n 2 filename");
			System.exit(1);
		}
	}

	private static void runOnHades(String[] args) {
		ds = new HadesDataService();

		ds = new HadesDataService();
		List<Packet> pkts = ds.getDataPackets(args[1], args[2], args[3],
				args[4]);
		if (pkts.size() != 0)
			runDiagnosis(pkts);
		else
			logger.info("No data returned from the Hades service for this call");

	}

	private static void runDiagnosis(List<Packet> pkts) {
		List<List<Packet>> windows = Preprocessor.divideIntoWindows(pkts);

		DetectionResult toDiagnose = null;
		for (List<Packet> window : windows) {
			// genererateGraphData(window);
			DetectionResult result = new DetectionResult(window);
			if (result.hasDelay() || result.hasLoss() || result.hasReordering()) {
				toDiagnose = DetectionUtils
						.mergeCloseEvents(toDiagnose, result);
			}
			if (toDiagnose != null) { // && toDiagnose.getDuration() >
										// MAX_PROBLEM_DURATION) {
				daignoser.diagnose(toDiagnose);
				toDiagnose = null;
			}
		}
	}

	private static void runOnPerfsonar(String[] args) {
		String filename = args[1];
		File file = new File(filename);
		if (file.exists()) {
			ds = new PerfsonarFileDataService();
			// List<Packet> pkts =
			// ds.getDataPackets("/home/ranjan/pythia/pythis_test.txt", null,
			// null, null);
			List<Packet> pkts = ds.getDataPackets(filename, null, null, null);
			if (pkts.size() != 0)
				runDiagnosis(pkts);
			else
				logger.info("No data in the input file");
		}

	}

	private static void genererateGraphData(List<Packet> window) {
		try {
			DataGenerator
					.generateGraphData(
							GraphUtils.fromPacketToTimeSeriesRowData(window),
							"/home/ranjan/Documents/charts/generated_data/timeseries/",
							"section_",
							"/home/ranjan/Documents/charts/generated_data/timeseries/dictionary.txt",
							"Delay (in ms)");
			List<List<Packet>> divByTime = Preprocessor.divideIntoWindows(
					window, 10);
			for (List<Packet> delays : divByTime) {
				DataGenerator
						.generateGraphData(
								GraphUtils
										.fromPacketToTimeSeriesRowData(delays),
								"/home/ranjan/Documents/charts/generated_data/tenpackets/",
								"section_",
								"/home/ranjan/Documents/charts/generated_data/tenpackets/dictionary.txt",
								"Delay (in ms)");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
