package EasyRename;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import java.io.Serializable;

public class MyFolder implements Serializable{

    private static final long serialVersionUID = 5L;
    private SimpleStringProperty name;
    private SimpleStringProperty path;

    public MyFolder(String pName, String pathN) {
        this.name = new SimpleStringProperty(pName);
        this.path = new SimpleStringProperty(pathN);
    }

    public MyFolder(){
    }

    public MyFolder(ObservableList<MyFolder> folders) {
    }


    public String getName(){
        return name.get();
    }

    public void setName(String pName){
        name.set(pName);
    }

    public String getPath(){
        return path.get();
    }

    public void setPath(String pathN){
        path.set(pathN);
    }

}




