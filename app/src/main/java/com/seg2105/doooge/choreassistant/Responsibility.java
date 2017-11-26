package com.seg2105.doooge.choreassistant;

import java.util.LinkedList;


/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility {

    private LinkedList<Chore> chores;
    private int size;

    public void Responsibility(){
        chores = new LinkedList<Chore>();
        size = -1;

    }

    public void addChore(Chore chore){
        chores.add(chore);
        size++;
    }

    public Chore getChore(int index){
        return chores.get(index);
    }

    public int getSize(){
        return size;
    }




}
