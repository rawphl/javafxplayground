package ch.javafxplayground;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class App extends Application {
    // Breite und Höhe des Spielfeldes
    public int width = 600;
    public int height = 600;
    // Anzahl Zeilen und Spalten des Spielfelds
    public int numberOfRows = 10;
    public int numberOfColumns = 10;
    // Breite und Höhe einer einzelnen Zelle
    public int cellWidth = width / numberOfRows;
    public int cellHeight = height / numberOfColumns;
    // Das Spielfeld, ersetzt int[][] mit einem 2d-Array eurer eigenen Klasse, also: MeineKlasse[][] z.B.
    public int[][] board = new int[numberOfRows][numberOfColumns];
    // Ein Gameobjekt mit Position x / y
    public GameObject gameObject = new GameObject();

    // Wir merken uns die letzten Indices, wo unsere Maus war, damit wir immer nur das Feld blaue markieren, auf dem die Maus gerade ist
    public int mouseIndexX;
    public int mouseIndexY;

    public Canvas canvas = new Canvas(width, height);
    public GraphicsContext gc = canvas.getGraphicsContext2D();
    public Scene scene;
    public long lastTime = System.nanoTime();

    // Hier hat es einen Bug, überlegt euch: was passiert oder sollte an den Ränder passieren?
    public AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            var dt = (now - lastTime) / 1e9;
            var x = (int) Math.floor(gameObject.getX() / cellWidth);
            var y = (int) Math.floor(gameObject.getY() / cellHeight);
            board[x][y] = 0;
            gameObject.update(dt);
            x = (int) Math.floor(gameObject.getX() / cellWidth);
            y = (int) Math.floor(gameObject.getY() / cellHeight);
            board[x][y] = 4;
            render();
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Javafx Boilerplate
        GridPane gridPane = new GridPane();
        gridPane.add(canvas, 0, 0);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new Label("Platzhalter für ein GUI. JavaFX scheint Probleme mit Umlauten zu haben, für die ich gerade keine Geduld habe :)"));
        gridPane.add(borderPane, 1, 0);
        scene = new Scene(gridPane, width * 2, height);
        primaryStage.setScene(scene);
        primaryStage.show();

        var gc = canvas.getGraphicsContext2D();

        // Wir nehmen die Pixelkoordinaten der Maus und berechen die Indices auf unserem Spielfeld
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            var mx = e.getX();
            var my = e.getY();

            var x = (int) Math.floor(mx / cellWidth);
            var y = (int) Math.floor(my / cellHeight);
            board[x][y] = 1;
            System.out.println(String.format("MOUSE_CLICKED: Mouse { mx: %.2f, my: %.2f }, Indices { x: %s, y: %s }", mx, my, x, y));
        });

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (!(board[mouseIndexX][mouseIndexY] == 1)) {
                board[mouseIndexX][mouseIndexY] = 0;
            }

            var mx = e.getX();
            var my = e.getY();

            mouseIndexX = (int) Math.floor(mx / cellWidth);
            mouseIndexY = (int) Math.floor(my / cellHeight);

            if (!(board[mouseIndexX][mouseIndexY] == 1)) {
                board[mouseIndexX][mouseIndexY] = 3;
            }
            System.out.println(String.format("MOUSE_MOVED: Mouse { mx: %.2f, my: %.2f }, Indices { x: %s, y: %s }", mx, my, mouseIndexX, mouseIndexX));
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            var code = e.getCode();

            if (code == KeyCode.W) {
                gameObject.setVy(-5);
            }

            if (code == KeyCode.A) {
                gameObject.setVx(-5);
            }

            if (code == KeyCode.S) {
                gameObject.setVy(5);
            }

            if (code == KeyCode.D) {
                gameObject.setVx(5);
            }

            System.out.println("KEY_PRESSED " + code);
        });

        timer.start();
    }

    public void render() {
        // Wir zeichnen das Feld
        for (int x = 0; x < numberOfRows; x++) {
            for (int y = 0; y < numberOfColumns; y++) {
                var value = board[x][y];
                if (value == 0) {
                    gc.setFill(Paint.valueOf("#FF0000"));
                }

                if (value == 1) {
                    gc.setFill(Paint.valueOf("#00FF00"));
                }

                if (value == 3) {
                    gc.setFill(Paint.valueOf("#0000FF"));
                }

                if (value == 4) {
                    gc.setFill(Paint.valueOf("#00FFFF"));
                }
                gc.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
            }
        }
        // Gameobjects müssen nach dem Felder gerendert werden, damit sie über dem Feld sichtbar sind
        gc.setFill(Paint.valueOf("#FF00FF"));
        gc.fillOval(gameObject.getX(), gameObject.getY(), cellWidth, cellHeight);
    }
}
