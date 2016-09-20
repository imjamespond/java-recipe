package com.pengpeng.admin.analyse.model;

import java.math.BigInteger;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-22上午11:47
 */
public class DataFactory {


    public void sql1(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum1(val.intValue());
        }
    }

    public void sql2(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum2(val.intValue());
        }
    }
    public void sql3(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum3(val.intValue());
        }
    }
    public void sql4(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum4(val.intValue());
        }
    }
    public void sql5(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum5(val.intValue());
        }
    }
    public void sql6(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum6(val.intValue());
        }
    }
    public void sql7(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum7(val.intValue());
        }
    }
    public void sql8(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum8(val.intValue());
        }
    }
    public void sql9(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum9(val.intValue());
        }
    }
    public void sql10(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum10(val.intValue());
        }
    }
    public void sql11(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum11(val.intValue());
        }
    }
    public void sql12(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum12(val.intValue());
        }
    }
    public void sql13(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum13(val.intValue());
        }
    }
    public void sql14(Map<String, CommonData> map, Map<String, Object> value) {
        for(Map.Entry<String,Object> entry:value.entrySet()){
            String date = entry.getKey();
            BigInteger val = (BigInteger)entry.getValue();
            CommonData data = map.get(date);
            data.setNum14(val.intValue());
        }
    }
}
