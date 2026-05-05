package com.example.model.dao;

import com.example.model.connection.ConnectionBD;
import com.example.model.entity.Room;
import com.example.model.entity.Machine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private static final String INSERT = "INSERT INTO room (RoomCode) VALUES (?)";
    private static final String UPDATE = "UPDATE room SET RoomCode=? WHERE RoomCode=?";
    private static final String FINDALL = "SELECT RoomCode FROM room";
    private static final String FINDBYCODE = "SELECT RoomCode FROM room WHERE RoomCode=?";
    private static final String DELETE = "DELETE FROM room WHERE RoomCode=?";
    private static final String FINDMACHINESBYROOM = "SELECT * FROM Machine WHERE RoomCode = ?;";

    /**
     * Guarda una nueva entidad de sala en la base de datos.
     *
     * @param entity la entidad de sala a guardar
     * @return la entidad de sala guardada
     */
    public static Room save(Room entity) {
        Room result = entity;
        if (entity == null || entity.getCode() == 0) return result;
        try {
            if (findByRoomCode(entity.getCode()) == null) {
                // INSERT
                try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, entity.getCode());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Actualiza una entidad de sala existente en la base de datos.
     *
     * @param entity la entidad de sala a actualizar
     * @return la entidad de sala actualizada
     */
    public Room update(Room entity) {
        Room result = new Room();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(UPDATE)) {
            pst.setInt(1, entity.getCode());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Elimina una sala de la base de datos por su código.
     *
     * @param roomCode código de la sala a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    public static boolean delete(int roomCode) throws SQLException {
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, roomCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Encuentra una sala por su código.
     *
     * @param code el código de la sala a encontrar
     * @return la sala encontrada o null si no se encontró ninguna sala
     */
    public static Room findByRoomCode(Integer code) {
        Room result = null;
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDBYCODE)) {
            pst.setInt(1, code);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Room();
                result.setCode(res.getInt("RoomCode"));

                //  Obtener máquinas asociadas a la sala
                ArrayList<Machine> machines = new ArrayList<>();
                try (PreparedStatement pstMachine = ConnectionBD.getConnection().prepareStatement(FINDMACHINESBYROOM)) {
                    pstMachine.setInt(1, code);
                    ResultSet resMachines = pstMachine.executeQuery();
                    while (resMachines.next()) {
                        Machine machine = new Machine();
                        machine.setCode(resMachines.getInt("MachineCode"));
                        machine.setMachineType(resMachines.getString("MachineType"));
                        machines.add(machine);
                    }
                    resMachines.close();
                }
                result.setMachines(machines);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Encuentra todas las salas en la base de datos.
     *
     * @return una lista de todas las salas
     */
    public static List<Room> findAll() {
        List<Room> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDALL)) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Room room = new Room();
                room.setCode(res.getInt("RoomCode"));
                result.add(room);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Crea y devuelve una nueva instancia de RoomDAO.
     *
     * @return nueva instancia de RoomDAO
     */
    public static RoomDAO build() {
        return new RoomDAO();
    }
}
