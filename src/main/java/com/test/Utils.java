package com.test;

import java.io.IOException;
import java.io.Reader;

public class Utils {
    static public String GetLastN(String str, int n) {
        return str.substring(str.length() - n);
    }

    public interface Invoke {
        void call(String file, int line, String method, String thread);
    }

    static public void PrintCaller(String msg, final int level) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[level];
        String methodName = e.getMethodName();
        String fileName = e.getFileName();
        int lineNum = e.getLineNumber();
        String thread = currentThread();
        System.out.printf("[%s]%s(%d).%s %s\n", thread, fileName, lineNum, methodName, msg);
    }

    static public void PrintCaller(Invoke invoke) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        String methodName = e.getMethodName();
        String fileName = e.getFileName();
        int lineNum = e.getLineNumber();
        String thread = currentThread();
        invoke.call(fileName, lineNum, methodName, thread);// (fileName, lineNum, methodName, thread)->{ System.out.printf("[%s]%s(%d).%s \n", thread, fileName, lineNum, methodName);}
    }

    static public void PrintCaller(Invoke invoke, final int level) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[level];
        String methodName = e.getMethodName();
        String fileName = e.getFileName();
        int lineNum = e.getLineNumber();
        String thread = currentThread();
        invoke.call(fileName, lineNum, methodName, thread);// (fileName, lineNum, methodName, thread)->{ System.out.printf("[%s]%s(%d).%s \n", thread, fileName, lineNum, methodName);}
    }

    static private String currentThread(){
        String thread = Thread.currentThread().getName();
        int len = 14;
        if (thread.length()> len){
            return thread.substring(0,7)+ thread.substring(thread.length()-7);
        }
        return thread;
    }

    static public String Reader2String(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try {
            int ch = reader.read();
            while (ch > 0) {
                sb.append((char)ch);
                ch = reader.read();
            };
            //reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
