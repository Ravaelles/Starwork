package starwork.utils;

import java.util.HashMap;
import java.util.Map;

public class ObjectAmount<T> {

    private HashMap<T, Double> objects = new HashMap<>();
    private boolean removeObjectWhenAmountReachesZero = true;

    // --------------------------------------------------------------------

    public ObjectAmount() {
    }

    // --------------------------------------------------------------------

    /**
     *     */
    public void addAmount(T object, double unitsToAdd) {
        if (unitsToAdd < 0) {
        	error("Trying to add objects, but value is <0 (" + unitsToAdd + ")");
            return;
        }

        double newUnits = unitsToAdd + (objects.containsKey(object) ? objects.get(object) : 0);
//        if (newUnits > MAX_UNITS) {
//            newUnits = MAX_UNITS;
//        }

        objects.put(object, newUnits);
    }

//    /**
//     *     */
//    public void addTExtraValue(T object, double extraValue) {
//        if (extraValues == null) {
//            extraValues = new HashMap<>();
//        }
//        extraValues.put(object, extraValue);
//    }
//
//    /**
//     *     */
//    public void addTExtraProperty(T object, boolean booleanValue) {
//        if (extraProperties == null) {
//            extraProperties = new HashMap<>();
//        }
//        extraProperties.put(object, booleanValue);
//    }

    /**
     *     */
    public Double getAmountOf(T object) {
        return objects.containsKey(object) ? objects.get(object) : 0;
    }

//    /**
//     *     */
//    public double getExtraValueOf(T object) {
//        return extraValues.get(object);
//    }
//
//    /**
//     *     */
//    public boolean getExtraPropertyOf(T object) {
//        return extraProperties.get(object);
//    }

    /**
     *     */
    public void removeAmount(T object, double unitsToRemove) {
        if (unitsToRemove < 0) {
            error("Trying to remove objects, but value is <0 (" + unitsToRemove + ")");
        }

        double currentUnits = objects.containsKey(object) ? objects.get(object) : 0;
        if (currentUnits - unitsToRemove < 0.01) {
            if (removeObjectWhenAmountReachesZero) {
                objects.remove(object);
//                if (extraValues != null) {
//                    extraValues.remove(object);
//                }
            }
            else {
                objects.put(object, (double) 0);
//                if (extraValues != null) {
//                    extraValues.put(object, (double) 0);
//                }
            }
        }
        else {
            objects.put(object, currentUnits - unitsToRemove);
        }
    }

    // --------------------------------------------------------------------

//    /**
//     *     */
//    public void sortByExtraValues(boolean ascending) {
//        if (extraValues != null) {
//            extraValues = RUtilities.sortByValue(extraValues, ascending);
//        }
//    }

    /**
     *     */
    public Map<T, Double> getObjects() {
        if (objects != null) {
        return objects;
        }
        else {
            return null;
        }
    }

    /** Sums all units of all objects.
     *     */
    public double calculateTotalAmount() {
        double sum = 0;
        for (Double units : objects.values()) {
            sum += units;
        }
        return sum;
    }

    // --------------------------------------------------------------------

    /**
     *     */
    public boolean isEmpty() {
        return objects.isEmpty();
    }

    /**
     *     */
    public void clear() {
        objects.clear();
//        if (extraValues != null) {
//            extraValues.clear();
//            extraValues = null;
//        }
    }

    @Override
    public String toString() {
        String string = "";
        string += "Set of objects contains " + objects.keySet().size() + " different objects, with "
                + calculateTotalAmount() + " total units.\n";
        for (T object : objects.keySet()) {
            string += "   " + object.toString() + ":  " + objects.get(object) + "\n";
        }
        return string;
    }

    // --------------------------------------------------------------------

//    /**
//     *     */
//    public Map<T, Double> getExtraValues() {
//        return extraValues;
//    }

    public void setRemoveObjectWhenAmountReachesZero(boolean removeObjectWhenAmountReachesZero) {
        this.removeObjectWhenAmountReachesZero = removeObjectWhenAmountReachesZero;
    }
    
    // =========================================================
    
    private static void error(String error) {
    	System.err.println("### ObjectAmount class error ###");
    	System.err.println(error);
    }

}
