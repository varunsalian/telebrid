package com.telegrambot.realdebrid.services.dtos;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "client")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name = "client_id")
    private String clientID;

    @Column(name = "client_secret")
    private String clientSecret;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setAll(Client client){
        this.clientID = client.getClientID();
        this.clientSecret = client.getClientSecret();
    }

    @Override
    public String toString() {
        return "AccessTokenDTO{" +
                "clientID='" + clientID + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client(){

    }
}
