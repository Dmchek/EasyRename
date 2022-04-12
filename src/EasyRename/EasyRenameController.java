package EasyRename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@SuppressWarnings({"unchecked", "rawtypes"})
public class EasyRenameController implements Initializable {

    private static final Logger logger = Logger.getLogger(EasyRenameController.class.getName());
    private final HashMap<String, String> tableFolders = new HashMap<>();
    private final MyList myList = new MyList();
    private File oldImage;
    private ObservableList<File> observableList;
    private ObservableList<String> namePathFolder = FXCollections.observableArrayList();
    private ObservableList<String> listNameFileFolder;
    private SelectionModel<String> fileSelect;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private File selectedDirectory;
    private File directoryBuffer;
    private CheckMenuItem checkMenuItem;
    private MenuItem checkMenu;
    private Object CheckMenuItem;
    ObservableList<String> newLangs;

    void setFolder() { //изменяем дирректорию
        directoryBuffer = selectedDirectory;
        if (selectedDirectory != null) {
            selectedDirectory = null;
        }
        Stage primaryStage = (Stage) anchorId.getScene().getWindow();
        File validate = directoryChooser.showDialog(primaryStage);

        if (validate == null) {
            selectedDirectory = directoryBuffer;

        } else {
            selectedDirectory = validate;
            String msg = "Папка : " + selectedDirectory.getAbsolutePath();
            pathFolder.setText(msg);
        }
    }

    public String getFolder() { //получаем путь к папке, которую выбрали

        return selectedDirectory.getAbsolutePath();
    }

    @FXML
    private AnchorPane anchorId;

    @FXML
    private MenuItem openFolder;

    @FXML
    private MenuItem close;

    @FXML
    private ListView list;

    @FXML
    private TextField reName;

    @FXML
    private Button btnOk;

    @FXML
    private MenuButton changeFolder;

    @FXML
    private Text pathFolder;

    @FXML
    private Text yesOrNo;

    @FXML
    private ImageView preview;

    @FXML
    private MenuItem folderManager;

    @FXML
    void ok(ActionEvent event) throws IOException {
        File oldFile = new File(getFolder() + "\\" + observableList.get(fileSelect.getSelectedIndex()).getName());
        Path oldPath = Paths.get(oldFile.getPath());
        for (int i = 0; i < changeFolder.getItems().size(); i++) {
            checkMenuItem = (javafx.scene.control.CheckMenuItem) changeFolder.getItems().get(i);
            if (checkMenuItem.isSelected()) {
                File newFile = new File(tableFolders.get(checkMenuItem.getText()) + "\\" + reName.getText());
                Path newPath = Paths.get(newFile.getPath());
                Files.copy(oldPath, newPath, REPLACE_EXISTING);
                if (oldFile.renameTo(newFile)) {
                    yesOrNo.setText("Well done !");

                } else {
                    yesOrNo.setText("Nope , try again");
                }
            } else {
                yesOrNo.setText("Nope , try again");
            }
        }
        boolean deleted = oldFile.delete();
        //observableList.clear();
        //listNameFileFolder = FXCollections.observableArrayList(myList.GetNameFilesFromFolder(getFolder()));
        //list.getItems().clear();
        //list.setItems(listNameFileFolder);
        fileSelect.select(reName.getText());
        preview.setDisable(true);
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    @FXML
    void newFolder(ActionEvent event) {
        try {
            preview.setVisible(false);
            setFolder();
            if (selectedDirectory == null || selectedDirectory == directoryBuffer) {
                System.out.println("Отмена выбора");
                directoryBuffer = null;
                preview.setVisible(true);
            } else {
                if (listNameFileFolder != null) {
                    observableList.clear();
                    listNameFileFolder.removeAll();
                    listNameFileFolder = null;
                }
                observableList = FXCollections.observableArrayList(myList.GetFilesFromFolder(getFolder()));
                listNameFileFolder = FXCollections.observableArrayList(myList.GetNameFilesFromFolder(getFolder()));
                list.setItems(listNameFileFolder);
                fileSelect = list.getSelectionModel();
                fileSelect.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    reName.setText(newValue);
                    try {
                        previewImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "не открылась папка", new Throwable());
        }
        directoryBuffer = null;
    }

    @FXML
    private void previewImage() throws IOException {
        oldImage = new File(getFolder() + "\\" + observableList.get(fileSelect.getSelectedIndex()).getName());
        System.out.println(fileSelect.getSelectedIndex());
        System.out.println(oldImage);
        String pathImage = oldImage.toURI().toURL().toString();
        if (pathImage.endsWith(".pdf")) {
            try (PDDocument pdf = PDDocument.load(oldImage)) {
                preview.setVisible(true);
                PDFRenderer imagePDF = new PDFRenderer(pdf);
                Image image = SwingFXUtils.toFXImage(imagePDF.renderImage(0), null);
                preview.setImage(image);
                System.out.println(pathImage);
            }
        } else if (pathImage.endsWith(".jpeg") || pathImage.endsWith(".jpg") || pathImage.endsWith(".png")) {
            preview.setVisible(true);
            Image image = new Image(pathImage);
            preview.setImage(image);
            System.out.println(pathImage);
        }
    }

    @FXML
    void scaleUpImage() {
        preview.setOnMousePressed(event -> {
            preview.setScaleX(2);
            preview.setScaleY(2);
        });
    }

    @FXML
    void scaleDownImage() {
        preview.setOnMouseReleased(event -> {
            preview.setScaleX(1);
            preview.setScaleY(1);
        });
    }

    @FXML
    void openManagerFolder(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/EasyRename/pathmaneger.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Менеджер Папок 1.5");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("PathList.dm"))) {
            HashMap<String, String> tableManagerFolders = (HashMap) ois.readObject();
            String[] nameManagerFolder = tableManagerFolders.keySet().toArray(new String[0]);
            String[] pathManagerFolder = tableManagerFolders.values().toArray(new String[0]);
            for (int i = 0; i < nameManagerFolder.length; i++) {
                String nameM = nameManagerFolder[i];
                String pathM = pathManagerFolder[i];
                namePathFolder.add(nameM);
                tableFolders.put(nameM, pathM);
                checkMenuItem = new CheckMenuItem(nameM);
                changeFolder.getItems().add(i, checkMenuItem);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // listClear = list.getSelectionModel();
        // listClear.selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
        //  listNameFileFolder.clear();
        //  listNameFileFolder.addAll(myList.GetNameFilesFromFolder(getFolder()));
        // });
    }
}
