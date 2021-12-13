package org.ih.labtest;

public enum LabTestResult {
    POSITIVE,
    NEGATIVE,
    INCONCLUSIVE;

    public static LabTestResult fromString(String value) {
        return LabTestResult.valueOf(value.toUpperCase());
    }
}
