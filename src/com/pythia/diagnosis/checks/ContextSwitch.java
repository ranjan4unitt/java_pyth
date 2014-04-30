package com.pythia.diagnosis.checks;

import java.util.List;

import com.hades.data.Packet;
import com.pythia.detector.DetectionResult;

public class ContextSwitch  {
	private static final int CSD_INCREASE_THRESHOLD = 300;
	private static final int CSD_INCREASE_THRESHOLD_LOW = 100;
	
	public static ContextSwitchCheckResult check(DetectionResult event) {
		
		ContextSwitchCheckResult ctx = contextSwitch(event, false, CSD_INCREASE_THRESHOLD);
		if(ctx == null)
			ctx = contextSwitch(event, true, CSD_INCREASE_THRESHOLD_LOW);
		
		return ctx;
	}
	private static ContextSwitchCheckResult contextSwitch(DetectionResult event, boolean low, int threshold) {
		List<Packet> window = event.getWindow();
		if(window.size() == 0)
			return null;
		int numOfCxtSwitches = 0;
		int numOfPktsInCS = 0; // CS = context switch
		boolean startFlag = false;
		boolean endFlag = false;
		int indexOfLastPktInCS = -1;
		double startTimeOfCS = 0;
		double endTimeOfCS = 0;
		for(int i=1; i<window.size(); ++i){
			Packet packet = window.get(i);
			
			if(low && startFlag && packet.getDelay() > window.get(i-1).getDelay()){
				break;
			}
			if(packet.getDelay() - window.get(i-1).getDelay() > threshold){
				startFlag = true;
				numOfCxtSwitches++;
				startTimeOfCS = (packet.getSendTimeMilli() + packet.getDelay()) / 1000; 
				numOfPktsInCS = 0;
			}
			if(startFlag && !endFlag && packet.getDelay() < 1.2 * event.getDelay().getMode()){
				endFlag = true;
				endTimeOfCS = (packet.getSendTimeMilli() + window.get(i-1).getDelay()) / 1000;
				indexOfLastPktInCS = i-1;
			}
			if(startFlag && !endFlag)
				numOfPktsInCS++;
		}
		if(indexOfLastPktInCS <= 0 || endTimeOfCS - startTimeOfCS < 0 || numOfPktsInCS == 0)
			return null;
		boolean CSFound = false;
		if(low && endFlag) CSFound = true;
		else if(numOfCxtSwitches > 0 && numOfCxtSwitches / window.size() < 0.1) CSFound = true;  
		double recvRate = -1;
		double recvRate2 = -1;
		if(CSFound){
			if(startTimeOfCS != endTimeOfCS && endFlag)
				recvRate = Math.floor((numOfPktsInCS * (14 + 28) * .008) / Math.abs(endTimeOfCS - startTimeOfCS));
			numOfPktsInCS--;
			endTimeOfCS = (window.get(indexOfLastPktInCS-1).getSendTimeMilli() + window.get(indexOfLastPktInCS-1).getDelay())/100;
			
			if(startTimeOfCS != endTimeOfCS &&  endFlag && numOfPktsInCS > 2)
				recvRate2 = Math.floor((numOfPktsInCS * (14 + 28) * .008) / Math.abs(endTimeOfCS - startTimeOfCS));
			
			recvRate = Math.max(recvRate, recvRate2);
			
			ContextSwitchCheckResult toRet = new ContextSwitchCheckResult();
			toRet.hasContextSwitch = true;
			toRet.recvRate = recvRate;
			
			return toRet;
		}
		
		return null;

	}



}
