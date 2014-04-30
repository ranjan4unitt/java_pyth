package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.ContextSwitch;
import com.pythia.diagnosis.checks.ContextSwitchCheckResult;

public class LargeTriangle {

	public static SymptomResult exists(DetectionResult event) {
		ContextSwitchCheckResult result = ContextSwitch.check(event);
		if(result != null && result.recvRate >= 1)
			return SymptomResult.TRUE;
		return SymptomResult.FALSE;
	}
	/*
	 * return $cache{LargeTriangle} if exists $cache{LargeTriangle};

	my ($ret, $rate) = contextSwitch($ref, $tref, $prevbaseline);
	if($ret == 1)
	{
		$cache{LargeTriangle} = TRUE;
		$supportVarKey = "CSRate";
		$supportVarVal = $rate;
	}
	else #0 or -1
	{
		$cache{LargeTriangle} = FALSE;
	}
	return $cache{LargeTriangle};
	 */

}
