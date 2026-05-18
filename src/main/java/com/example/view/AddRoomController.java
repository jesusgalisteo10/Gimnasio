package com.example.view;

import com.example.App;
import com.example.model.dao.RoomDAO;
import com.example.model.entity.Room;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddRoomController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldRoom;
    @FXML
    private Button addRoomButton;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Maneja la acción de guardar cuando el usuario hace clic en el botón de guardar.
     * Valida y guarda un nuevo código de sala, asegurándose de que no esté vacío y esté dentro de un rango válido.
     * Verifica si el código de sala ya existe en la base de datos antes de guardar.
     * Muestra mensajes de error apropiados para entradas inválidas o códigos de sala existentes.
     *
     * @param event el evento activado por la acción de guardar
     * @throws Exception si hay un problema durante las operaciones de base de datos o al parsear el código de sala
     */
    public void onSave(Event event) throws Exception {
        String roomCodeText = fieldRoom.getText().trim();

        // Comprueba si el campo del código de sala está vacío
        if (roomCodeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo no puede estar vacío.");
        } else {
            try {
                int roomCode = Integer.parseInt(roomCodeText);

                // Comprueba si el código de sala está dentro del rango válido
                if (roomCode > 9999) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El valor no puede ser mayor que 9999.");
                } else {
                    RoomDAO roomDAO = new RoomDAO();

                    // Comprueba si el código de sala ya existe en la base de datos
                    if (roomDAO.findByRoomCode(roomCode) != null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación ya existe en la base de datos.");
                    } else {
                        // Guarda la nueva sala si no existe
                        Room room = new Room(roomCode);
                        roomDAO.save(room);
                        // Navega a la página de mostrar máquinas y cierra la ventana actual
                        App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El valor ingresado no es válido.");
            }
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
