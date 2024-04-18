package com.bank.BLL;

import com.bank.DAL.StaffDAL;
import com.bank.DTO.Staff;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class StaffBLL extends Manager<Staff> {
    private StaffDAL staffDAL;

    private Staff staff ;

    public StaffBLL() {
        staffDAL = new StaffDAL();
    }

    public StaffDAL getStaffDAL() {
        return staffDAL;
    }

    public void setStaffDAL(StaffDAL staffDAL) {
        this.staffDAL = staffDAL;
    }

    public Object[][] getData() {
        return getData(staffDAL.searchStaffs("[deleted] = 0"));
    }


    public Pair<Boolean, String> addStaff(Staff staff) {
        Pair<Boolean, String> result = validateStaffAll(staff);
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        if (staffDAL.addStaff(staff) == 0)
            return new Pair<>(false, "Thêm nhân viên không thành công.");

        return new Pair<>(true, "Thêm nhân viên thành công.\nTài khoản nhân viên được gửi qua email nhân viên đã đăng ký.");
    }


    public Pair<Boolean, String> updateStaff(Staff oldStaff, Staff newStaff) {
        List<String> errorMessages = new ArrayList<>();
        if(!Objects.equals(oldStaff.getStaffNo(),newStaff.getStaffNo())) {
            Pair<Boolean, String> result = validateStaffNo(newStaff.getStaffNo());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldStaff.getName(),newStaff.getName())){
            Pair<Boolean, String> result = validateName(newStaff.getName());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldStaff.getPhone(),newStaff.getPhone())){
            Pair<Boolean, String> result = validatePhone(newStaff.getPhone());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldStaff.getEmail(),newStaff.getEmail())){
            Pair<Boolean, String> result = validateEmail(newStaff.getEmail());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldStaff.getAddress(),newStaff.getAddress())){
            Pair<Boolean, String> result = validateAddress(newStaff.getAddress());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if(!Objects.equals(oldStaff.getBirthdate(),newStaff.getBirthdate())){
            Pair<Boolean, String> result = validateDate(newStaff.getBirthdate());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        if (staffDAL.updateStaff(newStaff) == 0)
            return new Pair<>(false, "Cập nhật nhân viên không thành công.");

        return new Pair<>(true, "Cập nhật nhân viên thành công.");
    }

    public Pair<Boolean, String> deleteStaff(Staff staff) {

        if (staffDAL.deleteStaff("[id] = " + staff.getId()) == 0)
            return new Pair<>(false, "Xoá nhân viên không thành công.");

        return new Pair<>(true, "Xoá nhân viên thành công.");
    }

    public Pair<Boolean, String> changeStaffBranch_id(Staff staff, int newBranch_id) {
        staffDAL.changeStaffBranch_id(staff, newBranch_id);
        return new Pair<>(true, "Chuyển chi nhánh thành công.");
    }

    public List<Staff> searchStaffs(String... conditions) {
        return staffDAL.searchStaffs(conditions);
    }

    public List<Staff> findStaffs(String key, String value) {
        List<Staff> list = new ArrayList<>();
        List<Staff> staffList = staffDAL.searchStaffs("[deleted] = 0");
        for (Staff staff : staffList) {
            if (getValueByKey(staff, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(staff);
            }
        }
        return list;
    }

    public List<List<String>> getStatisticStaff() {
        return staffDAL.getStatisticStaff();
    }

//    public List<Staff> findStaffsBy(Map<String, Object> conditions) {
//        List<Staff> staffs = staffDAL.searchStaffs("[deleted] = 0");
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            staffs = findObjectsBy(entry.getKey(), entry.getValue(), staffs);
//        return staffs;
//    }

    public Pair<Boolean, String> validateStaffAll(Staff staff) {
        Pair<Boolean, String> result;

        result = validateStaffNo(staff.getStaffNo());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateName(staff.getName());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validatePhone(staff.getPhone());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateEmail(staff.getEmail());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateDate(staff.getBirthdate());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateAddress(staff.getAddress());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());
        result = exists(staff);
        if (result.getKey())
            return new Pair<>(false, result.getValue());

        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> exists(Staff newStaff) {
        List<Staff> staffs = staffDAL.searchStaffs("[no] = '" + newStaff.getStaffNo() + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(true, "Số căn cước công dân của nhân viên đã tồn tại.");
        }

        staffs = staffDAL.searchStaffs("[phone] = '" + newStaff.getPhone() + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(true, "Số điện thoại nhân viên đã tồn tại.");
        }

        staffs = staffDAL.searchStaffs("[email] = '" + newStaff.getEmail() + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(true, "Email nhân viên đã tồn tại.");
        }
        return new Pair<>(false, "");
    }


    private Pair<Boolean, String> validateStaffNo(String no) {
        if (no.isBlank())
            return new Pair<>(false, "Số căn cước công dân của nhân viên không được bỏ trống.");
        if (VNString.containsUnicode(no))
            return new Pair<>(false, "Số căn cước công dân của nhân viên không được chứa unicode.");
        if (VNString.containsAlphabet(no))
            return new Pair<>(false, "Số căn cước công dân của nhân viên không được chứa chữ cái.");
        if (!VNString.checkNo(no))
            return new Pair<>(false, "Số căn cước công dân của nhân viên phải bao gồm 12 số.");

        List<Staff> staffs = staffDAL.searchStaffs("[no] = '" + no + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(false, "Số căn cước công dân của nhân viên đã tồn tại.");
        }

        return new Pair<>(true, no);
    }


    public Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên nhân viên không được bỏ trống.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên nhân viên không không được chứa số.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên nhân viên không không được chứa ký tự đặc biệt.");
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> validatePhone(String phone) {
        if (phone.isBlank())
            return new Pair<>(false, "Số điện thoại nhân viên không được bỏ trống.");
        if (!VNString.checkFormatPhone(phone))
            return new Pair<>(false, "Số điện thoại nhân viên phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc \\{\\\\3, 5, 7, 8, 9\\}\\\\.");


        List<Staff> staffs = staffDAL.searchStaffs("[phone] = '" +phone + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(false, "Số điện thoại nhân viên đã tồn tại.");
        }

        return new Pair<>(true, phone);
    }

    public Pair<Boolean, String> validateEmail(String email) {
        if (email.isBlank())
            return new Pair<>(false, "Email nhân viên không được để trống.");
        if (VNString.containsUnicode(email))
            return new Pair<>(false, "Email nhân viên không được chứa unicode.");
        if (!VNString.checkFormatOfEmail(email))
            return new Pair<>(false, "Email nhân viên phải theo định dạng (username@domain.name).");

        List<Staff> staffs = staffDAL.searchStaffs("[email] = '" + email + "'", "[deleted] = 0");
        if (!staffs.isEmpty()) {
            return new Pair<>(false, "Email nhân viên đã tồn tại.");
        }
        return new Pair<>(true, email);
    }

    public Pair<Boolean, String> validateAddress(String address) {
        if (address.isBlank())
            return new Pair<>(false, "Email nhân viên không được để trống.");
        return new Pair<>(true, address);
    }
    private static Pair<Boolean, String> validateDate(java.util.Date birthDate){
        if (birthDate == null)
            return new Pair<>(false, "Ngày sinh không được để trống.");
        if(!VNString.checkFormatDate(String.valueOf(birthDate)))
            return new Pair<>(false, "Ngày sinh không đúng định dạng");

        // nếu ngày sinh nhỏ hơn 18 tuổi tính cả ngày tháng năm
        Calendar dob = Calendar.getInstance();
        dob.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -18); // Giảm đi 18 năm từ ngày hiện tại
        if (dob.after(now)) {
            return new Pair<>(false, "Nhân viên chưa đủ 18 tuổi.");
        }

        return new Pair<>(true, "Ngày sinh hợp lệ");
    }

    @Override
    public Object getValueByKey(Staff staff, String key) {
        return switch (key) {
            case "id" -> staff.getId();
            case "no" -> staff.getStaffNo();
            case "name" -> staff.getName();
            case "gender" -> staff.isGender();
            case "birthdate" -> staff.getBirthdate();
            case "phone" -> staff.getPhone();
            case "address" -> staff.getAddress();
            case "email" -> staff.getEmail();
            case "branch_id" -> staff.getBranch_id();
            default -> null;
        };
    }

    public int getAutoID() {
        return staffDAL.getAutoID();
    }
}
