package com.bank.DAL;

import com.bank.DTO.Customer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAL extends Manager{
    public CustomerDAL() {
        super("customer",
                List.of("no",
                        "name",
                        "gender",
                        "birthdate",
                        "phone",
                        "address",
                        "email",
                        "deleted"));
    }

    public List<Customer> convertToCustomers(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Customer(
                        row.get(0), // no
                        row.get(1), // name
                        Boolean.parseBoolean(row.get(2)), // gender
                        Date.valueOf(row.get(3)), // birthday
                        row.get(4), // phone
                        row.get(5), // address
                        row.get(6), // email
                        Boolean.parseBoolean(row.get(7)) // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in CustomerDAL.convertToCustomers(): " + e.getMessage());
            }
            return new Customer();
        });
    }

    public int addCustomer(Customer customer) {
        try {
            return create(customer.getCustomerNo(),
                    customer.getName(),
                    customer.isGender(),
                    customer.getBirthdate(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getEmail(),
                    false
            ); // customer khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.addCustomer(): " + e.getMessage());
        }
        return 0;
    }

    public int updateCustomer(Customer customer) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(customer.getCustomerNo());
            updateValues.add(customer.getName());
            updateValues.add(customer.isGender());
            updateValues.add(customer.getBirthdate());
            updateValues.add(customer.getPhone());
            updateValues.add(customer.getAddress());
            updateValues.add(customer.getEmail());
            updateValues.add(customer.isDeleted());
            return update(updateValues, "[no] = '" + customer.getCustomerNo() + "'");
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.updateCustomer(): " + e.getMessage());
        }
        return 0;
    }



    public int deleteCustomer(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.deleteCustomer(): " + e.getMessage());
        }
        return 0;
    }

    public List<Customer> searchCustomers(String... conditions) {
        try {
            return convertToCustomers(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.searchCustomers(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
