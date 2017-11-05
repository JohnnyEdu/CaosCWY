package com.example5.lilian.caos_cwy.database;

/**
 * Created by Johnny on 28/10/2017.
 */

public class User {
    private String username;
    private String password;


    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
}