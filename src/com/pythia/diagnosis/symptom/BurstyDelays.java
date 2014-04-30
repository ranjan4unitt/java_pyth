package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.Utilization;
import com.pythia.diagnosis.checks.UtilizationCheckResult;

public class BurstyDelays {
	public static SymptomResult exists(DetectionResult event){
		UtilizationCheckResult util = Utilization.check(event);
		
		if(util.isBursty == 1) return SymptomResult.TRUE;
		if(util.isBursty == 0) return SymptomResult.FALSE;
		return SymptomResult.UNKNOWN;
	}
}
