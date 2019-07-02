package de.circuitco.pushnotifications;

/**
 * Created by alex on 3/5/2018.
 */

public class Test {
    static double distanceInKm(double kmDistance) {
        return kmDistance * 0.6214;
    }

    static double insurance_cost(double buildingCost) {
        return buildingCost * 0.8;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Invalid args");
            return;
        }
        // First question
        System.out.println(""+distanceInKm(Double.parseDouble(args[0])));
        // Second question
        System.out.println(""+insurance_cost(Double.parseDouble(args[0])));
    }
}
