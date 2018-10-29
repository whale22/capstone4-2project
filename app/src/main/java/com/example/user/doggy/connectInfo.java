package com.example.user.doggy;

public class connectInfo {
    public static String IP;
    public static String alarm;
    public void setIP(String IP2){
        IP=IP2;
    }
    public String getIP(){
        return IP;
    }
    public void setAlarm(String al){
        alarm=al;
    }
    public String getAlarm(){
        return alarm;
    }
}
