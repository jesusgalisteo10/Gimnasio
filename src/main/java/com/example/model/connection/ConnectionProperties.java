package com.example.model.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// Indica que esta clase es el elemento raíz del XML y que su nombre raíz es "connection"
@XmlRootElement(name = "connection")
// Indica que JAXB debe acceder a los campos (variables de instancia) directamente
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties {

    // @XmlElement no es estrictamente necesario aquí si los nombres de los campos coinciden
    // con los nombres de las etiquetas XML y usamos XmlAccessType.FIELD.
    // Sin embargo, las dejo para mayor claridad y control.
    @XmlElement(name = "server")
    private String server;
    @XmlElement(name = "port")
    private String port;
    @XmlElement(name = "database")
    private String database;
    @XmlElement(name = "user")
    private String user;
    @XmlElement(name = "password")
    private String password;


    public ConnectionProperties() {
    }

    public String getURL() {
        return "jdbc:mysql://" + server + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    }

    // Getters y setters (JAXB los usa para serializar/deserializar, pero con XmlAccessType.FIELD

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + (password != null && !password.isEmpty() ? "********" : "[empty]") + '\'' + // No mostrar la contraseña real en logs
                '}';
    }
}
