package Shared;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {
    public static void signInRequest(Socket socket, JsonObject request) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(request);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loginRequest(Socket socket, JsonObject request){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(request);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showSpecificGame(Socket socket, JsonObject request){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(request);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showAllGames(Socket socket, JsonObject request){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(request);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
