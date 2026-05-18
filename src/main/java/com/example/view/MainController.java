package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
import com.example.model.entity.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addClient;
    @FXML
    private Button deleteClient;
    @FXML
    private Button showMachines;
    @FXML
    private Button showMachinesToClient;
    @FXML
    private Button addMachineToClient;
    @FXML
    private Button deleteMachineToClient;
    @FXML
    private TableView<Client> tableInfo;
    @FXML
    private TableColumn<Client, String> colCode;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, String> colSurname;
    @FXML
    private TableColumn<Client, String> colDNI;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TableColumn<Client, String> colSex;
    @FXML
    private ImageView exitButton;
    @FXML
    private TableColumn<?, ?> colDeleteClient;
    @FXML
    private TableColumn<?, ?> colAddMachine;
    @FXML
    private ObservableList<Client> clientList;

    /**
     * metodo invocado cuando se abre la vista asociada a este controlador.
     * Carga todos los clientes de la base de datos y los muestra en la tabla.
     *
     * @param input El objeto de entrada (típicamente no usado directamente en este contexto).
     * @throws Exception Si ocurre un error al cargar los clientes de la base de datos.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Inicializa el DAO para acceder a la información del cliente.
        ClientDAO cdao = new ClientDAO();

        // Recupera todos los clientes de la base de datos.
        List<Client> clients = cdao.findAll();

        // Convierte la lista de clientes a una lista observable para JavaFX (necesario para TableView).
        this.clientList = FXCollections.observableArrayList(clients);

        // Establece la lista observable como los elementos a mostrar en la tabla (tableInfo).
        tableInfo.setItems(clientList);
    }



    @Override
    public void onClose(Object output) {

    }

    /**
     * Valida si un número de DNI tiene el formato correcto.
     *
     * @param dni El número de DNI a validar.
     * @return true si el DNI tiene el formato correcto, false en caso contrario.
     */
    private boolean validateDNI(String dni) {
        // Expresión regular para validar el DNI español: 8 dígitos seguidos de una letra.
        String dniPattern = "\\d{8}[a-zA-Z]";
        return dni.matches(dniPattern);
    }
    /**
     * Valida si una dirección de correo electrónico tiene el formato correcto.
     *
     * @param email La dirección de correo electrónico a validar.
     * @return true si la dirección de correo electrónico tiene el formato correcto, false en caso contrario.
     */
    private boolean validateEmail(String email) {
        // Expresion para regular el email
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailPattern);
    }


    /**
     * Inicializa el controlador.
     * Configura las columnas de la tabla para permitir la edición de la información del cliente
     * y maneja los eventos de confirmación de edición.
     *
     * @param location La ubicación utilizada para resolver rutas relativas para el objeto raíz.
     * @param resources Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Habilita la edición de la tabla.
        tableInfo.setEditable(true);

        // Configura la fábrica de filas para manejar eventos de doble clic en las filas.
        tableInfo.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Client client = tableInfo.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 3 && (!row.isEmpty())) {
                    try {
                        App.currentController.changeScene(Scenes.MACHINESTOCLIENT, client);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

        //  Configuración de columnas para edición

        colCode.setCellValueFactory(client -> new SimpleStringProperty(String.valueOf(client.getValue().getCode())));
        colCode.setOnEditCommit(event -> {
            try {
                // Verifica si el nuevo valor es diferente al valor antiguo.
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Valida la longitud del nuevo valor.
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setCode(Integer.parseInt(event.getNewValue()));
                    ClientDAO.build().update(client);
                } else {
                    // Muestra un error si la longitud excede los 60 caracteres.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Muestra un error si se introduce un formato de número inválido
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configura la columna 'Name' (Nombre) para el nombre del cliente.
        colName.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getName()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            try {
                // Verifica si el nuevo valor es diferente al valor antiguo.
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Valida la longitud del nuevo valor.
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setName(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Muestra un error si la longitud excede los 60 caracteres.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Muestra un error si se introduce un formato de número inválido
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configura la columna 'Surname' (Apellido) para el apellido del cliente.
        colSurname.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getSurname()));
        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        colSurname.setOnEditCommit(event -> {
            try {
                // Verifica si el nuevo valor es diferente al valor antiguo.
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Valida la longitud del nuevo valor.
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setSurname(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Muestra un error si la longitud excede los 60 caracteres.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Muestra un error si se introduce un formato de número inválido
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configura la columna 'DNI' para el DNI del cliente.
        colDNI.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getDni()));
        colDNI.setCellFactory(TextFieldTableCell.forTableColumn());
        colDNI.setOnEditCommit(event -> {
            boolean shouldUpdate = true;
            try {
                String newValue = event.getNewValue().trim();

                // Valida el formato y la longitud del DNI.
                if (newValue.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "DNI cannot be empty.");
                    shouldUpdate = false;
                } else if (!validateDNI(newValue)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid DNI format.");
                    shouldUpdate = false;
                } else if (newValue.equals(event.getOldValue())) {
                    shouldUpdate = false;
                } else if (newValue.length() > 9) {
                    showAlert(Alert.AlertType.ERROR, "Error", "DNI cannot exceed 9 characters including the letter.");
                    shouldUpdate = false;
                }

                if (shouldUpdate) {
                    Client client = event.getRowValue();
                    client.setDni(newValue);
                    ClientDAO.build().update(client);
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid value entered.");
                shouldUpdate = false;
            }
        });

        // Configura la columna 'Email' para el correo electrónico del cliente
        colEmail.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getEmail()));
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            boolean shouldUpdate = true;
            try {
                String newValue = event.getNewValue().trim();

                // Valida el formato y la longitud del correo electrónico
                if (newValue.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email cannot be empty.");
                    shouldUpdate = false;
                } else if (!validateEmail(newValue)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
                    shouldUpdate = false;
                } else if (newValue.equals(event.getOldValue())) {
                    shouldUpdate = false;
                } else if (newValue.length() > 60) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email cannot exceed 60 characters.");
                    shouldUpdate = false;
                }

                if (shouldUpdate) {
                    Client client = event.getRowValue();
                    client.setEmail(newValue);
                    ClientDAO.build().update(client);
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid value entered.");
                shouldUpdate = false;
            }
        });

        // Configura la columna 'Sex' (Sexo) para el sexo del cliente.
        colSex.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getSex()));
        colSex.setCellFactory(TextFieldTableCell.forTableColumn());
        colSex.setOnEditCommit(event -> {
            try {
                // Verifica si el nuevo valor es diferente al valor antiguo
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Valida la longitud del nuevo valor.
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setSex(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Muestra un error si la longitud excede los 60 caracteres
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Muestra un error si se introduce un formato de número inválido
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });
    }


    /**
     * Abre la escena para añadir un nuevo cliente llamando al diálogo modal correspondiente.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openAddClient() throws Exception {
        App.currentController.openModal(Scenes.ADDCLIENT, "Adding client...", this, null);
    }

    /**
     * Abre la escena para eliminar un cliente llamando al diálogo modal correspondiente.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openDeleteClient() throws Exception {
        App.currentController.openModal(Scenes.DELETECLIENT, "Deleting client...", this, null);
    }

    /**
     * Abre la escena para mostrar las máquinas asociadas a los clientes.
     *
     * @throws Exception Si ocurre un error al cambiar la escena.
     */
    public void openShowMachines() throws Exception {
        App.currentController.changeScene(Scenes.SHOWMACHINES, null);
    }

    /**
     * Opens the modal dialog to add a machine to a client.
     *
     * @throws Exception If there is an error while opening the modal dialog.
     */
    public void openAddMachineClient() throws Exception {
        App.currentController.openModal(Scenes.ADDMACHINETOCLIENT, "Adding machine to client...", this, null);
    }

    /**
     * Abre el diálogo modal para añadir una máquina a un cliente.
     *
     * @throws Exception Si ocurre un error al abrir el diálogo modal.
     */
    public void openDeleteMachineClient() throws Exception {
        App.currentController.openModal(Scenes.DELETEMACHINETOCLIENT, "Deleting associated machine...", this, null);
    }



    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}