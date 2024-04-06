package com.bank.DAL;

import com.bank.DTO.Headquarter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeadquarterDAL extends Manager{
    public HeadquarterDAL() {
        super("headquarter",
                List.of("id",
                        "name",
                        "address"));
    }

    public List<Headquarter> convertToHeadquarters(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Headquarter(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        row.get(2) // address
                );
            } catch (Exception e) {
                System.out.println("Error occurred in HeadquarterDAL.convertToHeadquarters(): " + e.getMessage());
            }
            return new Headquarter();
        });
    }

    public int addHeadquarter(Headquarter headquarter) {
        try {
            return create(headquarter.getId(),
                    headquarter.getName(),
                    headquarter.getAddress()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in HeadquarterDAL.addHeadquarter(): " + e.getMessage());
        }
        return 0;
    }

    public int updateHeadquarter(Headquarter headquarter) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(headquarter.getId());
            updateValues.add(headquarter.getName());
            return update(updateValues, "[id] = " + headquarter.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in HeadquarterDAL.updateHeadquarter(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteHeadquarter(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in HeadquarterDAL.deleteHeadquarter() " + e.getMessage());
        }
        return 0;
    }

    public List<Headquarter> searchHeadquarters(String... conditions) {
        try {
            return convertToHeadquarters(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in HeadquarterDAL.searchHeadquarters(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
