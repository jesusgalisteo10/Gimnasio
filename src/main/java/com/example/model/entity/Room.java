package com.example.model.entity;


import java.util.List;
import java.util.Objects;

public class Room {
    private int Code;
    private List<Machine> machines;

    public Room() {

    }

    public Room(int code) {
        Code = code;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Room)) return false;
        Room room = (Room) object;
        return getCode() == room.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public String toString() {
        return "Room{" +
                "Code=" + Code +
                ", machines=" + machines +
                '}';
    }


}