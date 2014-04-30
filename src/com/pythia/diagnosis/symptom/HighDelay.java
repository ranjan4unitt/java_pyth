package com.pythia.diagnosis.symptom;

import java.util.Arrays;
import java.util.List;

import com.hades.data.Packet;
import com.pythia.detector.DetectionResult;
import com.utils.MathUtils;

public class HighDelay   {

	private static final double LOW_PERCENTILE = 0.05;
	private static final double HIGH_PERCENTILE = 0.95;
	private static final double HIGH_BUF = 500;

	public static SymptomResult exists(DetectionResult event) {
		List<Packet> pkts = event.getWindow();
		int lowDelayIdx = MathUtils.percentileElementIdxInArray(pkts.size(), LOW_PERCENTILE );
		int highDelayIdx = MathUtils.percentileElementIdxInArray(pkts.size(), HIGH_PERCENTILE );
		
		double[] delays = new double[pkts.size()];
		for(int i=0; i<pkts.size(); ++i){
			delays[i] = pkts.get(i).getDelay();
		}
		Arrays.sort(delays);
		if(delays[highDelayIdx] - delays[lowDelayIdx] > HIGH_BUF)
			return SymptomResult.TRUE;
		return SymptomResult.FALSE;
	}
/*
 * 
 * return $cache{HighDelayIQR} if exists $cache{HighDelayIQR};

	$cache{HighDelayIQR} = FALSE;
	$cache{LowDelayIQR} = FALSE;

	my @parr = (0.05, 0.95);
	my ($lowd, $highd) = prctileArr($ref, \@parr); #IQR($ref);
	my $iqr = $highd - $lowd;
	if($iqr > $highBuf)
	{
		$cache{HighDelayIQR} = TRUE;
	}
	elsif($iqr < $lowBuf)
	{
		$cache{LowDelayIQR} = TRUE;
	}
	return $cache{HighDelayIQR};
 */

}
