package com.bank.DTO;

import java.util.Date;

public class Customer {
    private String no;
    private String name;
    private boolean gender;
    private Date birthdate;
    private String phone;
    private String address;
    private String email;
    private boolean deleted;
    public Customer() {
    }

    public Customer(String no, String name, boolean gender, Date birthdate, String phone,
                    String address, String email, boolean deleted) {
        this.no = no;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.deleted = deleted;
    }

    public String getCustomerNo() {
        return no;
    }

    public void setCustomerNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        String gender1 = gender? "Nữ" : "Nam";
        return no + " | " +  // can cuoc cong dan
                name + " | " + // tên
                phone + " | " +// số điện thoại
                email; // email
    }




}
