package com.example.user.doggy;

public class connectInfo {
    private static String IP;
    private static String alarm;
    private static String user_ID=null;
    private static int random;
    private static String name2;
    private  static String partner_ID=null;

    public void setName(String name){
        name2 = name;
    }
    public String getName(){return name2;}

    public static void setRandom(int random){
        connectInfo.random = random;
    }
    public static int getRandom(){return random;}

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
        ui=ui.split("<")[0];
        user_ID=ui;
    }
    public String getUserID(){
        return user_ID;
    }

    public void setPartnerID(String ui){
        ui=ui.split("<")[0];
        user_ID=ui;
    }
    public String getPartnerID(){
        return user_ID;
    }
}
