package com.rest;

public class Test {
    public static void main(String[] args) {
        String trackingData = "S{0|1|||0|0||||||||^9|2|||0|0|0|||||||}^ivr.main.menu{0|3|||0|0|0||1|||||^2|4||0|0|8|'m.0'|||_TE_FINISH_|S|||^9|5|||0|13|0|||||||}^1{0|6|||13|0|svc_test1|||||||^9|7|||13|13|0|||||||}^13{0|8|||13|0|svc_digit|||||||^2|9||0|13|2|m.13|||_TE_FINISH_|S|||^1|10||0|16|0|trk_01|0|9||W|||^2|11||0|18|2|m.13|||_TE_FINISH_|S|||^1|12||0|25|0|trk_01|0|||F|||^2|13||0|27|1|m.13|||_TE_MAX_DTMF_|S|||^1|14||0|28|0|trk_01|0|1||S|||^1|15||0|31|0|trk_01|0|1122331122||S|******####||^9|16|||13|32|2|||||||}  ";
        System.out.println("trackingData:: [" + trackingData + "]");
        System.out.println("trackingData trim:: [" + trackingData.trim() + "]");
    }
}
