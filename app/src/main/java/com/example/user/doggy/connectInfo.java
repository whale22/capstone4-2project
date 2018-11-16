package com.example.user.doggy;

public class connectInfo {
    private static String IP;
    private static String alarm;
    private static String user_ID;
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
    public void setUserID(String ui){
        user_ID=ui;
    }
    public String getUserID(){
        return user_ID;
    }
}
