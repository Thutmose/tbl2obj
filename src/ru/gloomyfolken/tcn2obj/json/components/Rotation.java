package ru.gloomyfolken.tcn2obj.json.components;

import java.util.Arrays;

public class Rotation
{
    double[] origin;
    private String   axis;
    private double   angle;
    
    public String toString()
    {
        return getAxis()+" "+getAngle()+" "+Arrays.toString(origin);
    }

    public String getAxis()
    {
        return axis;
    }

    public double getAngle()
    {
        return angle;
    }
    
    public float[] getOrigin()
    {
        float[] origin = new float[3];
        for (int i = 0; i < 3; i++)
            origin[i] = (float) this.origin[i];
        return origin;
    }
}
