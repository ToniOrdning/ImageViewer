import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.List;

public class testi extends Application{
    public static void main(String[] args){

        launch(args);

    }

    //Windowsize
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 600;
    private List<File> selectedFiles;
    private VBox layout;
    private ImageView testImageView;
    private Button fileButton;
    private Image showNewImage;

    @Override
    public void start(Stage primaryStage){

        //Setup
        primaryStage.setTitle("Image Viewer");
        layout = new VBox();
        Scene primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Image
        testImageView = new ImageView();
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);

        //Files
        fileButton = new Button("Images");
        fileButton.setOnAction(e -> chooseFile(testImageView));

        //Add elements
        layout.getChildren().addAll(fileButton, testImageView);

        //Final setup
        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    public void chooseFile(ImageView viewer){   //Select files for viewing

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your pictures");

        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("JPG", "*.jpg"));

        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        
        //System.out.println(selectedFiles);    //TROUBLESHOOT

        showNewImage = new Image("file:" + selectedFiles.get(0));
        testImageView.setImage(showNewImage);

    }
}