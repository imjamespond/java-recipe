package com.pengpeng.stargame.servermanager;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-28 下午3:44
 */
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommandLineTools {
// ------------------------------ 属性 ------------------------------

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CommandLineTools.class);

    private static String OS;

// -------------------------- 静态方法 --------------------------

    static {
        OS = System.getProperty("os.name").toLowerCase().startsWith("windows") ? "windows"
                : "linux";
    }

    public static String getOS() {
        return OS;
    }

    public static boolean Exec(String cmd) throws java.io.IOException,
            java.lang.InterruptedException {
        String[] cmds;
        if (OS.equals("windows")) {
            cmds = new String[]{"cmd.exe", "/c", cmd};
        } else {
            cmds = new String[]{"/bin/sh", "-c", cmd};
        }
//		if (logger.isDebugEnabled()) {
        logger.info("执行" + OS + "系统命令: " + cmd);
//		}
        return Exec(cmds);
    }

    private static boolean Exec(String[] cmds) throws java.io.IOException,
            java.lang.InterruptedException {
        Process ps = Runtime.getRuntime().exec(cmds);

        String out = loadStream(ps.getInputStream());

        String err = loadStream(ps.getErrorStream());

        int r = ps.waitFor();
        if (!err.equalsIgnoreCase("")) {
            throw new IOException(err);
        }
        return (r == 0);
    }

    // read an input-stream into a String
    public static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        try {
            while ((ptr = in.read()) != -1) {
                buffer.append((char) ptr);
            }
        } finally {
            in.close();
        }
        return buffer.toString();
    }

    private static Process Execing(String[] cmds) throws IOException {
        Process ps = Runtime.getRuntime().exec(cmds);
        return ps;
    }

// --------------------------- main方法 ---------------------------

    public static void main(String[] args) throws IOException,
            InterruptedException {
        System.out.println(System.getProperty("os.name"));
        Process p = CommandLineTools.Execing("identify \"F:\\createcode.BMP\"");
        String out = loadStream(p.getInputStream());
        System.out.println(out);
        String[] tmps = out.split(" ");
        if (tmps != null && tmps.length > 0) {
            System.out.println(tmps[tmps.length - 1 - 4]);
        }
    }

    public static Process Execing(String cmd) throws IOException {
        String[] cmds;
        if (OS.equals("windows")) {
            cmds = new String[]{"cmd.exe", "/c", cmd};
        } else {
            cmds = new String[]{"/bin/sh", "-c", cmd};
        }
        return Execing(cmds);
    }
}
