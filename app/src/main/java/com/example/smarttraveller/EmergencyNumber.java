package com.example.smarttraveller;

public class EmergencyNumber {
    private String relative;
    private String police;
    private String ambulance;
    private String fireBrigade;
    private String usercommand;

    public EmergencyNumber(String usercommand) {
        relative = "9795644147";
        police = "9548548535";
        ambulance = "9711749232";
        fireBrigade = "9971984993";

        this.usercommand = usercommand;
    }

    public String NumberSelector(){
        String[] temp = usercommand.split(" ");
        for(String X:temp){
            if(X.equalsIgnoreCase("police") || (X.equalsIgnoreCase("crime"))){
                return police;
            }else if (X.equalsIgnoreCase("ambulance") || (X.equalsIgnoreCase("medical"))){
                return ambulance;
            }else if(X.equalsIgnoreCase("relative") || X.equalsIgnoreCase("lost")){
                return relative;
            }else if(X.equalsIgnoreCase("fire") || X.equalsIgnoreCase("fire Brigade")){
                return fireBrigade;
            }
        }
        return relative;
    }
}
