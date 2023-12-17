package org.example.j_p_moldmaker;


import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.example.functions.GenJava.genJava;
import static org.example.functions.GenPython.genPython;
/**
 * Kontroler klasy dla aplikacji Java/Python SchematicGenerator.
 *
 * <p>
 * Klasa ta obsluguje logike i interakcje z glownym oknem aplikacji.
 * </p>
 *
 * @author Kacper Dworak
 * @version 2.5
 */
public class HelloController {
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private TextField Text;
    private Stage stage;
    @FXML
    private AnchorPane root;

    @FXML
    private Button Gen;
    @FXML
    private GridPane Gp;
    @FXML
    private TextArea Textout;
    @FXML
    private CheckBox JV;
    @FXML
    private CheckBox PY;
    public Double TextI,GridP;
    public boolean isInfo = false;
    public int carterPos = 0;
    public File selectedDirectory;
    /**
     * Inicjalizuje kontroler glownym etapem.
     *
     * @param stage glowny etap dla aplikacji
     */
    public void ControllerInit(Stage stage) {
        this.stage = stage;
        Gen.setDisable(true);
    }

    /**
     * Obsluguje zdarzenie klikniecia przycisku informacji.
     * Przelacza widocznosc elementow informacyjnych.
     */

    @FXML
    protected void onInfoButtonClick() {

        if(!isInfo){
            TextI=Text.getHeight();
            GridP=Gp.getHeight();
            Gp.setVisible(false);
            Text.setPrefHeight(0);
            Gp.setPrefHeight(0);
            isInfo= true;
            Textout.setText("info");

        }else {
            Text.setPrefHeight(TextI);
            Text.setVisible(true);
            Gp.setPrefHeight(GridP);
            Gp.setVisible(true);
            Textout.setText("");
            isInfo= false;
        }

    }
    /**
     * Obsluguje zdarzenie klikniecia przycisku generowania.
     * Generuje kod Java i Python na podstawie danych wprowadzonych przez uzytkownika.
     *
     * @throws IOException jesli wystapi blad podczas generowania kodu
     */
    @FXML
    protected void onGenerateButtonClick() throws IOException {


        TextInputDialog td = new TextInputDialog("enter any text");
        String results="";

        td.setHeaderText("enter your name");
        td.showAndWait();
        results += genJava(Text.getText(),selectedDirectory.getAbsolutePath(), td.getEditor().getText());
        results += genPython(Text.getText(),selectedDirectory.getAbsolutePath(), td.getEditor().getText());
        if(!Objects.equals(results, "")){
            Textout.setText(results);
        }

    }
    /**
     * Obsluguje zdarzenie klikniecia przycisku ustawiania lokalizacji wyjsciowej.
     * Otwiera okno wyboru katalogu w celu ustawienia lokalizacji wyjsciowej.
     */
    @FXML
    protected void onSetLocationButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        selectedDirectory = directoryChooser.showDialog(this.stage);
        //System.out.println(selectedDirectory.getAbsolutePath());
        Gen.setDisable(false);
    }
    /**
     * Zamyka aplikacje.
     */
    @FXML
    protected  void exit(){
        stage.close();
    }
    /**
     * Obsluguje zdarzenie nacisniecia myszy na obszarze zakladki.
     * Umozliwia uzytkownikowi przeciaganie okna aplikacji.
     *
     * @param event zdarzenie myszy
     */
    @FXML
    protected void onMousePressTab(javafx.scene.input.MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }
    }
    /**
     * Obsluguje zdarzenie przeciagania myszy na obszarze zakladki.
     * Umozliwia uzytkownikowi przeciaganie okna aplikacji.
     *
     * @param event zdarzenie myszy
     */
    @FXML
    protected void onMouseDragTab(javafx.scene.input.MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if(stage.isMaximized()){
                stage.setMaximized(false);
                root.resize(stage.getWidth(),stage.getHeight());
              
            }

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);

        }
    }
    /**
     * Obsluguje zdarzenie klikniecia przycisku maksymalizacji.
     * Przelacza miedzy maksymalizacja a przywracaniem okna.
     */
    @FXML
    protected void onMaximizeButtonClick() {
        if (!stage.isMaximized()) {
            Screen screen = Screen.getPrimary();
            double taskbarHeight = Screen.getPrimary().getVisualBounds().getHeight() - screen.getVisualBounds().getHeight();

          
            stage.setMaximized(true);
            stage.setHeight(screen.getVisualBounds().getHeight() - taskbarHeight);
           
            root.resize(stage.getWidth(),stage.getHeight());
        } else {
            stage.setMaximized(false);
            root.resize(stage.getWidth(),stage.getHeight());

        }
    }

    /**
     * Obsluguje zdarzenie klikniecia przycisku minimalizacji.
     * Minimalizuje okno do paska zadań.
     */
    @FXML
    protected void onMinimizeButtonClick() {
        stage.setIconified(true);
    }
}