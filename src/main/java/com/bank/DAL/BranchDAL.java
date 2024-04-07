package com.bank.DAL;

import com.bank.DTO.Branch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BranchDAL extends Manager{
    public BranchDAL() {
        super("branch",
                List.of("id",
                        "name",
                        "phone",
                        "address",
                        "headquater_id",
                        "deleted"));
    }

    public List<Branch> convertToBranches(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Branch(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        row.get(2), // phone
                        row.get(3), // address
                        Integer.parseInt(row.get(4)), // headquarter_id
                        Boolean.parseBoolean(row.get(5)) // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in BranchDAL.convertToBranches(): " + e.getMessage());
            }
            return new Branch();
        });
    }

    public int addBranch(Branch branch) {
        try {
            return create(branch.getId(),
                    branch.getName(),
                    branch.getPhone(),
                    branch.getAddress(),
                    branch.getHeadquarter_id(),
                    false
            ); // branch khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BranchDAL.addBranch(): " + e.getMessage());
        }
        return 0;
    }

    public int updateBranch(Branch branch) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(branch.getId());
            updateValues.add(branch.getName());
            updateValues.add(branch.getPhone());
            updateValues.add(branch.getAddress());
            updateValues.add(branch.getHeadquarter_id());
            updateValues.add(branch.isDeleted());
            return update(updateValues, "[id] = " + branch.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BranchDAL.updateBranch(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteBranch(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BranchDAL.deleteBranch(): " + e.getMessage());
        }
        return 0;
    }

    public List<Branch> searchBranches(String... conditions) {
        try {
            return convertToBranches(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BranchDAL.searchBranches(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Branch> getAllBranches() {
        try {
            return convertToBranches(executeProcedure("GetAllBranches"));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BranchDAL.getAllBranches(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
