package com.example.model.entity;

import java.util.List;
import java.util.Objects;

public class Client extends Machine {
    private int Code;
    private List<Machine> machines;
    private String Name;
    private String Surname;
    private String Email;
    private String Password;
    private String Dni;
    private String Sex;

    public Client() {
    }




    public Client( String name, String surname, String email, String password, String dni, String sex) {
        this.machines = machines;
        Name = name;
        Surname = surname;
        Email = email;
        Password = password;
        Dni = dni;
        Sex = sex;
    }



    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Client)) return false;
        Client client = (Client) object;
        return getCode() == client.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public String toString() {
        return "Client{" +
                "Code=" + Code +
                ", machines=" + machines +
                ", Name='" + Name + '\'' +
                ", Surname='" + Surname + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", Dni='" + Dni + '\'' +
                ", Sex='" + Sex + '\'' +
                '}';
    }

}

