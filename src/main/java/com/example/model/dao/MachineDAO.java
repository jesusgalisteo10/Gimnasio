package com.example.model.dao;

import com.example.model.connection.ConnectionBD;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO implements DAO<Machine> {
    private static final String INSERT = "INSERT INTO Machine (RoomCode, MachineType) VALUES (?,?)";
    private static final String UPDATE = "UPDATE Machine SET RoomCode=?, MachineType=? WHERE MachineCode=?";
    private static final String FIND_BY_CODE = "SELECT MachineCode, RoomCode, MachineType FROM Machine WHERE MachineCode=?";
    private static final String DELETE = "DELETE FROM Machine WHERE MachineCode=?";
    private static final String FINDALL = "SELECT * FROM Machine";
    private static final String FINDMACHINEBYCLIENT = "SELECT m.MachineCode, m.MachineType, m.RoomCode FROM Machine m JOIN Client_Machine cm ON m.MachineCode = cm.MachineCode WHERE cm.ClientCode = ?";

    // Constructor vacío
    public MachineDAO() {
    }


    /**
     * Guarda una nueva entidad de máquina en la base de datos.
     *
     * @param entity la entidad de máquina a guardar
     * @return la entidad de máquina guardada
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Machine save(Machine entity) throws SQLException {
        if (entity == null) return null;
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, entity.getRoom().getCode());
            pst.setString(2, entity.getMachineType());
            pst.executeUpdate();
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setCode(generatedKeys.getInt(1));
                }
            }
        }
        return entity;
    }




    /**
     * Actualiza una entidad de máquina existente en la base de datos.
     *
     * @param entity  la entidad de máquina a actualizar
     * @return la entidad de máquina actualizada
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    public Machine update(Machine entity) throws SQLException {
        if (entity == null || entity.getRoom() == null) return null;
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(UPDATE)) {
            pst.setInt(1, entity.getRoom().getCode());
            pst.setString(2, entity.getMachineType());
            pst.setInt(3, entity.getCode());
            pst.executeUpdate();
        }
        return entity;
    }

    /**
     * Elimina una máquina de la base de datos por su código.
     *
     * @param machineCode el código de la máquina a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    @Override
    public boolean delete(int machineCode) throws SQLException {
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, machineCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Encuentra una máquina por su código.
     *
     * @param code el código de la máquina a encontrar
     * @return la máquina encontrada o null si no se encontró ninguna máquina
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    public static Machine findByMachineCode(Integer code) throws SQLException {
        Machine result = null;
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FIND_BY_CODE)) {
            pst.setInt(1, code);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    result = new Machine();
                    result.setCode(res.getInt("MachineCode"));
                    result.setRoom(RoomDAO.findByRoomCode(res.getInt("RoomCode")));
                    result.setMachineType(res.getString("MachineType"));
                }
            }
        }
        return result;
    }

    /**
     * Encuentra todas las máquinas en la base de datos.
     *
     * @return una lista de todas las máquinas
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    public static List<Machine> findAll() throws SQLException {
        List<Machine> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDALL)) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Machine machine = new Machine();
                    machine.setCode(res.getInt("MachineCode"));
                    // Carga ansiosa de la entidad Room (Habitación)
                    machine.setRoom(RoomDAO.findByRoomCode(res.getInt("RoomCode")));
                    machine.setMachineType(res.getString("MachineType"));
                    result.add(machine);
                }
            }
        }
        return result;
    }

    /**
     * Encuentra máquinas asociadas a un código de cliente específico.
     *
     * @param clientCode el código del cliente
     * @return una lista de máquinas asociadas al cliente
     */
    public static List<Machine> findByCode(int clientCode) {
        List<Machine> machines = new ArrayList<>();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDMACHINEBYCLIENT)) {
            pst.setInt(1, clientCode);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Machine m = new Machine();
                    m.setCode(res.getInt("MachineCode"));
                    m.setMachineType(res.getString("MachineType"));
                    Room room = new Room();
                    room.setCode(Integer.parseInt(res.getString("RoomCode")));
                    m.setRoom(room);
                    machines.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return machines;
    }

    /**
     * Crea y devuelve una nueva instancia de MachineDAO.
     *
     * @return nueva instancia de MachineDAO
     */
    public static MachineDAO build() {
        return new MachineDAO();
    }
}