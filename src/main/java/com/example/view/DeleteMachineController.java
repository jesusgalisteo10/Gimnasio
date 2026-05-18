package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DeleteMachineController extends Controller implements Initializable {

    @FXML
    private Button deleteMachineButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldMachineCode;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Inicializa el controlador cuando se carga el FXML.
     * Configura la acción del botón de eliminar máquina para manejar la eliminación de la misma.
     * Elimina una máquina de la base de datos basándose en el código de máquina introducido.
     * Muestra alertas para varios escenarios como entrada vacía, formato de código de máquina inválido,
     * eliminación exitosa o fallo en la eliminación.
     *
     * @param location la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce
     * @param resources los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteMachineButton.setOnAction(event -> {
            try {
                deleteMachine(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Maneja la acción de eliminar una máquina cuando es activada por el botón de eliminar máquina.
     * Valida y elimina una máquina de la base de datos basándose en el código de máquina introducido.
     * Muestra mensajes de error apropiados para entradas inválidas, formato de código de máquina inválido,
     * eliminación exitosa o fallo en la eliminación.
     *
     * @param event el evento activado por la acción de eliminar máquina
     * @throws SQLException si hay un problema durante las operaciones de base de datos
     */
    @FXML
    private void deleteMachine(Event event) throws SQLException {
        String machineCode = fieldMachineCode.getText().trim();

        // Comprueba si el campo del código de máquina está vacío.
        if (machineCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código de máquina no puede estar vacío.");
        } else {
            try {
                // Comprueba si el código de máquina contiene solo dígitos
                if (!machineCode.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina debe contener solo números.");
                    return;
                }
                MachineDAO mdao = new MachineDAO();
                boolean deleted = mdao.delete(Integer.parseInt(machineCode));


                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "La máquina se ha eliminado correctamente.");
                    App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina no coincide con ninguna máquina en la base de datos.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina debe ser un número válido.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la máquina de la base de datos.");
                e.printStackTrace();
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
