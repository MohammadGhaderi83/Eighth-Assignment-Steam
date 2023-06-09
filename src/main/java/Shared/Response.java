package Shared;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Response {
    public static void signInResponse(Socket serverSocket, Boolean flag){
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            JSONObject response = new JSONObject();
            response.put("responseType", "SIGN IN");

            if (flag){
                response.put("response", "This username already exists");
            } else {
                response.put("response", "Account created successfully");
            }

            writer.println(response);
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loginResponse(Socket serverSocket, Boolean flag, JSONObject jsonObject){
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            JSONObject response = new JSONObject();
            response.put("responseType", "LOGIN");
            if (flag){
                response.put("response", "LOGIN SUCCESSFULLY");
                response.put("Account", jsonObject);
            } else {
                response.put("response", "LOGIN FAILED");
            }

            writer.println(response);
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showSpecificGame(Socket serverSocket, Boolean flag, JsonObject object){
        OutputStream outputStream = null;
        PrintWriter writer = null;
        try {
            outputStream = serverSocket.getOutputStream();
            writer = new PrintWriter(outputStream, true);

            object.addProperty("response","SELECTED SPECIFIC GAME");
            writer.println(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                outputStream.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void showAllGames(Socket serverSocket, JsonObject object){
        OutputStream outputStream = null;
        PrintWriter writer = null;
        try {
            outputStream = serverSocket.getOutputStream();
            writer = new PrintWriter(outputStream, true);

            object.addProperty("response","SELECTED ALL AVAILABLE Games");
            writer.println(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                outputStream.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
