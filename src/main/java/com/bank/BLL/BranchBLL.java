package com.bank.BLL;

import com.bank.DAL.BranchDAL;
import com.bank.DTO.Branch;
import com.bank.DTO.Decentralization;
import com.bank.DTO.Function;
import com.bank.DTO.Staff;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.util.*;

public class BranchBLL extends Manager<Branch> {
    private BranchDAL branchDAL;

    private List<Branch> branchListAll ;

    public BranchBLL() {
        branchDAL = new BranchDAL();
        branchListAll = branchDAL.getAllBranches();
    }

    public List<Branch> getBranchListAll() {
        return branchListAll;
    }

    public void setBranchListAll(List<Branch> branchListAll) {
        this.branchListAll = branchListAll;
    }

    public BranchDAL getBranchDAL() {
        return branchDAL;
    }

    public void setBranchDAL(BranchDAL branchDAL) {
        this.branchDAL = branchDAL;
    }

    public Object[][] getData() {
        return getData(branchDAL.searchBranches("[deleted] = 0"));
    }


    public Pair<Boolean, String> addBranch(Branch branch) {
        Pair<Boolean, String> result = validateBranchAll(branch);
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        if (branchDAL.addBranch(branch) == 0)
            return new Pair<>(false, "Thêm chi nhánh không thành công.");

        return new Pair<>(true, "Thêm chi nhánh thành công.");
    }


    public Pair<Boolean, String> updateBranch(Branch oldBranch, Branch newBranch) {
        List<String> errorMessages = new ArrayList<>();

        if(!Objects.equals(oldBranch.getName(),newBranch.getName())){
            Pair<Boolean, String> result = validateName(newBranch.getName());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldBranch.getPhone(),newBranch.getPhone())){
            Pair<Boolean, String> result = validatePhone(newBranch.getPhone());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if(!Objects.equals(oldBranch.getAddress(),newBranch.getAddress())){
            Pair<Boolean, String> result = validateAddress(newBranch.getAddress());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        if (branchDAL.updateBranch(newBranch) == 0)
            return new Pair<>(false, "Cập nhật chi nhánh không thành công.");

        return new Pair<>(true, "Cập nhật chi nhánh thành công.");
    }

    public Pair<Boolean, String> deleteBranch(Branch branch) {
        Pair<Boolean, String> result;

        result = checkStaff(branch);
        if(result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (branchDAL.deleteBranch("id = " + branch.getId()) == 0)
            return new Pair<>(false, "Xoá chi nhánh không thành công.");

        return new Pair<>(true, "Xoá chi nhánh thành công.");
    }

    public List<Branch> searchBranches(String... conditions) {
        return branchDAL.searchBranches(conditions);
    }

    public List<Branch> findBranchs(String key, String value) {
        List<Branch> list = new ArrayList<>();
        List<Branch> branchList = branchDAL.searchBranches("[deleted] = 0");
        for (Branch branch : branchList) {
            if (getValueByKey(branch, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(branch);
            }
        }
        return list;
    }

    public List<Branch> findAllBranchs(String key, String value) {
        List<Branch> list = new ArrayList<>();
        List<Branch> branchList = branchListAll;
        for (Branch branch : branchList) {
            if (getValueByKey(branch, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(branch);
            }
        }
        return list;
    }

//    public List<Branch> findBranchsBy(Map<String, Object> conditions) {
//        List<Branch> branchs = branchDAL.searchBranches("[deleted] = 0");
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            branchs = findObjectsBy(entry.getKey(), entry.getValue(), branchs);
//        return branchs;
//    }

    public Pair<Boolean, String> validateBranchAll(Branch branch) {
        Pair<Boolean, String> result;


        result = validateName(branch.getName());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validatePhone(branch.getPhone());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateAddress(branch.getAddress());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = exists(branch);
        if (result.getKey())
            return new Pair<>(false, result.getValue());

        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> exists(Branch newBranch) {
        List<Branch> branchs = branchDAL.searchBranches("[name] = '" + newBranch.getName() + "'", "[deleted] = 0");
        if (!branchs.isEmpty()) {
            return new Pair<>(true, "Tên chi nhánh đã tồn tại.");
        }

        branchs = branchDAL.searchBranches("[phone] = '" + newBranch.getPhone() + "'", "[deleted] = 0");
        if (!branchs.isEmpty()) {
            return new Pair<>(true, "Số điện thoại chi nhánh đã tồn tại.");
        }

        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên chi nhánh không được bỏ trống.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên chi nhánh không không được chứa số.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên chi nhánh không không được chứa ký tự đặc biệt.");
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> validatePhone(String phone) {
        if (phone.isBlank())
            return new Pair<>(false, "Số điện thoại chi nhánh không được bỏ trống.");
        if (!VNString.checkFormatPhone(phone))
            return new Pair<>(false, "Số điện thoại chi nhánh phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc \\{\\\\3, 5, 7, 8, 9\\}\\\\.");


        List<Branch> branchs = branchDAL.searchBranches("[phone] = '" +phone + "'", "[deleted] = 0");
        if (!branchs.isEmpty()) {
            return new Pair<>(false, "Số điện thoại chi nhánh đã tồn tại.");
        }

        return new Pair<>(true, phone);
    }

    public Pair<Boolean, String> validateAddress(String address) {
        if (address.isBlank())
            return new Pair<>(false, "Địa chỉ chi nhánh không được để trống.");
        return new Pair<>(true, address);
    }

    public Pair<Boolean, String> checkStaff(Branch branch) {
        StaffBLL staffBLL = new StaffBLL();
        List<Staff> staffs = staffBLL.searchStaffs("[branch_id] = " + branch.getId(), "[deleted] = 0");

        if(!staffs.isEmpty()){
            return new Pair<>(true, "Không thể xoá chi nhánh do tồn tại nhân viên thuộc chi nhánh.");
        }
        return new Pair<>(false, "");
    }

    public List<Branch> getALLBranches() {
        return branchDAL.getAllBranches();
    }

    @Override
    public Object getValueByKey(Branch branch, String key) {
        return switch (key) {
            case "id" -> branch.getId();
            case "name" -> branch.getName();
            case "phone" -> branch.getPhone();
            case "address" -> branch.getAddress();
            case "headquarter_id" -> branch.getHeadquarter_id();
            default -> null;
        };
    }

    public int getAutoID() {
        return branchDAL.getAutoID();
    }
}
