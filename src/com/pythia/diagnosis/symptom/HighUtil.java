package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.Utilization;
import com.pythia.diagnosis.checks.UtilizationCheckResult;

public class HighUtil {
	private static final double HIGH_UTIL_THRESHOLD = 0.5;
	public static SymptomResult exists(DetectionResult event){
		UtilizationCheckResult util = Utilization.check(event);
		if(util.utilization > HIGH_UTIL_THRESHOLD)
			return SymptomResult.TRUE;
					
		return SymptomResult.FALSE;			
	}
}
