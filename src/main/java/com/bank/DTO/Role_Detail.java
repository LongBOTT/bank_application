package com.bank.DTO;


import java.util.Date;

public class Role_Detail {
    private int role_id;
    private int staff_id;
    private Date entry_date;
    private double salary;

    public Role_Detail() {
    }

    public Role_Detail(int role_id, int staff_id, Date entry_date, double salary) {
        this.role_id = role_id;
        this.staff_id = staff_id;
        this.entry_date = entry_date;
        this.salary = salary;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public Date getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return role_id + " | " +
                staff_id + " | " +
                entry_date + " | " +
                salary;
    }
}
