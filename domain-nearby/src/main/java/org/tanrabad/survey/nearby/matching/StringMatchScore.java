package org.tanrabad.survey.nearby.matching;

public interface StringMatchScore {
    double calculate(int lcsLength, int compareStringLength);
}
