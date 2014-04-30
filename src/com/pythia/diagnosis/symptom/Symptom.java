package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;

public interface Symptom {
	SymptomResult exists(DetectionResult event);
}
