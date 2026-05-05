package com.example.model.dao;

import com.example.model.connection.ConnectionBD;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO  implements DAO<Client>{
    private static final String FINDALL = "SELECT * FROM client";
    private static final String FINDBYCODE = "SELECT * FROM client WHERE Clientcode=?";
    private static final String INSERT = "INSERT INTO client ( Name, Surname, Email, Password, DNI, Sex) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE client SET Name=?, Surname=?, Email=?, Password=?, DNI=?, Sex=? WHERE ClientCode=?";
    private static final String DELETE = "DELETE FROM client WHERE ClientCode=?";

    //  Constructor vacío
    public ClientDAO() {
    }

    /**
     * Guarda una nueva entidad de cliente en la base de datos.
     *
     * @param entity la entidad de cliente a guardar
     * @return la entidad de cliente guardada
     */
    @Override
    public Client save(Client entity) {
        Client result = new Client();
        if (entity == null || entity.getCode() != 0) return result;
        try {
            if (findByClientCode(entity.getCode()) == null) {
                // INSERT CLIENT
                try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(INSERT)) {
                    pst.setString(1, entity.getName());
                    pst.setString(2, entity.getSurname());
                    pst.setString(3, entity.getEmail());
                    pst.setString(4, hashPassword(entity.getPassword()));
                    pst.setString(5, entity.getDni());
                    pst.setString(6, entity.getSex());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Hashea una contraseña usando el algoritmo SHA-256.
     *
     * @param password la contraseña a hashear
     * @return la contraseña hasheada
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza una entidad de cliente existente en la base de datos.
     *
     * @param entity la entidad de cliente a actualizar
     * @return la entidad de cliente actualizada
     */
    public Client update(Client entity) {
        Client result = new Client();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(UPDATE)) {
            pst.setString(1, entity.getName());
            pst.setString(2, entity.getSurname());
            pst.setString(3, entity.getEmail());
            pst.setString(4, entity.getPassword());
            pst.setString(5, entity.getDni());
            pst.setString(6, entity.getSex());
            pst.setString(7, String.valueOf(entity.getCode()));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Crea y devuelve una nueva instancia de ClientDAO.
     *
     * @return nueva instancia de ClientDAO
     */
    public static ClientDAO build() {
        return new ClientDAO();
    }

    /**
     * Elimina un cliente de la base de datos por su código.
     *
     * @param clientCode el código del cliente a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException si ocurre un error de acceso a la base de datos
     */
    @Override
    public boolean delete(int clientCode) throws SQLException {
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, clientCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Encuentra un cliente por su código.
     *
     * @param code el código del cliente a encontrar
     * @return el cliente encontrado o null si no se encontró ningún cliente
     */
    public static Client findByClientCode(Integer code) {
        Client result = null;
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDBYCODE)) {
            pst.setInt(1, code);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    Client c = new ClientLazy();
                    c.setCode(res.getInt("ClientCode"));
                    c.setName(res.getString("Name"));
                    c.setSurname(res.getString("Surname"));
                    c.setEmail(res.getString("Email"));
                    c.setPassword(res.getString("Password"));
                    c.setDni(res.getString("DNI"));
                    c.setSex(res.getString("Sex"));
                    result = c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Encuentra todos los clientes en la base de datos.
     *
     * @return una lista de todos los clientes
     */
    public static List<Client> findAll() {
        List<Client> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDALL)) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Client c = new ClientLazy();
                    c.setCode(res.getInt("ClientCode"));
                    c.setName(res.getString("Name"));
                    c.setSurname(res.getString("Surname"));
                    c.setEmail(res.getString("Email"));
                    c.setPassword(res.getString("Password"));
                    c.setDni(res.getString("DNI"));
                    c.setSex(res.getString("Sex"));
                    result.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

class ClientLazy extends Client {
    private static final String FINDMACHINESBYCLIENTS = "SELECT * FROM machine m, client_machine cm, client c WHERE m.MachineCode = cm.MachineCode AND cm.ClientCode = c.ClientCode AND c.ClientCode = ?;";

    public ClientLazy() {
    }

    public ClientLazy(String name, String surname, String email, String password, String dni, String sex) {
        super(name, surname, email, password, dni, sex);
    }

    /**
     * Obtiene las máquinas asociadas al cliente.
     *
     * @return una lista de máquinas asociadas al cliente
     */
    @Override
    public List<Machine> getMachines() {
        if (super.getMachines() == null) {
            List<Machine> result = new ArrayList<>();
            try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(FINDMACHINESBYCLIENTS)) {
                pst.setInt(1, getCode());
                try (ResultSet res = pst.executeQuery()) {
                    while (res.next()) {
                        Client c = new Client();
                        c.setCode(res.getInt("ClientCode"));
                        c.setName(res.getString("Name"));
                        c.setSurname(res.getString("Surname"));
                        c.setEmail(res.getString("Email"));
                        c.setPassword(res.getString("Password"));
                        c.setDni(res.getString("DNI"));
                        c.setSex(res.getString("Sex"));
                        result.add(c);
                    }
                }
                super.setMachines(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return super.getMachines();
    }
}
