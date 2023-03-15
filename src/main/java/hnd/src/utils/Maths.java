package hnd.src.utils;

import org.joml.Vector3f;

/**
 * A utility class for mathematical calculations.
 */
public class Maths {
    /**
     * Converts the given rotation vector from radians to degrees.
     *
     * @param rotation The rotation vector in radians.
     * @return The rotation vector converted to degrees.
     */
    public static Vector3f radiansToDegrees(Vector3f rotation) {
        float radiansToDegrees = (float) 57.295779513082320876798154814105;
        return new Vector3f(rotation.x * radiansToDegrees, rotation.y * radiansToDegrees, rotation.z * radiansToDegrees);
    }

    /**
     * Converts the given rotation vector from degrees to radians.
     *
     * @param rotation The rotation vector in degrees.
     * @return The rotation vector converted to radians.
     */
    public static Vector3f degreesToRadians(Vector3f rotation) {
        float degreesToRadians = (float) 0.01745329251994329576923690768489;
        return new Vector3f(rotation.x * degreesToRadians, rotation.y * degreesToRadians, rotation.z * degreesToRadians);
    }
}
