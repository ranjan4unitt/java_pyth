package com.pythia.diagnosis.checks;

import com.pythia.detector.DetectionResult;

public class LevelShift {
	public static boolean check(DetectionResult event){
		return false;
	}
	/*
	my $ref = shift;
	my $baseline = shift;
	my $n = @$ref;

	my ($rightsch, $nright1) = searchRight($ref, $baseline);
	my ($leftsch, $nright2) = searchLeft($ref, $baseline);
#print "LS: $nright1 $nright2 $n\n";

	return 0 if $leftsch == 0 and $rightsch == 0;

	my $nright = ($leftsch == 1) ? $nright2 : $nright1;
	my @rightarr = @$ref[($n-$nright)..$n-1];
	my @leftarr = @$ref[0..($n-$nright-1)];

	#check low variance
	my @parr = (0.05, 0.90); #0.9 since the #pts is small
	my ($lowd, $highd) = prctileArr(\@rightarr, \@parr);
#print "LS: leftdiff: ".($highd-$lowd)."\n";
	return 0 if ($highd - $lowd) > $prctileThresh;

	#check low variance
	my ($lowd, $highd) = prctileArr(\@leftarr, \@parr);
#print "LS: rtdiff: ".($highd-$lowd)."\n";
	return 0 if ($highd - $lowd) > $prctileThresh;

	return 1;
	 */
}
