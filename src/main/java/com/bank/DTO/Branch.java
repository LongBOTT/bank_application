package com.bank.DTO;

public class Branch {
    private int id;
    private String name;
    private String phone;
    private String address;
    private int  headquarter_id;
    private boolean deleted;

    public Branch() {
    }

    public Branch(int id, String name, String phone, String address, int headquarter_id, boolean deleted) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.headquarter_id = headquarter_id;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHeadquarter_id() {
        return headquarter_id;
    }

    public void setHeadquarter_id(int headquarter_id) {
        this.headquarter_id = headquarter_id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return id + " | " +
                name + " | " +
                phone + " | " +
                address;
    }
}
