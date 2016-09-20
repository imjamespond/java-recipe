package com.pengpeng.stargame.rule.loader;

import com.pengpeng.stargame.rule.farm.FarmLevelRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import  java.util.List;


/**
 * User: mql
 * Date: 13-4-25
 * Time: 上午9:13
 */
public  class BaseRuleLoader {


    public  List<String>  readFile(String fileName)
    {
        try {
            String encoding="UTF-8";
            File file=new File(this.getClass().getResource(fileName).toURI());
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);

                List<String> allLineTest=new ArrayList<String>();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    allLineTest.add(lineTxt);
                };
                read.close();
                bufferedReader.close();
                return allLineTest;
            }else{
                System.out.println("找不到指定的文件 ");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }

}
