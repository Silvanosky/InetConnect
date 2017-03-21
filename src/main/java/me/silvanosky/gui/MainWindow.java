package me.silvanosky.gui;

import javafx.util.Pair;
import me.silvanosky.SocketFactoryCustom;
import okhttp3.*;

import javax.net.SocketFactory;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * ╱╲＿＿＿＿＿＿╱╲
 * ▏╭━━╮╭━━╮▕
 * ▏┃＿＿┃┃＿＿┃▕
 * ▏┃＿▉┃┃▉＿┃▕
 * ▏╰━━╯╰━━╯▕
 * ╲╰╰╯╲╱╰╯╯╱  Created by Silvanosky on 13/03/2017
 * ╱╰╯╰╯╰╯╰╯╲
 * ▏▕╰╯╰╯╰╯▏▕
 * ▏▕╯╰╯╰╯╰▏▕
 * ╲╱╲╯╰╯╰╱╲╱
 * ＿＿╱▕▔▔▏╲＿＿
 * ＿＿▔▔＿＿▔▔＿＿
 */
public class MainWindow extends JFrame {

    private JPanel jPanel = new JPanel();

    private JTextField text_user = new JTextField();
    private JPasswordField text_pass = new JPasswordField();
    private JComboBox box_in = new JComboBox();

    private ArrayList<Pair<String, String>> interfaces = new ArrayList<>();

    public MainWindow()
    {
        super("InetHack");

        setSize(500, 500);
        setResizable(false);

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                close();
            }
        };
        addWindowListener(l);
        jPanel.setLayout(null);

        createLabel("Username: ", 10, 10, 70, 20);

        initTextComp(text_user, 80, 10, 100, 20);

        createLabel("Password: ", 10, 40, 70, 20);

        initTextComp(text_pass, 80, 40, 100, 20);

        JButton open = new JButton("Open");
        open.setBounds(190, 10, 100, 50);
        open.addActionListener(e -> {
            try {
                connect("Open");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        open.setVisible(true);
        jPanel.add(open);

        JButton close = new JButton("Close");
        close.setBounds(300, 10, 100, 50);
        close.addActionListener(e -> {
            try {
                connect("Close");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        close.setVisible(true);
        jPanel.add(close);

        interfaces = getInterfaces();

        for(Pair<String, String> v: interfaces)
            box_in.addItem(v.getValue());
        box_in.setBounds(10, 80, 300, 30);
        box_in.setVisible(true);
        jPanel.add(box_in);

        setContentPane(jPanel);

        setVisible(true);
    }

    private void createLabel(String name, int x, int y, int w, int h)
    {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, w, h);
        label.setVisible(true);
        jPanel.add(label);
    }

    private void initTextComp(JTextComponent component, int x, int y, int w, int h)
    {
        component.setBounds(x, y, w, h);
        component.setVisible(true);
        jPanel.add(component);
    }

    private ArrayList<Pair<String, String>> getInterfaces()
    {
        ArrayList<Pair<String, String>> data = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual())
                    continue;
                data.add(new Pair<String, String>(current.getName(), current.toString()));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return data;
    }

    private void connect(String state) throws IOException {

        NetworkInterface current = NetworkInterface.getByName(interfaces.get(box_in.getSelectedIndex()).getKey());
        System.out.println("Using interface: " + current.getName());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.socketFactory(new SocketFactoryCustom(SocketFactory.getDefault(), current));

        OkHttpClient client = builder.build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String username = URLEncoder.encode(text_user.getText(), "UTF-8");
        String password = URLEncoder.encode(new String(text_pass.getPassword()), "UTF-8");

        RequestBody body = RequestBody.create(mediaType, "username=" + username + "&password=" + password + "&op=" + state + "&state=0");
        Request request = new Request.Builder()
                .url("https://inetkey.sun.ac.za/index.php")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    private void close()
    {
        System.exit(0);
    }
}
