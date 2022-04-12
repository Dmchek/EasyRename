package EasyRename;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Start EasyRename");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("easyrename.fxml"));
        Parent root = loader.load(); // must be called before getting the controller!
        EasyRenameController controller = loader.getController();
        primaryStage.setTitle("EasyRename");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}
