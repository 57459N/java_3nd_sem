package bsu.rfct.course2.group9.Indyukov;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class User {

    private String name;
    private String IP;
    private Integer port;
    private String dialog = "";


    User(String name, String IP, Integer port) {
        this.name = name;
        this.IP = IP;
        this.port = port;
    }

    void appendDialog(String str) {
        dialog += str;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return IP;
    }

    public Integer getPort() {
        return port;
    }

    public String getDialog() {
        return dialog;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
}
