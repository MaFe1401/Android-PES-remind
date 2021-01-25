package edu.upc.pes;

import org.json.JSONArray;

public class Singleton {
    private static final Singleton instance = new Singleton();
    public String username;
    public JSONArray listaTareas;
    public static Singleton getInstance(){
        return instance;
    }
    private Singleton(){}
}
