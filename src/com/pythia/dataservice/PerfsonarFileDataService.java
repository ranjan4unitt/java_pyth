package com.pythia.dataservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hades.data.Packet;

public class PerfsonarFileDataService implements DataService {

	public static void main(String[] args) {
		PerfsonarFileDataService ds = new PerfsonarFileDataService();
		List<Packet> pkts = ds.getDataPackets(
				"/home/ranjan/pythia/pythis_test.txt", null, null, null);
		System.out.println("");
	}

	@Override
	public List<Packet> getDataPackets(String source, String destination,
			String startTime, String endTime) {
		List<Packet> toReturn = new ArrayList<>();
		try {
			Scanner scn = new Scanner(new File(source));
			while (scn.hasNextLine()) {
				if (scn.nextLine().contains("seq_no"))
					toReturn.add(parseDelayLine(scn.nextLine()));
			}
			scn.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to read the data file at "
					+ source);
		}
		return toReturn;
	}

	private Packet parseDelayLine(String nextLine) {

		if (nextLine.contains("LOST"))
			return null;
		String[] dataValues = nextLine.split(" ");

		BigInteger seqNr = new BigInteger(dataValues[0].split("=")[1]);
		double delay = (new BigDecimal(dataValues[1].split("=")[1]))
				.doubleValue();
		BigDecimal sendTimeInsec = new BigDecimal(dataValues[6].split("=")[1]);
		BigDecimal recvTimeInSec = new BigDecimal(dataValues[7].split("=")[1]);

		BigInteger sendTime = sendTimeInsec.toBigInteger();
		Integer nanosecs = (int) ((sendTimeInsec.doubleValue() - sendTimeInsec
				.intValue()) * 1000000);
		BigInteger sendTimeNsec = new BigInteger(nanosecs.toString());

		BigInteger recvTime = recvTimeInSec.toBigInteger();
		Integer recvNano = (int) (recvTimeInSec.doubleValue() - recvTimeInSec
				.intValue() * 100000);
		BigInteger recvTimeNsec = new BigInteger(recvNano.toString());

		return new Packet(recvTime, sendTime, seqNr, recvTimeNsec,
				sendTimeNsec, delay);
	}

}
