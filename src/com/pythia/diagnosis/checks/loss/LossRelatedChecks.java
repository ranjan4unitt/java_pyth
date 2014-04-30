package com.pythia.diagnosis.checks.loss;

import java.util.Map;
import java.util.Set;

import com.pythia.detector.DetectionResult;

public class LossRelatedChecks {
	private static final double SHORT_OUTAGE_GAP = 1;

	public static int checkShortOutage(DetectionResult event){
		if(event.getLoss().getLostPktsCount() == 0) return -1;
		
		Set<Integer> lostSeqs = event.getLoss().getLostSeqTimeStamp().keySet();
		Integer prevSeq = -1;
		Integer currSeq = -1;
		for (Integer seq : lostSeqs) {
			currSeq = seq;
			if(prevSeq != -1 && seq - prevSeq > 1){
				if(checkEvent(prevSeq+1, seq-1, event.getLoss().getLostSeqTimeStamp()) == 1)
					return 1;
			}
			else prevSeq = seq;
		}
		return checkEvent(prevSeq+1, currSeq-1, event.getLoss().getLostSeqTimeStamp()) ;
	}

	private static int checkEvent(Integer prevSeq, Integer currSeq,
			Map<Integer, Double> lostSeqTimeStamp) {
		if(lostSeqTimeStamp.containsKey(prevSeq) && lostSeqTimeStamp.containsKey(currSeq)){
			if(lostSeqTimeStamp.get(currSeq) - lostSeqTimeStamp.get(prevSeq) > SHORT_OUTAGE_GAP ) 
				return 1;
		}
		return 0;
	}
	
public static int lossDelayCorrelation(DetectionResult event){
		return 0;
	}
	
	/*
	 * my $ref = shift;
	my $tref = shift;
	my $seqref = shift;
	my $lseqref = shift;
	my $prevbaseline = shift;
	my $seq_d_ref = shift;
	my $seq_ts_ref = shift;

	my $n = @$ref;
	my %seq_d = %$seq_d_ref;
	my %seq_ts = %$seq_ts_ref;

	if(!defined %$seq_d_ref or !defined %$seq_d_ref)
	{
		%seq_d = ();
		%seq_ts = ();
		for(my $c = 0; $c < $n; $c++)
		{
			$seq_d{$seqref->[$c]} = $ref->[$c];
			$seq_ts{$seqref->[$c]} = $tref->[$c];
		}
	}

	my $nloss = scalar(keys %$lseqref);
	if($nloss == 0) { print STDERR "loss-rate: no losses\n"; return (-1, \%seq_d, \%seq_ts); }

	my $nonrndfrac = 0;
	my $levelshiftfrac = 0;
	foreach my $seq (keys %$lseqref)
	{
		my $ndfrac = 0;
		my $ntot = 0;
		my @halfarr = (); my $halfmed = -1;
		my $cneigh = 0; my $curseq = $seq-1;
		while($cneigh < 5 and $curseq >= $seqref->[0])
		{
			if(exists $seq_d{$curseq})
			{
				$ndfrac++ if $seq_d{$curseq} > $prevbaseline + 1; #ms
				$ntot++;
				push(@halfarr, $seq_d{$curseq});
				$cneigh++;
			}
			$curseq--;
		}
		$halfmed = median(\@halfarr);
		@halfarr = ();
		$cneigh = 0; $curseq = $seq+1;
		while($cneigh < 5 and $curseq <= $seqref->[$n-1])
		{
			if(exists $seq_d{$curseq})
			{
				$ndfrac++ if $seq_d{$curseq} > $prevbaseline + 1; #ms
				$ntot++;
				push(@halfarr, $seq_d{$curseq});
				$cneigh++;
			}
			$curseq++;
		}
=pod
		# count # pts in n-hood > baseline
		for(my $s = $seq - 5; $s <= $seq + 5; $s++)
		{
			if($s == $seq)
			{
				$halfmed = median(\@halfarr);
				@halfarr = ();
				next;
			}
			if(exists $seq_d{$s})
			{
				$ndfrac++ if $seq_d{$s} > $prevbaseline + 1; #ms
				$ntot++;
				push(@halfarr, $seq_d{$s});
			}
		}
=cut
		next if $ntot < 5;
		my $halfmed2 = median(\@halfarr);
		#$levelshiftfrac++ if $halfmed != -1 and $halfmed2 != -1 and 
		#			abs($halfmed2 - $halfmed) > 1; #ms
		#$nonrndfrac++ if $ndfrac/$ntot > $rndNeighborFrac;
		if($halfmed != -1 and $halfmed2 != -1 and 
			abs($halfmed2 - $halfmed) > 1) #ms
		{
			$levelshiftfrac++ 		
		}
		else
		{
			$nonrndfrac++ if $ndfrac/$ntot > $rndNeighborFrac;
		}
print STDERR "Rnd-loss: seq $seq non-neighborhood: $ndfrac / $ntot\n";
print STDERR "Rnd-loss: seq $seq medians: $halfmed $halfmed2 LSfrac: $levelshiftfrac\n" if $halfmed != -1 and $halfmed2 != -1;
	}
print STDERR "Rnd-loss: nonrnd $nonrndfrac LS $levelshiftfrac nloss $nloss\n";

	#TODO: distinguish LSes from congestion
	# 3 : routing + random losses
	# 2 : routing change losses
	# 1 : random losses; 0 : otherwise
	my $ret = (($nloss!=$levelshiftfrac and 
			1-$nonrndfrac/($nloss-$levelshiftfrac) > 0.5) ? 1 : 0) + 
		  (($levelshiftfrac != 0) ? 2 : 0);
	return ($ret, \%seq_d, \%seq_ts);
	 */
}
