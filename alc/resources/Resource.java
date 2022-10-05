package alc.resources;

import javafx.scene.image.Image;

import java.util.HashMap;

public class Resource {

    private static final HashMap<String, Image> images = new HashMap<>();

    public Resource(){}

    public Image get(String fileName){
        if(images.containsKey(fileName)){
            return images.get(fileName);
        } else {
            Image image = new Image("alc/resources/"+fileName);
            images.put(fileName, image);
            return image;
        }
    }
}
