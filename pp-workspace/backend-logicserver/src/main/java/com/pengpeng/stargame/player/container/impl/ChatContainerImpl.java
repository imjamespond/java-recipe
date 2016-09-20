package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerChat;
import com.pengpeng.stargame.player.container.IChatContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 聊天内容检查规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22上午10:41
 */
@Deprecated
//@Component
public class ChatContainerImpl implements IChatContainer {
    private Set<String> words = new HashSet<String>();

    @Autowired
    private IExceptionFactory exceptionFactory;

    private ArrayList<String> first = new ArrayList<String>();
    private String[] sortFirst;
    private char[] charFirst;
    private HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private HashMap<String, String[]> sortMap = new HashMap<String, String[]>();
    private HashMap<String, char[]> charMap = new HashMap<String, char[]>();

    private ArrayList<String> temp;
    private String key, value;
    int length;

    @Override
    public void init(){
        for (String k : words) {
            if (!first.contains(k.substring(0, 1))) {
                first.add(k.substring(0, 1));
            }
            length = k.length();
            for (int i = 1; i < length; i++) {
                key = k.substring(0, i);
                value = k.substring(i, i + 1);
                if (i == 1 && !first.contains(key)) {
                    first.add(key);
                }

                // 有，添加
                if (map.containsKey(key)) {
                    if (!map.get(key).contains(value)) {
                        map.get(key).add(value);
                    }
                }
                // 没有添加
                else {
                    temp = new ArrayList<String>();
                    temp.add(value);
                    map.put(key, temp);
                }
            }
        }
        sortFirst = first.toArray(new String[first.size()]);
        Arrays.sort(sortFirst); // 排序

        charFirst = new char[first.size()];
        for (int i = 0; i < charFirst.length; i++) {
            charFirst[i] = first.get(i).charAt(0);
        }
        Arrays.sort(charFirst); // 排序

        String[] sortValue;
        ArrayList<String> v;
        Map.Entry<String, ArrayList<String>> entry;
        Iterator<Map.Entry<String, ArrayList<String>>> iter = map.entrySet()
                .iterator();
        while (iter.hasNext()) {
            entry = (Map.Entry<String, ArrayList<String>>) iter.next();
            v = (ArrayList<String>) entry.getValue();
            sortValue = v.toArray(new String[v.size()]);
            Arrays.sort(sortValue); // 排序
            sortMap.put(entry.getKey(), sortValue);
        }

        char[] charValue;
        iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            entry = (Map.Entry<String, ArrayList<String>>) iter.next();
            v = (ArrayList<String>) entry.getValue();
            charValue = new char[v.size()];
            for (int i = 0; i < charValue.length; i++) {
                charValue[i] = v.get(i).charAt(0);
            }
            Arrays.sort(charValue); // 排序
            charMap.put(entry.getKey(), charValue);
        }
    }

    @Override
    public void checkCoin(Player player, PlayerChat playerChat) throws GameException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void checFreeNum(Player player, PlayerChat playerChat) throws GameException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public int getFreeNum(String pid) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addWord(Collection<String> words) {
        this.words.addAll(words);
    }

    @Override
    public void addWord(String word) {
        this.words.add(word);
    }

    @Override
    public void removeWord(String word) {
        this.words.remove(word);
    }

    @Override
    public void checkTalk(OtherPlayer player) throws RuleException {
        //TODO:方法需要实现
    }

    @Override
    public boolean canTalk(OtherPlayer player) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean hasSensitive(String msg) {
        String r = null, f, c = msg;
        char g;
        char[] temps;
        int length = c.length();
        for (int i = 0; i < length - 1; i++) {
            g = c.charAt(i);
            // 二分查找
            if (Arrays.binarySearch(charFirst, g) > -1) {
                tag : for (int j = i + 1; j < length; j++) {
                    f = c.substring(i, j);
                    g = c.charAt(j);
                    temps = charMap.get(f);
                    if (temps == null) { // 找到了
                        //System.out.println("ok");
                        r = f;
                        String str = "";
                        for (int m = 1; m <= r.length(); m++) {
                            str = str + "*";
                        }
                        return true;
                    }
                    // 二分查找
                    if (Arrays.binarySearch(temps, g) > -1) {
                        if (j == length - 1) {
                            // print("find!");
                            //System.out.println("find!");
                            r = c.substring(i, j + 1);
                            String str = "";
                            for (int m = 1; m <= r.length(); m++) {
                                str = str + "*";
                            }
                            return true;
                        }
                    } else { // 没有找到了
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void checkSensitive(String msg) throws RuleException {
        if (hasSensitive(msg)){
            exceptionFactory.throwRuleException("chat.illegal.info");
        }
    }

    @Override
    public String filter(String msg) {
        String r = null, f, c = msg;
        String replacedword = msg;
        char g;
        char[] temps;
        int length = c.length();
        for (int i = 0; i < length - 1; i++) {
            g = c.charAt(i);
            // 二分查找
            if (Arrays.binarySearch(charFirst, g) > -1) {
                tag : for (int j = i + 1; j < length; j++) {
                    f = c.substring(i, j);
                    g = c.charAt(j);
                    temps = charMap.get(f);
                    if (temps == null) { // 找到了
                        //System.out.println("ok");
                        r = f;
                        String str = "";
                        for (int m = 1; m <= r.length(); m++) {
                            str = str + "*";
                        }
                        replacedword = c.replace(r, str);
                        c = replacedword;
                        break tag;
                    }
                    // 二分查找
                    if (Arrays.binarySearch(temps, g) > -1) {
                        if (j == length - 1) {
                            // print("find!");
                            //System.out.println("find!");
                            r = c.substring(i, j + 1);
                            String str = "";
                            for (int m = 1; m <= r.length(); m++) {
                                str = str + "*";
                            }
                            replacedword = c.replace(r, str);
                            c = replacedword;
                            break tag;
                        }
                    } else { // 没有找到了
                        break;
                    }
                }
            }
        }
        return replacedword;
    }

}
