package com.certsuite.certsuitetesting.forms;

import javax.swing.*;

public class CertSuiteGUI extends JFrame
{
    private JPanel mainPanel;
    private JButton startButton;
    private JTextField urlTextField;
    private JTextField emailTextField;
    private JTextField passwordTextField;

    public CertSuiteGUI(String appname){
        super(appname);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setResizable(false);
        pack();

        //createListeners();
    }

    public static void main(String[] args){
        JFrame frame = new CertSuiteGUI("CertSuite Tester");
        frame.setVisible(true);
    }
}
