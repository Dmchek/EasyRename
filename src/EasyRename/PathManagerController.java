package EasyRename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class PathManagerController {

    private final ObservableList<MyFolder> folders = FXCollections.observableArrayList();
    private final HashMap<String, String> table = new HashMap<>();

    @FXML
    private TextField pathName;

    @FXML
    private Text pathFolder;

    @FXML
    private TableView<MyFolder> folderTable;

    @FXML
    private TableColumn<MyFolder, String> nameCol;

    @FXML
    private TableColumn<MyFolder, String> pathCol;

    @FXML
    private AnchorPane anchorId1;

    @FXML
    private Button close;

    @FXML
    private Button saveBase;

    @FXML
    private Text status;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private File selectedDirectory;

    void setFolder() { //изменяем дирректорию
        Stage primaryStage = (Stage) anchorId1.getScene().getWindow();
        selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            pathFolder.setText("Папка : " + selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void initialize() {  // загрузка базы при открытии менеджера папок
        nameCol.setCellValueFactory(new PropertyValueFactory<MyFolder, String>("name"));
        pathCol.setCellValueFactory(new PropertyValueFactory<MyFolder, String>("path"));
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("PathList.dm"))) {
            HashMap<String, String> oldTable = (HashMap) ois.readObject();
            String[] name = oldTable.keySet().toArray(new String[0]);
            String[] path = oldTable.values().toArray(new String[0]);
            for (int i = 0; i < name.length; i++) {
                String name1 = name[i];
                String path1 = path[i];
                table.put(name1, path1);
                folders.add(new MyFolder(name1, path1));
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        folderTable.setItems(folders);
        status.setText("Get Ready");
    }

    @FXML
    void newFolder(ActionEvent event) { // выбираем папку на добавление
        setFolder();
    }

    @FXML
    void add(ActionEvent event) { // добавляем папку в базу и таблицу
        if (pathName.getText().equals("")) { // проверяем , задал ли пользователь имя
            status.setText("Дайте имя папке!");
        } else if (pathFolder.getText().equals("Папка : ")) { // проверяем , выбрал ли пользователь папку
            status.setText("Выберите папку!");
        } else if (table.containsKey(pathName.getText())) {// проверяем , есть ли такое имя в базе
            status.setText("Имя уже в базе ! Поменяйте имя");
        } else if (table.containsValue(selectedDirectory.getAbsolutePath())) {// проверяем , есть ли такая папка в базе
            status.setText("Папка уже в базе ! Поменяйте папку!");
        } else { // если имени и папки нет в базе , добавляем ее
            table.put(pathName.getText(), selectedDirectory.getAbsolutePath());
            folders.add(new MyFolder(pathName.getText(), selectedDirectory.getAbsolutePath()));
            folderTable.setItems(folders);
            saveBase.setDisable(false);
            pathName.clear();
            pathFolder.setText("Папка : ");
            status.setText("Добавленно в базу!");
        }
    }

    @FXML
    void delete(ActionEvent event) { //удаляем папку из таблицы
        MyFolder selectedFolder = folderTable.getSelectionModel().getSelectedItem(); //создаем копию выделенного объекта
        table.remove(selectedFolder.getName()); // удаляем объект в базе
        folderTable.getItems().remove(folderTable.getSelectionModel().getSelectedIndex()); // удаляем объект в таблице
        status.setText("Папка удалена!");
        saveBase.setDisable(false); // кнопка save становится активной
    }

    @FXML
    void save(ActionEvent event) { // сохраняем базу
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("PathList.dm"))) {
            oos.writeObject(table);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setText("Сохраненно");
        saveBase.setDisable(true); // отключаем кнопку и скидываем папку и имя
        pathName.clear();
        pathFolder.setText("Папка : ");
    }

    @FXML
    void close(ActionEvent event) { // закрытие окна менеджера
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();

    }
}
