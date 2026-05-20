package com.example.model.dao;

import com.example.model.connection.ConnectionBD;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Client_MachineDAO {
    // Consulta SQL para insertar una relación cliente-máquina en la base de datos
    private static final String INSERTCM = "INSERT INTO client_machine (ClientCode, MachineCode) VALUES (?, ?)";
    // Consulta SQL para eliminar una relación cliente-máquina de la base de datos
    private static final String DELETEMC = "DELETE FROM client_machine WHERE ClientCode = ? AND MachineCode = ?;";
    // Consulta SQL para obtener los códigos de máquinas asignados a un cliente concreto
    private static final String SELECT_MACHINES_BY_CLIENT = "SELECT MachineCode FROM client_machine WHERE ClientCode = ?";

    // Constructor vacío que inicializa una instancia de Client_MachineDAO
    public Client_MachineDAO() {
    }

    /**
     * Crea y devuelve una nueva instancia de Client_MachineDAO.
     *
     * @return nueva instancia de Client_MachineDAO
     */
    public static Client_MachineDAO build() {
        return new Client_MachineDAO();
    }

    /**
     * Inserta una relación entre una máquina y un cliente en la tabla client_machine.
     *
     * @param machineCode el código de la máquina a insertar
     * @param clientCode  el código del cliente a insertar
     */
    public static void insertMachineToClient(int machineCode, int clientCode) {
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(INSERTCM)) {
            pst.setInt(1, machineCode);
            pst.setInt(2, clientCode);

            pst.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000") && e.getErrorCode() == 1062) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error: Duplicate entry found.");
                alert.show();
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina una relación entre un cliente y una máquina de la tabla 'client_machine'.
     *
     * @param clientCode  el código del cliente cuya relación se va a eliminar
     * @param machineCode el código de la máquina cuya relación se va a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    public static boolean deleteMachineFromClient(int clientCode, int machineCode) throws SQLException {
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(DELETEMC)) {
            pst.setInt(1, clientCode);
            pst.setInt(2, machineCode);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Obtiene la lista de códigos de máquinas asignadas a un cliente.
     *
     * @param clientCode Código del cliente
     * @return Lista de enteros con los códigos de sus máquinas
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public static List<Integer> findMachinesByClient(int clientCode) throws SQLException {
        List<Integer> machineCodes = new ArrayList<>();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(SELECT_MACHINES_BY_CLIENT)) {
            pst.setInt(1, clientCode);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    machineCodes.add(rs.getInt("MachineCode"));
                }
            }
        }
        return machineCodes;
    }
}
