package ru.gloomyfolken.tcn2obj.json;

import java.io.File;
import java.io.FileInputStream;

import ru.gloomyfolken.tcn2obj.ModelFormatException;
import ru.gloomyfolken.tcn2obj.json.helpers.JsonHelper;

public class JsonModel
{
    public JsonJsonModel model;
    private String filename;

    public JsonModel(File file) throws ModelFormatException
    {
        this.filename = file.getName();
        loadModel(file);
    }

    private void loadModel(File file) throws ModelFormatException
    {
        try
        {
            FileInputStream stream = new FileInputStream(file);
            model = JsonHelper.parseJsonModel(stream);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new ModelFormatException("Model " + filename + " has something wrong");
        }
    }
}
