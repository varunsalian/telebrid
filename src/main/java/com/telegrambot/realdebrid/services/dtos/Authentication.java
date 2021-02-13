package com.telegrambot.realdebrid.services.dtos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authentication")
public class Authentication implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name = "device_code")
    private String deviceCode;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "time_interval")
    private Integer interval;

    @Column(name="expires_in")
    private Integer expiresIn;

    @Column(name="verification_url")
    private String verificationUrl;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @Override
    public String toString() {
        return "com.varun.realdebrid.AuthenticationDTO{" +
                "deviceCode='" + deviceCode + '\'' +
                ", userCode='" + userCode + '\'' +
                ", interval=" + interval +
                ", expiresIn=" + expiresIn +
                ", verificationUrl='" + verificationUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public void setVerificationUrl(String verificationUrl) {
        this.verificationUrl = verificationUrl;
    }

    public void setAll(Authentication authenticationDTO){
        this.deviceCode = authenticationDTO.deviceCode;
        this.userCode = authenticationDTO.userCode;
        this.interval = authenticationDTO.interval;
        this.expiresIn = authenticationDTO.expiresIn;
        this.verificationUrl = authenticationDTO.verificationUrl;
    }

    public Authentication(){

    }
}
