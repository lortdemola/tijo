package org.example.j_p_moldmaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
/**
 * Glowna klasa aplikacji dla generatora schematow Java/Python.
 * Rozszerza klase Application z JavaFX.
 *
 * <p>
 * Klasa ta jest odpowiedzialna za inicjalizacje i wyswietlanie glownego okna aplikacji.
 * </p>
 *
 * @author Kacper Dworak
 * @version 4.21
 */
public class HelloApplication extends Application {

    /**
     * Glowny punkt wejscia do aplikacji.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Inicjalizuje i uruchamia glowne okno aplikacji.
     *
     * @param stage glowny etap (okno) aplikacji
     * @throws IOException jesli wystapi blad podczas ladowania pliku FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org.example.j_p_moldmaker/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 430);
        stage.initStyle(StageStyle.UNDECORATED);
        HelloController controller = fxmlLoader.getController();
        controller.ControllerInit(stage);
        stage.setTitle("Java/Python SchematicGenerator");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setScene(scene);

        stage.getScene().getRoot().setStyle("-fx-background-color: #333333;");
        stage.show();
    }

}