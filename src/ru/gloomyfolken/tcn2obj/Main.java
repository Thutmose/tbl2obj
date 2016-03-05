package ru.gloomyfolken.tcn2obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.gloomyfolken.tcn2obj.json.JsonModel;
import ru.gloomyfolken.tcn2obj.obj.ObjModel;
import ru.gloomyfolken.tcn2obj.tbl.TabulaModel;
import ru.gloomyfolken.tcn2obj.tcn.TechneModel;

public class Main
{

    private static final String tcn  = ".tcn";
    private static final String tbl  = ".tbl";
    private static final String obj  = ".obj";
    private static final String json = ".json";

    public static void main(String[] args) throws Exception
    {
        File baseDir = new File(".");

        doTbl(baseDir);
        doTcn(baseDir);
        doJson(baseDir);

        System.out.println("Done!");
    }
    
    private static void doJson(File baseDir) throws Exception
    {
        List<File> files = getFiles(baseDir, json);
        JsonConverter converter = new JsonConverter();

        for (File file : files)
        {
            System.out.println("Processing " + file.getAbsolutePath());

            String filename = file.getName().substring(0, file.getName().length() - obj.length());
            File objFile = new File(file.getParentFile(), filename + ".obj");

            JsonModel model = new JsonModel(file);
            System.out.println(model.toString());
            ObjModel objModel = converter.tcn2obj(model, 0.0625f);

            saveFile(objFile, objModel.toStringList());
        }
    }

    private static void doTcn(File baseDir) throws Exception
    {
        List<File> files = getFiles(baseDir, tcn);
        TcnConverter tcnConverter = new TcnConverter();

        for (File tcnFile : files)
        {
            System.out.println("Processing " + tcnFile.getAbsolutePath());

            String filename = tcnFile.getName().substring(0, tcnFile.getName().length() - obj.length());
            File objFile = new File(tcnFile.getParentFile(), filename + ".obj");

            TechneModel tcnModel = new TechneModel(tcnFile);
            ObjModel objModel = tcnConverter.tcn2obj(tcnModel, 0.0625f);

            saveFile(objFile, objModel.toStringList());
        }
    }

    private static void doTbl(File baseDir) throws Exception
    {
        TblConverter tblConverter = new TblConverter();
        List<File> files = getFiles(baseDir, tbl);
        for (File tblFile : files)
        {
            System.out.println("Processing " + tblFile.getAbsolutePath());

            String filename = tblFile.getName().substring(0, tblFile.getName().length() - obj.length());
            File objFile = new File(tblFile.getParentFile(), filename + ".obj");

            TabulaModel tblModel = new TabulaModel(tblFile);
            ObjModel objModel = tblConverter.tcn2obj(tblModel, 0.0625f);
            saveFile(objFile, objModel.toStringList());
            if (shouldExportXML())
            {
                File xmlFile = new File(tblFile.getParentFile(), filename + ".xml");
                TabulaMetadataExporter metaExp = new TabulaMetadataExporter(tblConverter);
                saveFile(xmlFile, metaExp.getXMLLines());
            }
        }
    }

    private static boolean shouldExportXML()
    {
        File dir = new File(".");
        File config = new File(dir, "tbl2obj.cfg");
        if (config.exists())
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(config));
                String line = reader.readLine();
                reader.close();
                return Boolean.parseBoolean(line.split("=")[1].trim());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            FileWriter writer;
            try
            {
                writer = new FileWriter(config);
                writer.write("outputXML=false");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    private static void saveFile(File file, List<String> lines) throws IOException
    {
        FileWriter writer = new FileWriter(file);
        for (String str : lines)
        {
            writer.write(str);
            writer.write("\n");
        }
        writer.close();
    }

    private static List<File> getFiles(File dir, String postfix) throws IOException
    {
        ArrayList<File> files = new ArrayList<File>();
        File[] filesArray = dir.listFiles();
        if (filesArray != null)
        {
            for (File file : filesArray)
            {
                if (file.isDirectory())
                {
                    files.addAll(getFiles(file, postfix));
                }
                else if (file.getName().endsWith(postfix))
                {
                    files.add(file);
                }
            }
        }
        return files;
    }

}
