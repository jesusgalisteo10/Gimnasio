package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.dao.RoomDAO;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class AddMachineController extends Controller implements Initializable {
    @FXML
    private Button addMachineButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldRoom;

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
     * Valida los campos de entrada (tipo de máquina y código de habitación) y muestra mensajes de error
     * si alguno de los campos está vacío o si el código de habitación no es válido (no se encuentra en la base de datos).
     * Si las validaciones pasan, crea un nuevo objeto Machine asociado con la Room encontrada y lo guarda
     * usando MachineDAO. Si la Room asociada con la Machine no existe inicialmente en la base de datos,
     * guarda la Room primero. Muestra un mensaje de éxito al añadir la máquina correctamente
     * y navega a la página de mostrar máquinas. Cierra la ventana actual después de una operación exitosa.
     *
     * @param event el evento disparado por la acción de guardar
     * @throws Exception si hay un problema durante las operaciones de base de datos o al parsear el código de habitación
     */
    public void onSave(Event event) throws Exception {
        String machineType = fieldName.getText().trim();
        String roomCodeText = fieldRoom.getText().trim();

        // Verificar si el campo del tipo de máquina está vacío
        if (machineType.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del tipo de máquina no puede estar vacío.");
        } else if (roomCodeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código de habitación no puede estar vacío.");
        } else {
            try {
                int roomCode = Integer.parseInt(roomCodeText);

                RoomDAO rdao = new RoomDAO();
                Room room = rdao.findByRoomCode(roomCode);

                // Verificar si la habitación con el código dado existe
                if (room == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación no se encuentra en la base de datos.");
                } else {
                    // Create Machine object and save to database
                    Machine machine = new Machine(room, machineType);
                    MachineDAO cdao = new MachineDAO();
                    cdao.save(machine);

                    // Crear objeto Machine y guardar en la base de datos
                    if (room == null) {
                        rdao.save(machine.getRoom());
                    }

                    // Navegar a la página de mostrar máquinas y cerrar la ventana actual
                    App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación ingresado no es válido.");
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
