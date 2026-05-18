package com.example.view;

import com.example.model.dao.ClientDAO;
import com.example.model.dao.Client_MachineDAO;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DeleteMachineToClientController extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button deleteMachineFromClientButton;
    @FXML
    private ComboBox<String> machineComboBox;
    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private ImageView goBackButton;

    private Map<String, String> machinesMap;
    private  Map<String, String> clientsMap;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }
    /**
     * Inicializa el controlador al cargar el FXML.
     * Recupera todos los clientes y máquinas de la base de datos para rellenar los cuadros combinados de clientes y máquinas.
     * Mapea los códigos de cliente a los nombres de cliente y los códigos de máquina a los tipos de máquina para la visualización en los cuadros combinados.
     * Configura el manejo de eventos para eliminar una máquina de un cliente.
     * Muestra alertas para eliminación exitosa, error de asignación o fallo en la operación de base de datos.
     *
     * @param location la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce
     * @param resources los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Rellena el cuadro combinado de clientes con los nombres de los clientes.
        List<Client> clients = ClientDAO.build().findAll();
        clientsMap = new HashMap<>();
        for (Client client : clients) {
            clientsMap.put(String.valueOf(client.getCode()), client.getName());
        }
        clientComboBox.setItems(FXCollections.observableArrayList(clientsMap.values()));

        // Rellena el cuadro combinado de máquinas con los tipos de máquina.
        List<Machine> machines;
        try {
            machines = MachineDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        machinesMap = new HashMap<>();
        for (Machine machine : machines) {
            machinesMap.put(String.valueOf(machine.getCode()), machine.getMachineType());
        }
        machineComboBox.setItems(FXCollections.observableArrayList(machinesMap.values()));
    }

    /**
     * Maneja la acción de eliminar una máquina de un cliente.
     * Recupera los códigos de máquina y cliente seleccionados de los cuadros combinados, los valida,
     * y elimina la asociación de la base de datos.
     * Muestra mensajes de alerta apropiados para éxito, error de asignación o fallo en la operación de base de datos.
     */
    @FXML
    private void deleteMachineFromClient() {
        String codeMachine = "";
        String valueMachine = machineComboBox.getValue();
        for (String key : machinesMap.keySet()) {
            if (machinesMap.get(key).equals(valueMachine)) {
                codeMachine = key;
                break;
            }
        }

        String codeClient = "";
        String valueClient = clientComboBox.getValue();
        for (String key : clientsMap.keySet()) {
            if (clientsMap.get(key).equals(valueClient)) {
                codeClient = key;
                break;
            }
        }

        int machineCode = 0;
        int clientCode = 0;
        if (!codeMachine.isEmpty() && codeMachine.matches("\\d+") && !codeClient.isEmpty() && codeClient.matches("\\d+")) {
            machineCode = Integer.parseInt(codeMachine);
            clientCode = Integer.parseInt(codeClient);

            try {
                boolean deleted = Client_MachineDAO.deleteMachineFromClient(clientCode, machineCode);
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "La máquina se ha eliminado del cliente correctamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "La máquina no estaba asignada a este cliente.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la máquina del cliente.");
                e.printStackTrace();
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se ha seleccionado nada en la base de datos.");
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
