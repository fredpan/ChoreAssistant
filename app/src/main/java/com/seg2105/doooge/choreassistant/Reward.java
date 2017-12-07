package com.seg2105.doooge.choreassistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredpan on 2017/11/30.
 */

public class Reward implements Serializable {

    private String userName;
    private String rewardName;

    private int points;
    private List<Responsibility> responsibilities;


    public Reward() {
        //For Firebase
        responsibilities = new ArrayList<>();
    }

    public List<Responsibility> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<Responsibility> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public void addResponsibility(Responsibility responsibility) {
        responsibilities.add(responsibility);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
