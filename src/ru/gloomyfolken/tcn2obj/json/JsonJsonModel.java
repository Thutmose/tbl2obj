package ru.gloomyfolken.tcn2obj.json;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import ru.gloomyfolken.tcn2obj.json.components.Box;

public class JsonJsonModel
{
    ArrayList<Box> elements = Lists.newArrayList();

    public ArrayList<Box> getElements()
    {
        return elements;
    }

}
