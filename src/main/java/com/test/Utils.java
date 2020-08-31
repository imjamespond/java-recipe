package com.test;

public class Utils {
    static public String GetLastN(String str, int n){
        return str.substring(str.length()-n);
    }

    public interface Invoke  {
        void call(String file,int line, String method, String thread);
    }
    static public void PrintCaller(Invoke invoke) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        String methodName = e.getMethodName();
        String fileName = e.getFileName();
        int lineNum = e.getLineNumber();
        String thread = Thread.currentThread().getName();
        invoke.call(fileName,lineNum,methodName,thread);
    }
}
