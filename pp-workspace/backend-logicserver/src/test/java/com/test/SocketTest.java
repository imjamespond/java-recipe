package com.test;

import org.junit.Ignore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * User: mql
 * Date: 13-10-30
 * Time: 上午8:54
 */
@Ignore
public class SocketTest {
    /**
     * 注释：short到字节数组的转换！
     *
     * @param
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    public static byte[] intToBytes(int v) {
        byte[] b = new byte[4];
        b[0] = (byte) ((v >>> 24));
        b[1] = (byte) ((v >>> 16));
        b[2] = (byte) ((v >>> 8));
        b[3] = (byte) ((v >>> 0));
//        for(int i=0;i<b.length;i++)
//            System.out.println(b[i]);
//        System.out.println(Integer.toBinaryString(v));
        return b;
    }

    public static void main(String[] args) {
        try {

            Socket socket = new Socket("192.168.10.22", 6400);
//            Socket socket = new Socket("118.192.20.20", 9000);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputStream);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Short a = new Short((short) 28);
            while (true) {
                byte[] b = "hai ,ni  hao fu  wu  qi  ".getBytes();
                short l = (short) (b.length-1);
//                dos.write(a.byteValue());
                dos.write(shortToByte(l));
                dos.write(b);
//                dos.flush();


//                String res=in.readUTF();
//                System.out.println(res);

            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
