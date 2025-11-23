package main;

public class UtilityTool {


    public static double distanceTo(float x, float y, float targetX, float targetY) {
        return Math.sqrt(Math.pow(targetX-x, 2) + Math.pow(targetY-y, 2));
    }
}
