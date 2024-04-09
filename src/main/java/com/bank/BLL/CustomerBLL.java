package com.bank.BLL;

import com.bank.DAL.CustomerDAL;
import com.bank.DTO.Customer;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.util.*;

public class CustomerBLL extends Manager<Customer> {
    private CustomerDAL customerDAL;

    private List<Customer> customerListAll;

    public CustomerBLL() {
        customerDAL = new CustomerDAL();
        customerListAll = customerDAL.getAllCustomers();
    }

    public List<Customer> getCustomerListAll() {
        return customerListAll;
    }

    public void setCustomerListAll(List<Customer> customerListAll) {
        this.customerListAll = customerListAll;
    }

    public CustomerDAL getCustomerDAL() {
        return customerDAL;
    }

    public void setCustomerDAL(CustomerDAL customerDAL) {
        this.customerDAL = customerDAL;
    }

    public Object[][] getData() {
        return getData(customerDAL.searchCustomers("[deleted] = 0"));
    }


    public Pair<Boolean, String> addCustomer(Customer customer) {
        Pair<Boolean, String> result = validateCustomerAll(customer);
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        if (customerDAL.addCustomer(customer) == 0)
            return new Pair<>(false, "Thêm khách hàng không thành công.");

        return new Pair<>(true, "Thêm khách hàng thành công.\nVui lòng kiểm tra thông tin tài khoản qua email.");
    }


