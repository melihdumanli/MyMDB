package com.bilgeadam.clientside.serverconnection;

import com.bilgeadam.clientside.mdbfactory.MovieDbFactory;
import com.bilgeadam.bautils.BAUtils;
import com.bilgeadam.clientside.dto.ActorDto;
import com.bilgeadam.clientside.menuitems.MenuItems;
import com.bilgeadam.resources.Resources;

import java.io.*;
import java.net.Socket;
import java.util.ResourceBundle;

public class MovieClient {
    public void start(ResourceBundle lang) {
        BAUtils baUtils = BAUtils.getInstance();
        ActorDto actorDto = new ActorDto();
        MovieDbFactory dbFactory = new MovieDbFactory();
        String actorName;
        do {
            actorName = baUtils.readString(lang.getString("Globalization.ACTORQUERY"));
            if (actorName.equals(""))
                System.out.println(lang.getString("Globalization.INVALIDENTRY"));
        } while (actorName.equals(""));

        if (checkName(actorName) != null) {
            System.out.println(checkName(actorName));
        } else {
            actorDto.setName(actorName);
            try {
                Socket socket = new Socket("localhost", Integer.parseInt(Resources.getConfig("serverPort")));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(actorDto);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                actorDto = (ActorDto) objectInputStream.readObject();
                System.out.println(actorDto);
                dbFactory.getDao(1).write(actorDto);


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("MovieClient start() method failed." + e.getMessage());
                e.printStackTrace();
            }
        }
        MenuItems.getInstance().showMainMenu(lang);
    }

    private String checkName(String name) {
        String str = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Resources.getConfig("filePath")))) {
            if (bufferedReader.readLine() != null) {
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.contains(name)) {
                        return str;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ActorDao checkName() Method Failed" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
