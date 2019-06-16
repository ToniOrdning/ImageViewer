import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

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

        ImageView testImageView = new ImageView();
        Image testImage = new Image("jesse.jpg");

        testImageView.setImage(testImage);
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);
        layout.getChildren().add(testImageView);

        //Final setup
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
}