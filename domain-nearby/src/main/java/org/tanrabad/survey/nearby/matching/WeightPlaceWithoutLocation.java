package org.tanrabad.survey.nearby.matching;

import org.tanrabad.survey.entity.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import info.debatty.java.stringsimilarity.LongestCommonSubsequence;

public class WeightPlaceWithoutLocation {
    public static Map<UUID, Double> calculate(List<Place> placeWithLocation, List<Place> placeWithoutLocation) {
        Map<UUID, Double> weightedScoreOfPlaceWithoutLocation = new HashMap<>();
        Map<UUID, String> placeWithLo = removeCommonPlaceName(placeWithLocation);
        Map<UUID, String> placeWithoutLo = removeCommonPlaceName(placeWithoutLocation);
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        StringMatchScore stringMatchScore = new LcsMatchScore();
        for (Entry<UUID, String> eachPlaceWithLo : placeWithLo.entrySet()) {
            for (Entry<UUID, String> eachPlaceWithoutLo : placeWithoutLo.entrySet()) {
                String placeWithLocationName = eachPlaceWithLo.getValue();
                int lcsLengthOfPlaceWithoutLocation =
                    lcs.length(placeWithLocationName, eachPlaceWithoutLo.getValue());
                double lcsWeightPercentage = stringMatchScore.calculate(lcsLengthOfPlaceWithoutLocation,
                    placeWithLocationName.length());

                if (weightedScoreOfPlaceWithoutLocation.containsKey(eachPlaceWithoutLo.getKey())) {
                    Double oldWeight = weightedScoreOfPlaceWithoutLocation.get(eachPlaceWithoutLo.getKey());
                    lcsWeightPercentage += oldWeight;
                }
                weightedScoreOfPlaceWithoutLocation.put(eachPlaceWithoutLo.getKey(), lcsWeightPercentage);
            }
        }

        calculateAverageMatchPercentage(placeWithLocation, weightedScoreOfPlaceWithoutLocation);

        return weightedScoreOfPlaceWithoutLocation;
    }

    private static void calculateAverageMatchPercentage(List<Place> placeWithLocation,
            Map<UUID, Double> weightedScoreOfPlaceWithoutLocation) {
        for (Entry<UUID, Double> entry : weightedScoreOfPlaceWithoutLocation.entrySet()) {
            double averageMatchPercentageOfEachPlace = entry.getValue() / placeWithLocation.size();
            entry.setValue(averageMatchPercentageOfEachPlace);
        }
    }

    private static Map<UUID, String> removeCommonPlaceName(List<Place> places) {
        Map<UUID, String> removeCommonPlaceNameMap = new HashMap<>();
        for (Place place : places) {
            removeCommonPlaceNameMap.put(place.getId(), CommonPlaceTypeRemover.remove(place.getName()));
        }
        return removeCommonPlaceNameMap;
    }
}
