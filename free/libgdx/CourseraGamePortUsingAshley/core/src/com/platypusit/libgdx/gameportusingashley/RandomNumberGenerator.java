package com.platypusit.libgdx.gameportusingashley;

import java.util.Random;

/**
 * Provides a single generator for random numbers throughout the game.
 *
 * Created by alfergon on 18/01/17.
 */
public class RandomNumberGenerator {

    private static final Random RAND = new Random();

    /**
     * Returns a non negative random number less than maxValue (exclusive)
     * @param maxValue the exclusive max value
     * @return the random number
     */
    public static int next(int maxValue) {
        return RAND.nextInt(maxValue);
    }

    /**
     * Returns a non negative random number less than maxValue (exclusive)
     * @param minValue the inclusive min value
     * @param maxValue the exclusive max value
     * @return the random number
     */
    public static int next(int minValue, int maxValue) {
        return minValue +  RAND.nextInt(maxValue - minValue);
    }

    /**
     * Returns a non negative random number less than maxValue (exclusive)
     * @param maxValue the exclusive max value
     * @return the random number
     */
    public static float nextFloat(float maxValue) {
        return RAND.nextFloat() * maxValue;
    }

    /**
     * Returns a non negative random number less than maxValue (exclusive)
     * @param minValue the inclusive min value
     * @param maxValue the exclusive max value
     * @return the random number
     */
    public static float nextFloat(float minValue, float maxValue) {
        return minValue + (RAND.nextFloat() * (maxValue - minValue));
    }
}
