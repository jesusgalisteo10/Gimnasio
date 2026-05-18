package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ShowEditMachinesController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView exitButton;
    @FXML
    private Button addMachine;
    @FXML
    private Button addRoom;
    @FXML
    private Button deleteMachine;
    @FXML
    private Button deleteRoom;
    @FXML
    private TableView<Machine> tableMachine;
    @FXML
    private TableColumn<Machine, String> colMachine;
    @FXML
    private TableColumn<Machine, Integer> colCodeMachine;
    @FXML
    private TableColumn<Machine, Integer> colRoom;
    private ObservableList<Machine> machineList;


    /**
     * Inicializa la vista asociada con este controlador cuando se abre.
     * Recupera todas las máquinas de la base de datos y las carga en la vista de tabla.
     *
     * @param input Objeto de entrada (generalmente no se usa directamente en este contexto).
     * @throws Exception Si ocurre un error al recuperar máquinas de la base de datos.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Recupera todas las máquinas de la base de datos.
        List<Machine> machines = MachineDAO.findAll();

        // Convierte la lista de máquinas en una lista observable para JavaFX.
        this.machineList = FXCollections.observableArrayList(machines);

        // Establece la lista observable como los elementos a mostrar en la vista de tabla (tableMachine).
        tableMachine.setItems(this.machineList);
    }


    @Override
    public void onClose(Object output) {

    }

    /**
     * Navega de vuelta a la página principal.
     *
     * @throws Exception Si ocurre un error al cambiar la escena a la página principal.
     */
    @FXML
    public void goBack() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }


    /**
     * Inicializa el controlador cuando se carga la vista asociada.
     * Configura la vista de tabla para máquinas con columnas editables.
     *
     * @param location La ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si la ubicación no se conoce.
     * @param resources Los recursos utilizados para localizar el objeto raíz, o null si el objeto raíz no fue localizado.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hace que la vista de tabla sea editable, permitiendo al usuario modificar los datos directamente en la tabla.
        tableMachine.setEditable(true);

        // Configura la columna para el código de la máquina.
        colCodeMachine.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getCode()).asObject());


        // Configura la columna para el código de la sala.
        colRoom.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getRoom().getCode()).asObject());
        colRoom.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colRoom.setOnEditCommit(event -> {
            try {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                Machine machine = MachineDAO.findByMachineCode(event.getRowValue().getCode());
                Room room = machine.getRoom();
                if (event.getNewValue().longValue() <= 50) {
                    room.setCode(event.getNewValue());
                    MachineDAO.build().update(machine);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, the field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, the entered value is not a valid number");
                alert.show();
            }
        });

        // Configura la columna para el tipo de máquina.
        colMachine.setCellValueFactory(machine -> new SimpleStringProperty(machine.getValue().getMachineType()));
        colMachine.setCellFactory(TextFieldTableCell.forTableColumn());
        colMachine.setOnEditCommit(event -> {
            try {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }

                if (event.getNewValue().length() <= 60) {
                    Machine machine = event.getRowValue();
                    machine.setMachineType(event.getNewValue());
                    MachineDAO.build().update(machine);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, the field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, the entered value is not a valid number");
                alert.show();
            }
        });
    }


    /**
     * Abre un diálogo modal para añadir una nueva sala.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openAddRoom() throws Exception {
        App.currentController.openModal(Scenes.ADDROOM, "Adding room...", this, null);
    }

    /**
     * Abre un diálogo modal para añadir una nueva máquina.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openAddMachine() throws Exception {
        App.currentController.openModal(Scenes.ADDMACHINE, "Adding machine...", this, null);
    }

    /**
     * Abre un diálogo modal para eliminar una máquina existente.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openDeleteMachine() throws Exception {
        App.currentController.openModal(Scenes.DELETEMACHINE, "Deleting machine...", this, null);
    }

    /**
     * Abre un diálogo modal para eliminar una sala existente.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openDeleteRoom() throws Exception {
        App.currentController.openModal(Scenes.DELETEROOM, "Deleting room...", this, null);
    }



}

