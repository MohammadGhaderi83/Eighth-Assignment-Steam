package Server;

import Shared.Response;
import Shared.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Services implements Runnable{
    private Socket serverSocket;
    private Scanner in;

    //Constructor

    public Services(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //Service-Providing
    @Override
    public void run() {
        try {
            try {
                in = new Scanner(serverSocket.getInputStream());
                doService();
            } finally {
                in.close();
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        } finally {
            System.out.println("CLIENT " + serverSocket.getRemoteSocketAddress() + " HAS BEEN DISCONNECTED");
        }
    }

    public void doService() {
        while (true) {
            while (!in.hasNext()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            //Receiving data
            String jsonString = in.nextLine();
            System.out.println("RECEIVED " + jsonString);

            JsonObject jsonRequest = new Gson().fromJson(jsonString, JsonObject.class);
            String requestType = jsonRequest.get("requestType").getAsString();
            if (requestType.equals("QUIT")) {
                return;
            } else {
                executeRequest(jsonRequest);
            }
        }
    }

    public void executeRequest(JsonObject jsonRequest) {
        String requestType = jsonRequest.get("requestType").getAsString();

        switch (requestType) {
            case "SIGN UP" -> {
                //Query to database
                if (!ServerMain.usernameExist(jsonRequest.get("username").getAsString())) {
                    //Insertion into database
                    new User(jsonRequest.get("username").getAsString(), jsonRequest.get("password").getAsString(), jsonRequest.get("dateOfBirth").getAsString());
                    //Sending response
                    Response.signInResponse(serverSocket, false);
                } else {
                    //Sending response
                    Response.signInResponse(serverSocket, true);
                }
            }
            case "SIGN IN" -> {
                String username = jsonRequest.get("username").getAsString();
                String password = jsonRequest.get("password").getAsString();
                //Query to database and sending response
                User account = ServerMain.accountLogin(username, password);
                if (account != null) {
                    //Successfully logged in
                    JSONObject jsonAccount = new JSONObject();
                    jsonAccount.put("account_id", account.getId());
                    jsonAccount.put("username", account.getUsername());
                    jsonAccount.put("password", account.getPassword());
                    jsonAccount.put("date_of_birth", account.getDateOfBirth());
                    //Sending response
                    Response.loginResponse(serverSocket, true, jsonAccount);
                } else {
                    //Login was Unsuccessful
                    Response.loginResponse(serverSocket, false, null);
                }
            }
            case "SHOW ALL AVAILABLE GAMES" -> {
                //Query to database
                JsonObject jsonResponse = ServerMain.selectAllGames();
                //Sending response
                Response.showAllGames(serverSocket, jsonResponse);
            }
            case "SHOW AN SPECIFIC GAME" -> {
                //Json
//                System.out.println("Haha");
                String game_id = jsonRequest.get("game_id").getAsString();

                //Query to database
                JsonObject jsonResponse = ServerMain.selectSpecificGame(game_id);
                if (jsonResponse != null){
                    //Sending response
                    Response.showSpecificGame(serverSocket, true,jsonResponse);
                }
                else {
                    Response.showSpecificGame(serverSocket, false, jsonResponse);
                }
            }
        }
    }
}
