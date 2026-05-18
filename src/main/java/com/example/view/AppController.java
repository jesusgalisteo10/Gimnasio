package com.example.view;

import com.example.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {


    @FXML
    private BorderPane borderPane;
    private Controller centerController;
    /**
     * Controla la funcionalidad principal de la aplicación y la gestión de escenas.
     * Maneja la carga de vistas FXML, el cambio de escenas dentro de la aplicación
     * y la apertura de diálogos modales. Inicializa la aplicación con una escena de bienvenida.
     *
     * @param input los datos de entrada (no utilizados en esta implementación)
     * @throws Exception si hay un problema durante la carga o inicialización de la escena
     */
    @Override
    public void onOpen(Object input) throws Exception {
        changeScene(Scenes.WELCOME, null);
    }

    /**
     * Inicializa el controlador cuando se carga el archivo FXML.
     *
     * @param url la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce
     * @param rb los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb)  {

    }

    /**
     * Carga una vista FXML basada en la enumeración de escena proporcionada.
     *
     * @param scenes la enumeración de escena que indica qué vista FXML cargar
     * @return el objeto View cargado que contiene la escena cargada y su controlador correspondiente
     * @throws Exception si hay un problema durante la carga del FXML
     */
    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL();
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }

    /**
     * Cambia la escena central principal de la aplicación a la escena especificada.
     * Carga la vista FXML, la establece como el centro del BorderPane e inicializa su controlador.
     *
     * @param scene la enumeración de escena que indica qué vista FXML cargar y mostrar
     * @param data el objeto de datos pasado al controlador de la escena recién cargada
     * @throws Exception si hay un problema durante la carga de la escena o la inicialización del controlador
     */
    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }

    /**
     * Abre un diálogo modal con la escena, título, controlador padre y objeto de datos especificados.
     * Carga la vista FXML, inicializa la ventana (Stage) como un diálogo modal y la muestra.
     *
     * @param scene la enumeración de escena que indica qué vista FXML cargar y mostrar como un diálogo modal
     * @param title el título de la ventana del diálogo modal
     * @param parent el controlador padre que abrió el diálogo modal
     * @param data el objeto de datos pasado al controlador del diálogo modal
     * @throws Exception si hay un problema durante la carga del diálogo modal o la inicialización del controlador
     */
    public void openModal(Scenes scene, String title, Controller parent, Object data) throws Exception {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }

    /**
     * Maneja la limpieza cuando la aplicación se cierra o el controlador ya no es necesario.
     *
     * @param output los datos de salida (no utilizados en esta implementación)
     */
    @Override
    public void onClose(Object output) {
    }



}



