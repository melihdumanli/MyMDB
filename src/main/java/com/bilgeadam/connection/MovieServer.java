package com.bilgeadam.connection;

import com.bilgeadam.clientside.mdbfactory.MovieDbFactory;
import com.bilgeadam.clientside.dto.ActorDto;
import com.bilgeadam.resources.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MovieServer {

	public void start() {
		ActorDto actorDto;
		MovieDbFactory dbFactory = new MovieDbFactory();

		try {
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(Resources.getConfig("serverPort")));
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Client Connected.");
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				actorDto = (ActorDto) objectInputStream.readObject();
				dbFactory.getDao(1).read(actorDto);


				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(actorDto);
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("MovieServer start() method failed." + e.getMessage());
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MovieServer movieServer = new MovieServer();
        movieServer.start();
	}
}
