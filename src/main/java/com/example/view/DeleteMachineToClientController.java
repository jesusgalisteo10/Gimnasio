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
import java.util.ArrayList;
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
    private Map<String, String> clientsMap;

    @Override
    public void onOpen(Object input) throws Exception {
    }

    @Override
    public void onClose(Object output) {
    }

    /**
     * Inicializa el controlador al cargar el FXML.
     * Recupera todos los clientes de la base de datos para rellenar su combo.
     * Crea un mapa con todas las máquinas disponibles para traducir sus códigos a nombres.
     * Configura un escuchador dinámico para que el combo de máquinas solo muestre las que posee el cliente seleccionado.
     *
     * @param location la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce
     * @param resources los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Rellena el cuadro combinado de clientes con los nombres de los clientes.
        List<Client> clients = ClientDAO.build().findAll();
        clientsMap = new HashMap<>();
        for (Client client : clients) {
            clientsMap.put(String.valueOf(client.getCode()), client.getName());
        }
        clientComboBox.setItems(FXCollections.observableArrayList(clientsMap.values()));

        // 2. Cargamos el mapa de todas las máquinas en memoria para traducir los códigos a textos reales
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

        // 3 Bloqueamos el combo para evitar el recuadro blanco,
        // pero forzamos por CSS que la opacidad visual sea del 100% para que se lea perfecto.
        machineComboBox.setDisable(true);
        machineComboBox.setPromptText("Elige un cliente primero");
        machineComboBox.setStyle("-fx-opacity: 1.0; -fx-background-color: #E0E0E0;");

        // 4. ESCUCHADOR DINÁMICO: Al cambiar el cliente seleccionado, se filtran sus máquinas en tiempo real
        clientComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String codeClient = "";
                for (String key : clientsMap.keySet()) {
                    if (clientsMap.get(key).equals(newValue)) {
                        codeClient = key;
                        break;
                    }
                }

                if (!codeClient.isEmpty()) {
                    try {
                        int clientCode = Integer.parseInt(codeClient);
                        // Buscamos solo las máquinas que pertenecen a este cliente en la BD
                        List<Integer> assignedMachineIds = Client_MachineDAO.findMachinesByClient(clientCode);

                        // Convertimos esos códigos numéricos a los nombres correspondientes (ej: "pesas")
                        List<String> assignedMachineNames = new ArrayList<>();
                        for (Integer machineId : assignedMachineIds) {
                            String name = machinesMap.get(String.valueOf(machineId));
                            if (name != null) {
                                assignedMachineNames.add(name);
                            }
                        }

                        // Actualizamos el ComboBox de máquinas con la lista filtrada
                        machineComboBox.setItems(FXCollections.observableArrayList(assignedMachineNames));

                        // Habilitamos el combo con su estilo normal
                        machineComboBox.setDisable(false);
                        machineComboBox.setStyle("-fx-opacity: 1.0;");

                        // Si el cliente no tiene ninguna máquina asignada
                        if (assignedMachineNames.isEmpty()) {
                            machineComboBox.setPromptText("Sin máquinas asignadas");
                        } else {
                            machineComboBox.setPromptText("Selecciona Máquina");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las máquinas del cliente.");
                    }
                }
            } else {
                machineComboBox.getItems().clear();
                machineComboBox.setDisable(true);
                machineComboBox.setPromptText("Elige un cliente primero");
                machineComboBox.setStyle("-fx-opacity: 1.0; -fx-background-color: #E0E0E0;");
            }
        });
    }

    /**
     * Maneja la acción de eliminar una máquina de un cliente.
     * Recupera los códigos de máquina y cliente seleccionados de los cuadros combinados, los valida,
     * y elimina la asociación de la base de datos.
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

                    // Limpiar selección tras borrar para obligar a refrescar
                    machineComboBox.setValue(null);
                    clientComboBox.setValue(null);
                    machineComboBox.setDisable(true);
                    machineComboBox.setPromptText("Elige un cliente primero");
                    machineComboBox.setStyle("-fx-opacity: 1.0; -fx-background-color: #E0E0E0;");
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