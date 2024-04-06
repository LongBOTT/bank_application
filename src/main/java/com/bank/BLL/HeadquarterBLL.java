package com.bank.BLL;

import com.bank.DAL.HeadquarterDAL;
import com.bank.DTO.Decentralization;
import com.bank.DTO.Headquarter;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeadquarterBLL extends Manager<Headquarter>{
    private HeadquarterDAL headquarterDAL;

    public HeadquarterBLL() {
        headquarterDAL = new HeadquarterDAL();
    }

    public HeadquarterDAL getHeadquarterDAL() {
        return headquarterDAL;
    }

    public void setHeadquarterDAL(HeadquarterDAL headquarterDAL) {
        this.headquarterDAL = headquarterDAL;
    }

    public Object[][] getData() {
        return getData(headquarterDAL.searchHeadquarters());
    }

    public Pair<Boolean, String> addHeadquarter(Headquarter headquarter) {
        Pair<Boolean, String> result = checkHeadquarterAll(headquarter);

        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (headquarterDAL.addHeadquarter(headquarter) == 0)
            return new Pair<>(false, "Thêm trụ sở không thành công.");

        return new Pair<>(true, "Thêm trụ sở thành công.");
    }

    public Pair<Boolean, String> updateHeadquarter(Headquarter headquarter) {
        Pair<Boolean, String> result = checkHeadquarterAll(headquarter);

        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        if (headquarterDAL.updateHeadquarter(headquarter) == 0)
            return new Pair<>(false, "Cập nhật trụ sở không thành công.");

        return new Pair<>(true, "Cập nhật trụ sở thành công.");
    }

    public Pair<Boolean, String> deleteHeadquarter(Headquarter headquarter) {
        if (headquarterDAL.deleteHeadquarter("[id] = " + headquarter.getId()) == 0)
            return new Pair<>(false, "Xoá trụ sở không thành công.");

        return new Pair<>(true, "Xoá trụ sở thành công.");
    }

    public List<Headquarter> searchHeadquarters(String... conditions) {
        return headquarterDAL.searchHeadquarters(conditions);
    }

    public List<Headquarter> findHeadquarters(String key, String value) {
        List<Headquarter> list = new ArrayList<>();
        List<Headquarter> headquarterList = headquarterDAL.searchHeadquarters();
        for (Headquarter headquarter : headquarterList) {
            if (getValueByKey(headquarter, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(headquarter);
            }
        }
        return list;
    }

//    public List<Headquarter> findHeadquartersBy(Map<String, Object> conditions) {
//        List<Headquarter> headquarters = headquarterDAL.searchHeadquarters();
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            headquarters = findObjectsBy(entry.getKey(), entry.getValue(), headquarters);
//        return headquarters;
//    }
    public  Pair<Boolean, String> checkHeadquarterAll(Headquarter headquarter) {
        Pair<Boolean, String> result;
        result = validateName(headquarter.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(headquarter);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        return new Pair<>(true,"");

    }

    private static Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên trụ sở không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên trụ sở không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên trụ sở không được chứa số.");
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> exists(Headquarter headquarter) {
        List<Headquarter> headquarters = searchHeadquarters("[name] = '" + headquarter.getName() + "'");

        if(!headquarters.isEmpty()){
            return new Pair<>(true, "Trụ sở đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Headquarter headquarter, String key) {
        return switch (key) {
            case "id" -> headquarter.getId();
            case "name" -> headquarter.getName();
            case "address" -> headquarter.getAddress();
            default -> null;
        };
    }

}
