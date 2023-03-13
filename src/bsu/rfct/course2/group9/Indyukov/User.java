package bsu.rfct.course2.group9.Indyukov;

import javax.swing.*;

public class User extends JMenuItem {

    private String name;
    private Integer port;
    private String dialog;


    User(String name, Integer port) {
        this.name = name;
        this.port = port;
    }

    void appendDialog(String str){
        dialog += str;
    }
}