    public Pair<Boolean, String> updateCustomer(Customer oldCustomer, Customer newCustomer) {
        List<String> errorMessages = new ArrayList<>();
        if(!Objects.equals(oldCustomer.getCustomerNo(),newCustomer.getCustomerNo())) {
            Pair<Boolean, String> result = validateCustomerNo(newCustomer.getCustomerNo());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getName(),newCustomer.getName())){
            Pair<Boolean, String> result = validateName(newCustomer.getName());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getPhone(),newCustomer.getPhone())){
            Pair<Boolean, String> result = validatePhone(newCustomer.getPhone());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getEmail(),newCustomer.getEmail())){
            Pair<Boolean, String> result = validateEmail(newCustomer.getEmail());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getAddress(),newCustomer.getAddress())){
            Pair<Boolean, String> result = validateAddress(newCustomer.getAddress());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if(!Objects.equals(oldCustomer.getBirthdate(),newCustomer.getBirthdate())){
            Pair<Boolean, String> result = validateDate(newCustomer.getBirthdate());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        if (customerDAL.updateCustomer(newCustomer) == 0)
            return new Pair<>(false, "Cập nhật khách hàng không thành công.");

        return new Pair<>(true, "Cập nhật khách hàng thành công.");
    }

    public Pair<Boolean, String> updateAllCustomer(Customer oldCustomer, Customer newCustomer) {
        List<String> errorMessages = new ArrayList<>();
        if(!Objects.equals(oldCustomer.getCustomerNo(),newCustomer.getCustomerNo())) {
            Pair<Boolean, String> result = validateCustomerNo(newCustomer.getCustomerNo());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getName(),newCustomer.getName())){
            Pair<Boolean, String> result = validateName(newCustomer.getName());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getPhone(),newCustomer.getPhone())){
            Pair<Boolean, String> result = validatePhone(newCustomer.getPhone());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getEmail(),newCustomer.getEmail())){
            Pair<Boolean, String> result = validateEmail(newCustomer.getEmail());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }
        if(!Objects.equals(oldCustomer.getAddress(),newCustomer.getAddress())){
            Pair<Boolean, String> result = validateAddress(newCustomer.getAddress());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if(!Objects.equals(oldCustomer.getBirthdate(),newCustomer.getBirthdate())){
            Pair<Boolean, String> result = validateDate(newCustomer.getBirthdate());
            if (!result.getKey())
                errorMessages.add(result.getValue());
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        if (customerDAL.updateAllCustomer(newCustomer) == 0)
            return new Pair<>(false, "Cập nhật khách hàng không thành công.");

        return new Pair<>(true, "Cập nhật khách hàng thành công.");
    }

    public Pair<Boolean, String> deleteCustomer(Customer customer) {

        if (customerDAL.deleteCustomer("[no] = '" + customer.getCustomerNo() + "'") == 0)
            return new Pair<>(false, "Xoá khách hàng không thành công.");

        return new Pair<>(true, "Xoá khách hàng thành công.");
    }

    public Pair<Boolean, String> deleteAllCustomer(Customer customer) {

        if (customerDAL.deleteAllCustomer(customer.getCustomerNo()) == 0)
            return new Pair<>(false, "Xoá khách hàng không thành công.");

        return new Pair<>(true, "Xoá khách hàng thành công.");
    }

    public List<Customer> searchCustomers(String... conditions) {
        return customerDAL.searchCustomers(conditions);
    }

    public List<Customer> findCustomers(String key, String value) {
        List<Customer> list = new ArrayList<>();
        List<Customer> customerList = customerDAL.searchCustomers("[deleted] = 0");
        for (Customer customer : customerList) {
            if (getValueByKey(customer, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(customer);
            }
        }
        return list;
    }

    public List<Customer> findAllCustomers(String key, String value) {
        List<Customer> list = new ArrayList<>();
        List<Customer> customerList = customerListAll;
        for (Customer customer : customerList) {
            if (getValueByKey(customer, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(customer);
            }
        }
        return list;
    }

    public List<Customer> searchCustomerByBranch(int branch_id) {
        return customerDAL.searchCustomerByBranch(branch_id);
    }

//    public List<Customer> findCustomersBy(Map<String, Object> conditions) {
//        List<Customer> customers = customerDAL.searchCustomers("[deleted] = 0");
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            customers = findObjectsBy(entry.getKey(), entry.getValue(), customers);
//        return customers;
//    }

    public Pair<Boolean, String> validateCustomerAll(Customer customer) {
        Pair<Boolean, String> result;

        result = validateCustomerNo(customer.getCustomerNo());
        if (!result.getKey()) {
            if (result.getValue().equals("Số căn cước công dân của khách hàng đã tồn tại.")) {
                mơ lại khách hàng
            }
            else
                return new Pair<>(false, result.getValue());

        }

        result = validateName(customer.getName());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validatePhone(customer.getPhone());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateEmail(customer.getEmail());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateDate(customer.getBirthdate());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateAddress(customer.getAddress());
        if (!result.getKey())
            return new Pair<>(false, result.getValue());
        result = exists(customer);
        if (result.getKey())
            return new Pair<>(false, result.getValue());

        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> exists(Customer newCustomer) {
        List<Customer> customers = customerDAL.searchCustomers("[no] = '" + newCustomer.getCustomerNo() + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(true, "Số căn cước công dân của khách hàng đã tồn tại.");
        }

        customers = customerDAL.searchCustomers("[phone] = '" + newCustomer.getPhone() + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(true, "Số điện thoại khách hàng đã tồn tại.");
        }

        customers = customerDAL.searchCustomers("[email] = '" + newCustomer.getEmail() + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(true, "Email khách hàng đã tồn tại.");
        }
        return new Pair<>(false, "");
    }


    private Pair<Boolean, String> validateCustomerNo(String no) {
        if (no.isBlank())
            return new Pair<>(false, "Số căn cước công dân của khách hàng không được bỏ trống.");
        if (VNString.containsUnicode(no))
            return new Pair<>(false, "Số căn cước công dân của khách hàng không được chứa unicode.");
        if (VNString.containsAlphabet(no))
            return new Pair<>(false, "Số căn cước công dân của khách hàng không được chứa chữ cái.");
        if (!VNString.checkNo(no))
            return new Pair<>(false, "Số căn cước công dân của khách hàng phải bao gồm 12 số.");

        List<Customer> customers = customerDAL.searchCustomers("[no] = '" + no + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(false, "Số căn cước công dân của khách hàng đã tồn tại.");
        }

        return new Pair<>(true, no);
    }


    public Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên khách hàng không được bỏ trống.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên khách hàng không không được chứa số.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên khách hàng không không được chứa ký tự đặc biệt.");
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> validatePhone(String phone) {
        if (phone.isBlank())
            return new Pair<>(false, "Số điện thoại khách hàng không được bỏ trống.");
        if (!VNString.checkFormatPhone(phone))
            return new Pair<>(false, "Số điện thoại khách hàng phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc {3, 5, 7, 8, 9}.");


        List<Customer> customers = customerDAL.searchCustomers("[phone] = '" +phone + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(false, "Số điện thoại khách hàng đã tồn tại.");
        }

        return new Pair<>(true, phone);
    }

    public Pair<Boolean, String> validateEmail(String email) {
        if (email.isBlank())
            return new Pair<>(false, "Email khách hàng không được để trống.");
        if (VNString.containsUnicode(email))
            return new Pair<>(false, "Email khách hàng không được chứa unicode.");
        if (!VNString.checkFormatOfEmail(email))
            return new Pair<>(false, "Email khách hàng phải theo định dạng (username@domain.name).");

        List<Customer> customers = customerDAL.searchCustomers("[email] = '" + email + "'", "[deleted] = 0");
        if (!customers.isEmpty()) {
            return new Pair<>(false, "Email khách hàng đã tồn tại.");
        }
        return new Pair<>(true, email);
    }

    public Pair<Boolean, String> validateAddress(String address) {
        if (address.isBlank())
            return new Pair<>(false, "Email khách hàng không được để trống.");
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

    public List<Customer> getALLCustomers() {
        return customerDAL.getAllCustomers();
    }

    @Override
    public Object getValueByKey(Customer customer, String key) {
        return switch (key) {
            case "no" -> customer.getCustomerNo();
            case "name" -> customer.getName();
            case "gender" -> customer.isGender();
            case "birthdate" -> customer.getBirthdate();
            case "phone" -> customer.getPhone();
            case "address" -> customer.getAddress();
            case "email" -> customer.getEmail();
            default -> null;
        };
    }

    public int getAutoID() {
        return 0;
    }
}
