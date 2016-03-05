package ru.gloomyfolken.tcn2obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Sets;

import ru.gloomyfolken.tcn2obj.json.JsonModel;
import ru.gloomyfolken.tcn2obj.json.components.Box;
import ru.gloomyfolken.tcn2obj.json.components.Faces;
import ru.gloomyfolken.tcn2obj.json.components.Faces.FaceComponent;
import ru.gloomyfolken.tcn2obj.json.components.Rotation;
import ru.gloomyfolken.tcn2obj.obj.Face;
import ru.gloomyfolken.tcn2obj.obj.ObjModel;
import ru.gloomyfolken.tcn2obj.obj.Shape;
import ru.gloomyfolken.tcn2obj.obj.TextureCoords;
import ru.gloomyfolken.tcn2obj.obj.Vertex;

public class JsonConverter
{
    public JsonModel model;

    public ObjModel tcn2obj(JsonModel model, float scale)
    {
        ObjModel obj = new ObjModel();
        this.model = model;
        ArrayList<Box> boxes = model.model.getElements();
        for (Box box : boxes)
        {
            obj.shapes.add(convertBoxToShape(obj, box, scale));
        }
        return obj;
    }

    int             boxNames = 0;
    HashSet<String> names    = Sets.newHashSet();

    private Shape convertBoxToShape(ObjModel model, Box box, float scale)
    {
        if (box.getName() == null)
        {
            box.setName("box_" + boxNames++);
        }
        if (names.contains(box.getName()))
        {
            box.setName(box.getName() + boxNames++);
        }
        names.add(box.getName());
        box.setName("Cube");
        Shape shape = new Shape(model, box.getName());

        float[] from = box.getFrom();
        float[] to = box.getTo();

        Faces faces = box.getFaces();
        faces.init();

        Vertex frontTopLeft = new Vertex(from[0], from[1], from[2]);
        Vertex frontTopRight = new Vertex(to[0], from[1], from[2]);
        Vertex frontBottomRight = new Vertex(to[0], to[1], from[2]);
        Vertex frontBottomLeft = new Vertex(from[0], to[1], from[2]);
        Vertex backTopLeft = new Vertex(from[0], from[1], to[2]);
        Vertex backTopRight = new Vertex(to[0], from[1], to[2]);
        Vertex backBottomRight = new Vertex(to[0], to[1], to[2]);
        Vertex backBottomLeft = new Vertex(from[0], to[1], to[2]);

        if (faces.faces[0] && faces.faces[1])
        {
            shape.faces.add(new Face(shape).append(frontBottomLeft, createUV(0, faces.components[0]))
                    .append(frontBottomRight, createUV(1, faces.components[0]))
                    .append(frontTopRight, createUV(2, faces.components[0]))
                    .append(frontTopLeft, createUV(3, faces.components[0])));
            shape.faces.add(new Face(shape).append(backBottomLeft, createUV(0, faces.components[1]))
                    .append(backBottomRight, createUV(1, faces.components[1]))
                    .append(backTopRight, createUV(2, faces.components[1]))
                    .append(backTopLeft, createUV(3, faces.components[1])));
        }
        if (faces.faces[4] && faces.faces[5])
        {
            shape.faces.add(new Face(shape).append(frontTopLeft, createUV(0, faces.components[4]))
                    .append(frontTopRight, createUV(1, faces.components[4]))
                    .append(backTopRight, createUV(2, faces.components[4]))
                    .append(backTopLeft, createUV(3, faces.components[4])));
            shape.faces.add(new Face(shape).append(backBottomLeft, createUV(0, faces.components[5]))
                    .append(backBottomRight, createUV(1, faces.components[5]))
                    .append(frontBottomRight, createUV(2, faces.components[5]))
                    .append(frontBottomLeft, createUV(3, faces.components[5])));
        }
        if (faces.faces[2] && faces.faces[3])
        {
            shape.faces.add(new Face(shape).append(backBottomLeft, createUV(0, faces.components[2]))
                    .append(frontBottomLeft, createUV(1, faces.components[2]))
                    .append(frontTopLeft, createUV(2, faces.components[2]))
                    .append(backTopLeft, createUV(3, faces.components[2])));
            shape.faces.add(new Face(shape).append(frontBottomRight, createUV(0, faces.components[3]))
                    .append(backBottomRight, createUV(1, faces.components[3]))
                    .append(backTopRight, createUV(2, faces.components[3]))
                    .append(frontTopRight, createUV(3, faces.components[3])));
        }

        Rotation rotation = box.getRotation();

        if (rotation != null)
        {
            float[] offset = rotation.getOrigin();

            shape.translate(new Vector3f(-offset[0], -offset[1], -offset[2]));
            if (rotation.getAxis().equals("x")) shape.rotate((float) -rotation.getAngle(), 1, 0, 0);
            if (rotation.getAxis().equals("y")) shape.rotate((float) -rotation.getAngle(), 0, 1, 0);
            if (rotation.getAxis().equals("z")) shape.rotate((float) -rotation.getAngle(), 0, 0, 1);

            shape.translate(new Vector3f(offset[0], offset[1], offset[2]));
        }

        return shape;
    }

    private TextureCoords createUV(int index, FaceComponent component)
    {
        return new TextureCoords(0, 0);
    }
}
