package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientMachineController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView exitButton;
    @FXML
    private TableView<Machine> tableMachine;
    @FXML
    private TableColumn<Machine, String> colMachine;
    @FXML
    private TableColumn<Machine, Integer> colCodeMachine;
    @FXML
    private TableColumn<Machine, Integer> colRoom;
    @FXML
    private Text textName;
    private ObservableList<Machine> machineList;
    private Client client;
    /**
     * Inicializa y gestiona la visualización de las máquinas asociadas a un cliente específico.
     * Muestra una vista de tabla con las máquinas asignadas al cliente, incluyendo el código de máquina, tipo y sala.
     * Permite la navegación de vuelta a la página principal y actualiza dinámicamente la información del cliente.
     *
     * @param input los datos de entrada (objeto Client) pasados para inicializar el controlador
     * @throws Exception si hay un problema durante las operaciones de base de datos o la recuperación de información del cliente
     */
    @Override
    public void onOpen(Object input) throws Exception {
        client = (Client) input;
        showName();

        // Recupera las máquinas asociadas con el cliente de la base de datos
        List<Machine> machines = MachineDAO.findByCode(client.getCode());
        this.machineList = FXCollections.observableArrayList(machines);
        tableMachine.setItems(this.machineList);
    }
    @Override
    public void onClose(Object output) {
    }

    /**
     * Actualiza la visualización del nombre del cliente en la interfaz de usuario.
     */
    private void showName() {
        textName.setText(client.getName() + " " + client.getSurname());
    }

    /**
     * Navega de vuelta a la página principal de la aplicación.
     * Lanza una excepción si hay un problema durante el cambio de escena.
     *
     * @throws Exception si hay un problema durante el cambio de escena
     */
    @FXML
    private void goBack() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }

    /**
     * Inicializa las columnas de la tabla y sus respectivas fábricas de valores de celda para mostrar los detalles de la máquina.
     * Configura las columnas para el código de máquina, tipo y sala asociada.
     *
     * @param location la ubicación utilizada para resolver rutas relativas para el objeto raíz
     * @param resources los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCodeMachine.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getCode()).asObject());
        colMachine.setCellValueFactory(machine -> new SimpleStringProperty(machine.getValue().getMachineType()));
        colRoom.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getRoom().getCode()).asObject());
    }

}
