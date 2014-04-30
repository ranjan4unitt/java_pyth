package com.pythia.diagnosis;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.LevelShift;
import com.pythia.diagnosis.symptom.BurstyDelays;
import com.pythia.diagnosis.symptom.HighDelay;
import com.pythia.diagnosis.symptom.HighUtil;
import com.pythia.diagnosis.symptom.LargeTriangle;
import com.pythia.diagnosis.symptom.SymptomResult;
import com.pythia.diagnosis.symptom.UnipointPeaks;

public class Diagnoser {
	public void diagnose(DetectionResult event){
		//StringBuilder sb = new StringBuilder();
		if(event.hasDelay() && LargeTriangle.exists(event).equals(SymptomResult.TRUE))

			System.out.println("Context switch");
		if(event.hasDelay() && UnipointPeaks.exists(event).equals(SymptomResult.FALSE)
				&& LargeTriangle.exists(event).equals(SymptomResult.FALSE)
				&& event.hasLoss() && LevelShift.check(event)
				&& HighDelay.exists(event).equals(SymptomResult.FALSE)
				&& HighUtil.exists(event).equals(SymptomResult.TRUE)
				&& BurstyDelays.exists(event).equals(SymptomResult.TRUE))
			System.out.println("Congestion bursty");
	}
}

/*
 *#!perl -w
use strict;

require 'symptomdiag.pl';

sub diagnosisTree {
my $diagstr = "";

if(DelayExist() == 1) {
if(LargeTriangle() == 1) {
$diagstr .= "ContextSwitch<BR>";
}
if(UnipointPeaks() == 2) {
if(LargeTriangle() == 2) {
if(LossExist() == 2) {
if(DelayLevelShift() == 2) {
if(HighDelayIQR() == 1) {
$diagstr .= "LargeBuffer<BR>";
}
}
}
if(DelayLevelShift() == 2) {
if(HighDelayIQR() == 2) {
if(HighUtil() == 1) {
if(BurstyDelays() == 1) {
$diagstr .= "CongestionBursty<BR>";
}
if(BurstyDelays() == 2) {
$diagstr .= "CongestionOverload<BR>";
}
}
}
}
if(DelayLevelShift() == 1) {
$diagstr .= "RouteNTPChange<BR>";
}
if(LossExist() == 1) {
if(DelayLevelShift() == 2) {
if(HighDelayIQR() == 1) {
if(DelayLossCorr() == 1) {
$diagstr .= "LargeBuffer<BR>";
}
}
if(DelayLossCorr() == 1) {
if(LowDelayIQR() == 1) {
$diagstr .= "SmallBuffer<BR>";
}
}
}
}
}
}
if(UnipointPeaks() == 1) {
$diagstr .= "EndHostNoise<BR>";
}
}
if(LossExist() == 1) {
if(DelayLossCorr() == 1) {
$diagstr .= "DelayCorrelatedLoss<BR>";
}
if(DelayLossCorr() == 2) {
$diagstr .= "RandomLoss<BR>";
}
if(LossEventSmallDur() == 1) {
$diagstr .= "ShortOutage<BR>";
}
}
if(ReorderShift() == 1) {
if(ReorderExist() == 1) {
$diagstr .= "ReorderUnstable<BR>";
}
}
if(ReorderShift() == 2) {
if(ReorderExist() == 1) {
$diagstr .= "ReorderPersistent<BR>";
}
}
$diagstr = "Unknown<BR>" if $diagstr =~ /^$/;
print $diagstr;
return $diagstr;
};

1; 
 * */
