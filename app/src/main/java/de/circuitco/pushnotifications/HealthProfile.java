package de.circuitco.pushnotifications;

/**
 * Created by alex on 3/5/2018.
 */

public class HealthProfile {
    private String name;
    private int age;
    private double weight;
    private double height;

    public void setName(String _name) {
        name = _name;
    }

    public void setHeight(float ft, float in) {
        height = ft*12;
        height += in;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public double getBMI() {
        return (weight / Math.sqrt(height)) * 703;
    }

    public String getCategory() {
        double bmi = getBMI();
        if (bmi >= 30) {
            return "obsese";
        } else if (bmi >= 25) {
            return "overweight";
        } else if (bmi >= 18.5) {
            return "normal";
        } else {
            return "underweight";
        }
    }

    public int getMaxHR() {
        return 200-age;
    }

    public static int main(String[] args) {
        while (true) {
            HealthProfile profile = new HealthProfile();
            System.out.println("Enter name or X to quit: ");
            profile.setName(System.console().readLine());
            if (profile.getName().equals("X")) {
                break;
            }
            System.out.println("Your age: ");
            profile.setAge(Integer.valueOf(System.console().readLine()));
            System.out.println("Your weight: ");
            profile.setWeight(Double.valueOf(System.console().readLine()));
            System.out.println("Your height - feet: ");
            int ft = Integer.valueOf(System.console().readLine());
            System.out.println("Your height - inches: ");
            int in = Integer.valueOf(System.console().readLine());
            profile.setHeight(ft, in);
            System.out.println();
            profile.printHealthProfile();
        }
        return 0;
    }

    public void printHealthProfile() {
        System.out.println("Health Profile for " + name);
        System.out.println("BMI: " + String.format("%.2f", getBMI()));
        System.out.println("BMI Category: " + getCategory());
        System.out.println("Max heart rate: " + getMaxHR());
    }
}
