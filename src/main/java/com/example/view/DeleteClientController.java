package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
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

public class DeleteClientController extends Controller implements Initializable {
    @FXML
    private Button deleteClientButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldClientCode;
    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }
    /**
     * Inicializa el controlador al cargar el FXML.
     * Configura la acción del botón de eliminar cliente para manejar la eliminación del cliente.
     * Elimina un cliente de la base de datos basándose en el código de cliente introducido.
     * Muestra alertas para varios escenarios, como entrada vacía, formato de código de cliente inválido,
     * eliminación exitosa o fallo de eliminación.
     *
     * @param location la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce
     * @param resources los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteClientButton.setOnAction(event -> {
            try {
                deleteClient(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Maneja la acción de eliminar un cliente cuando se activa mediante el botón de eliminar cliente.
     * Valida y elimina un cliente de la base de datos basándose en el código de cliente introducido.
     * Muestra mensajes de error apropiados para entradas inválidas, formato de código de cliente inválido,
     * eliminación exitosa o fallo de eliminación.
     *
     * @param event el evento activado por la acción de eliminar cliente
     * @throws SQLException si hay un problema durante las operaciones de base de datos
     */
    @FXML
    private void deleteClient(Event event) throws SQLException {
        String clientCode = fieldClientCode.getText().trim();

        // Comprueba si el campo del código del cliente está vacío.
        if (clientCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código del cliente no puede estar vacío.");
        } else {
            try {
                // Comprueba si el código del cliente contiene solo dígitos
                if (!clientCode.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente debe contener solo números.");
                    return;
                }
                ClientDAO cdao = new ClientDAO();
                boolean deleted = cdao.delete(Integer.parseInt(clientCode));

                // Muestra una alerta apropiada según el resultado de la eliminación.
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "El cliente se ha eliminado correctamente.");
                    App.currentController.changeScene(Scenes.MAINPAGE, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente no coincide con ningún cliente en la base de datos.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente debe ser un número válido.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar al cliente de la base de datos.");
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
