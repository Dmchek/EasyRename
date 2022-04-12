package EasyRename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;

/**
 * Created by DM5 on 18.02.2020.
 */
class MyList {
    private final ObservableList<File> observableList;
    private ObservableList<String> listNameFileFolder;
    final FilenameFilter filter = new FolderFileFilter();
    // --Commented out by Inspection (22.02.2020 17:32):private String path;

// --Commented out by Inspection START (22.02.2020 17:32):
//    public ListView getList() {
//
//        return list;
//    }
// --Commented out by Inspection STOP (22.02.2020 17:32)

// --Commented out by Inspection START (22.02.2020 17:32):
//    public void setListView(ListView listFileFolder) {
//
//        this.list = listFileFolder;
//    }
// --Commented out by Inspection STOP (22.02.2020 17:32)

    //меняем путь к папке
    // public void setPath(String path){
//
    //  this.path = path;
    // }

    public MyList() {
        this.observableList = FXCollections.observableArrayList();
        this.listNameFileFolder = FXCollections.observableArrayList();
        // ListView<String> list = new ListView(this.observableList);
    }

    //получаем список файлов в папке и полоный путь к ним
    public ObservableList<File> GetFilesFromFolder(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles(filter);
        observableList.clear();
        observableList.addAll(files);
        return observableList;
    }

    //получаем список имен файлов в папке
    public ObservableList<String> GetNameFilesFromFolder(String path) {
        File dir = new File(path);
        String[] fileName = dir.list(filter);
        listNameFileFolder.clear();
        assert fileName != null;
        Collections.addAll(listNameFileFolder, fileName);
        return listNameFileFolder;
    }

    public void setNameFilesFromFolder(ObservableList<String> listNameFileFolder) {
        this.listNameFileFolder = listNameFileFolder;
    }

}
