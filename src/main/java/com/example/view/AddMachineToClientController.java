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


public class AddMachineToClientController extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addMachineToClientButton;
    @FXML
    private ComboBox<String> machineComboBox;
    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private ImageView goBackButton;

    private  Map<String, String> machinesMap;
    private  Map<String, String> clientsMap;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Inicializa el controlador con datos de clientes y máquinas para los menús desplegables.
     * Recupera y rellena los menús desplegables de clientes y máquinas con datos de la base de datos.
     * Maneja la asignación de máquinas a clientes basándose en la selección del usuario.
     *
     * @param location la ubicación utilizada para resolver rutas relativas de recursos
     * @param resources los recursos utilizados para localizar el objeto raíz
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Recupera todos los clientes de la base de datos y rellena el ComboBox de clientes.
        List<Client> clients = ClientDAO.build().findAll();
        clientsMap = new HashMap<>();
        for (Client client : clients) {
            clientsMap.put(String.valueOf(client.getCode()), client.getName());
        }
        clientComboBox.setItems(FXCollections.observableArrayList(clientsMap.values()));

        // Recupera todas las máquinas de la base de datos y rellena el ComboBox de máquinas.
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
     * Asigna la máquina seleccionada al cliente seleccionado.
     * Recupera los códigos de máquina y cliente seleccionados de sus respectivos menús desplegables,
     * luego inserta la asociación en la base de datos usando Client_MachineDAO.
     * Muestra un mensaje de error si la máquina o el cliente no se seleccionaron correctamente.
     *
     * @throws SQLException si hay un problema durante la operación de base de datos
     */
    @FXML
    private void AssignMachines() throws SQLException {
        String CodeMachine = "";
        String ValueMachine = machineComboBox.getValue();
        for (String key : machinesMap.keySet()) {
            if (machinesMap.get(key).equals(ValueMachine)) {
                CodeMachine = key;
            }
        }

        String CodeClient = "";
        String ValueClient = clientComboBox.getValue();
        for (String key : clientsMap.keySet()) {
            if (clientsMap.get(key).equals(ValueClient)) {
                CodeClient = key;
            }
        }

        int machineCode = 0;
        int clientCode = 0;
        // Valida y parsea los códigos a enteros.
        if (CodeMachine != null && CodeMachine.matches("\\d+") && CodeClient != null && CodeClient.matches("\\d+")) {
            machineCode = Integer.parseInt(CodeMachine);
            clientCode = Integer.parseInt(CodeClient);

            // Inserta la asociación de máquina a cliente en la base de datos.
            Client_MachineDAO.insertMachineToClient(clientCode, machineCode);

        } else {
            // Muestra una alerta de error si la máquina o el cliente no se seleccionaron correctamente.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No se ha seleccionado nada en la base de datos");
            alert.show();
        }
    }



}

