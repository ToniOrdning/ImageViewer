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

public class testi extends Application{
    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage){

        //Windowsize
        final int WINDOW_WIDTH = 600;
        final int WINDOW_HEIGHT = 600;

        //Setup
        primaryStage.setTitle("Image Viewer");
        VBox layout = new VBox();
        Scene primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Image
        ImageView testImageView = new ImageView();
        Image testImage = new Image("jesse.jpg");
        testImageView.setImage(testImage);
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);

        //Files
        Button fileButton = new Button("Images");
        fileButton.setOnAction(e -> chooseFile(testImageView));

        layout.getChildren().addAll(fileButton, testImageView);

        //Final setup
        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    public void chooseFile(ImageView viewer){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your pictures");

        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("JPG", "*.jpg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        Image selectedImage = new Image("file:" + selectedFile);

        viewer.setImage(selectedImage);

    }
}