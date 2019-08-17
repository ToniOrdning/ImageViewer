import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewer extends Application {
	public static void main(String[] args) {

		launch(args);

	}

	// Windowsize
	private final double WINDOW_WIDTH = 1024; // Default window width
	private final double WINDOW_HEIGHT = 768; // Default window height
	private double tempWindowWidth; // For resizing images
	private double tempWindowHeight;// For resizing images

	// Misc
	private List<File> selectedFiles; // Selecting files with file manager
	private int fileCount = 0; // For randomizing slideshows
	private BorderPane layout; // Layout for program
	private ImageView showImageView; // Viewing images
	private Image showNewImage; // switching image
	private int imageCounter = 0; // Keeping track of shown image
	private File imageToDelete; // For deleting images
	private Timer slideshowTimer = new Timer(); // For timing picture changes
	private boolean slideshowRunning = false; // For slideshows
	private boolean randomSlideshowRunning = false; // For slideshows
	private Scene primaryScene;

	// Menu
	private MenuBar defaultMenubar;
	private Menu fileMenu;
	private Menu startMenu;
	private Menu viewMenu;
	private MenuItem openMenuItem;
	private MenuItem nextImageMenuItem;
	private MenuItem previousImageMenuItem;
	private MenuItem deleteMenuItem;
	private MenuItem startSlideshowMenuItem;
	private MenuItem randomSlideshowMenuItem;
	private MenuItem fullscreenMenuItem;
	private MenuItem exitProgram;

	@Override
	public void start(Stage primaryStage) {

		// Setup
		primaryStage.setTitle("Image Viewer");
		layout = new BorderPane();
		primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Image
		showImageView = new ImageView();
		showImageView.setFitWidth(WINDOW_WIDTH);
		showImageView.setFitHeight(WINDOW_HEIGHT);
		showImageView.setPreserveRatio(true);

		// MenuItems
		openMenuItem = new MenuItem("Open");
		openMenuItem.setOnAction(e -> {
			chooseFile(showImageView);
		});
		openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

		nextImageMenuItem = new MenuItem("Next");
		nextImageMenuItem.setOnAction(e -> {
			nextImage();
		});
		nextImageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_ANY));

		previousImageMenuItem = new MenuItem("Previous");
		previousImageMenuItem.setOnAction(e -> {
			previousImage();
		});
		previousImageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_ANY));

		deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(e -> {
			deleteImage();
		});
		deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_ANY));

		startSlideshowMenuItem = new MenuItem("Slideshow");
		startSlideshowMenuItem.setOnAction(e -> {
			startSlideshow();
		});
		startSlideshowMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.CONTROL_ANY));

		exitProgram = new MenuItem("Exit");
		exitProgram.setOnAction(e -> {
			Platform.exit();
		});
		exitProgram.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

		randomSlideshowMenuItem = new MenuItem("Random Slideshow");
		randomSlideshowMenuItem.setOnAction(e -> {
			startRandomSlideshow();
		});
		randomSlideshowMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F2, KeyCombination.CONTROL_ANY));

		fullscreenMenuItem = new MenuItem("Fullscreen");
		fullscreenMenuItem.setOnAction(e -> {
			if (primaryStage.isFullScreen() == false) {
				primaryStage.setFullScreen(true);
				layout.getChildren().remove(defaultMenubar);
				primaryScene.setCursor(Cursor.NONE);
			} else if (primaryStage.isFullScreen() == true) {
				primaryStage.setFullScreen(false);
				layout.getChildren().remove(showImageView);
				layout.setTop(defaultMenubar);
				layout.setCenter(showImageView);
				primaryScene.setCursor(Cursor.DEFAULT);
			}
		});
		fullscreenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11));

		// Menus
		fileMenu = new Menu("File");
		fileMenu.getItems().addAll(openMenuItem, nextImageMenuItem, previousImageMenuItem, deleteMenuItem, exitProgram);

		startMenu = new Menu("Start");
		startMenu.getItems().addAll(startSlideshowMenuItem, randomSlideshowMenuItem);

		viewMenu = new Menu("View");
		viewMenu.getItems().addAll(fullscreenMenuItem);

		// MenuBar
		defaultMenubar = new MenuBar();
		defaultMenubar.getMenus().addAll(fileMenu, startMenu, viewMenu);

		// Add elements
		layout.setTop(defaultMenubar);
		layout.setCenter(showImageView);

		// Listeners for window resizes
		primaryScene.widthProperty().addListener((listener) -> {
			whenWindowSizeUpdate();
		});
		primaryScene.heightProperty().addListener((listener) -> {
			whenWindowSizeUpdate();
		});

		// Final setup
		primaryScene.getStylesheets().add("ImageViewer.css");
		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}

	protected void chooseFile(ImageView viewer) { // Select files for viewing

		imageCounter = 0;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your pictures");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "//Pictures//"));

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"),
				new ExtensionFilter("PNG", "*.png"), new ExtensionFilter("JPG", "*.jpg"));

		selectedFiles = fileChooser.showOpenMultipleDialog(null);

		showNewImage();
	}

	protected void nextImage() {

		if (imageCounter >= selectedFiles.size() - 1) {
			imageCounter = 0;
		} else {
			imageCounter += 1;
		}
		showNewImage();
	}

	protected void previousImage() {

		if (imageCounter <= 0) {
			imageCounter = selectedFiles.size() - 1;
		} else {
			imageCounter -= 1;
		}
		showNewImage();
	}

	protected void deleteImage() {

		imageToDelete = selectedFiles.get(imageCounter);
		System.out.println("Deleting image...");
		imageToDelete.delete();
		imageCounter += 1;
		showNewImage();
	}

	protected void randomImage() {

		fileCount = selectedFiles.size();
		imageCounter = (int) Math.floor(Math.random() * fileCount);
		showNewImage();
	}

	protected void showNewImage() {

		tempWindowWidth = primaryScene.getWidth();
		tempWindowHeight = primaryScene.getHeight();

		try {
			showNewImage = new Image("file:" + selectedFiles.get(imageCounter), 0, 0, true, false); // Get new image
		} catch (java.lang.NullPointerException e) {
			System.out.println(e.getMessage());
		}

		if (showNewImage.getWidth() > tempWindowWidth) {
			showImageView.setFitWidth(tempWindowWidth);
		} else {
			showImageView.setFitWidth(showNewImage.getWidth());
		}
		if (showNewImage.getHeight() > tempWindowHeight) {
			showImageView.setFitHeight(tempWindowHeight);
		} else {
			showImageView.setFitHeight(showNewImage.getHeight());
		}

		showImageView.setSmooth(true);
		showImageView.setImage(showNewImage); // Show new image
	}

	protected void startSlideshow() {

		final TimerTask slideshowTimerTask;

		// Checks if slideshow is already running
		if (slideshowRunning == true) {
			try {
				// Cancels the current slideshow
				slideshowTimer.cancel();
				slideshowRunning = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else if (randomSlideshowRunning == true) {
			try {
				// Cancels slideshow
				slideshowTimer.cancel();
				randomSlideshowRunning = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			// Creates a new slideshow and runs it
			slideshowTimer = new Timer();
			if (randomSlideshowRunning == false) {

				slideshowTimerTask = new TimerTask() {
					@Override
					public void run() {
						nextImage();
					}
				};

				try {
					slideshowTimer.schedule(slideshowTimerTask, 5000, 5000);
					slideshowRunning = true;
				} catch (IllegalStateException e) {
					System.out.println(e.getMessage());
				} catch (NullPointerException ne) {
					System.out.println(ne.getMessage());
				}
			}
		}
	}

	protected void startRandomSlideshow() {

		final TimerTask randomSlideshowTimerTask;

		// Checks if the randomized slideshow is running
		if (randomSlideshowRunning == true) {
			try {
				// Cancels the current slideshow
				slideshowTimer.cancel();
				randomSlideshowRunning = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else if (slideshowRunning == true) {
			try {
				// cancels slideshow
				slideshowTimer.cancel();
				slideshowRunning = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			// Creates a new randomized slideshow and runs it
			slideshowTimer = new Timer();
			if (slideshowRunning == false) {

				randomSlideshowTimerTask = new TimerTask() {
					@Override
					public void run() {
						randomImage();
					}
				};

				try {
					slideshowTimer.schedule(randomSlideshowTimerTask, 5000, 5000);
					randomSlideshowRunning = true;
				} catch (IllegalStateException e) {
					System.out.println(e.getMessage());
				} catch (NullPointerException ne) {
					System.out.println(ne.getMessage());
				}
			}
		}
	}

	protected void whenWindowSizeUpdate() {

		tempWindowWidth = primaryScene.getWidth();
		tempWindowHeight = primaryScene.getHeight();

		if (showNewImage.getWidth() > tempWindowWidth) {
			showImageView.setFitWidth(tempWindowWidth);
		} else {
			showImageView.setFitWidth(showNewImage.getWidth());
		}
		if (showNewImage.getHeight() > tempWindowHeight) {
			showImageView.setFitHeight(tempWindowHeight);
		} else {
			showImageView.setFitHeight(showNewImage.getHeight());
		}
	}
}