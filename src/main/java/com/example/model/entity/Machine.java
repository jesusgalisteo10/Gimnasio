package com.example.model.entity;

import java.util.Objects;

public class Machine {
    private int Code;
    private Room room;
    private String MachineType;

    public Machine() {

    }

    public Machine( Room room, String machineType) {
        this.room = room;
        MachineType = machineType;

    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getMachineType() {
        return MachineType;
    }

    public void setMachineType(String machineType) {
        MachineType = machineType;
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Machine)) return false;
        Machine machine = (Machine) object;
        return getCode() == machine.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public String toString() {
        return "Machine{" +
                "Code=" + Code +
                ", room=" + room +
                ", MachineType='" + MachineType + '\'' +
                '}';
    }
}