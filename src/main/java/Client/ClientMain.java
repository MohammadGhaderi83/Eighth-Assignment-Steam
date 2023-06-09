package Client;

import Shared.Request;
import Shared.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientMain {
    private static Scanner input = new Scanner(System.in);
    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    {
        try {
            socket = new Socket("localhost", 1234);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        System.out.println("\t** Welcome to Steam **");
        boolean flag = true;
        while (flag) {
            System.out.println("Please choose from the options below:\n" +
                    "1- Sign in\n" +
                    "2- Sign up\n" +
                    "3- Exit");
            String userChoice = input.next();
            if (userChoice.equals("1")){
                signIn();
            } else if (userChoice.equals("2")){
                login();
            } else if (userChoice.equals("3")){
                flag = false;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
    }
    public static void signIn(){
        System.out.println("Please enter your username: ");
        String username = input.next();
        System.out.println("Please enter your password: ");
        String password = input.next();
        System.out.println("Please confirm your password: ");
        String confirmPassword = input.next();

        if (password.equals(confirmPassword)){
            System.out.println("Please enter your date of birth(like )");
            String date_of_birth = input.next();

            //JSON
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("RequestType", "signIn");
            jsonRequest.addProperty("username", username);
            jsonRequest.addProperty("password", password);
            jsonRequest.addProperty("date_of_birth", date_of_birth);

            System.out.println("Sending sign in request");
            Request.signInRequest(socket, jsonRequest);

            String response = input.next();
            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
        } else {
            System.out.println("Passwords don't match, please try again.");
            signIn();
        }
    }
    public static void login(){
        System.out.println("Enter username:");
        String username = input.nextLine();
        System.out.println("Enter password:");
        String password = input.nextLine();

        //Json
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("requestType", "SIGN IN");
        jsonRequest.addProperty("username", username);
        jsonRequest.addProperty("password", password);

        Request.loginRequest(socket, jsonRequest);
        String response = input.next();
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        String valid = jsonObject.get("response").getAsString();

        if (valid.equalsIgnoreCase("LOGIN SUCCESSFULLY")){
            JsonObject account = jsonObject.get("Account").getAsJsonObject();
            User user = new User(account.get("id").getAsString(), account.get("username").getAsString(), account.get("password").getAsString(), account.get("date_of_birth").getAsString());

        }
    }
    public void userMenu(User user){
        System.out.println("Select from the options below: \n" +
                "1- Show an specific game\n" +
                "2- Show all available games" +
                "3- Downloaded games" +
                "4- Download a game");

        String userInput = input.next();
        if (userInput.equalsIgnoreCase("1")){
            System.out.println("Enter game_id");
            String game_id = input.nextLine();

            //Json
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("requestType", "SHOW AN SPECIFIC GAME");
            jsonRequest.addProperty("game_id", game_id);

            //Sending Request
            Request.showSpecificGame(socket, jsonRequest);
            System.out.println("SENDING : SHOW AN SPECIFIC GAME REQUEST");

            //Receiving Response
            String response = input.nextLine();
            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
            System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());
            showGameTable(jsonResponse);
        } else if (userInput.equalsIgnoreCase("2")) {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("requestType", "SHOW ALL AVAILABLE GAMES");

            //Sending Request
            System.out.println("SENDING :" + "SHOW ALL AVAILABLE GAMES REQUEST");
            Request.showAllGames(socket, jsonRequest);

            //Receiving response
            String response = input.next();
            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
            System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());
            showGameTable(jsonResponse);
        } else if (userInput.equalsIgnoreCase("3")){

        } else if (userInput.equalsIgnoreCase("4")){

        } else {
            System.out.println("Invalid input, Please try again.");
            userMenu(user);
        }
    }

    public static void showGameTable(JsonObject table) {
        int rowCount = table.get("rowCount").getAsInt();
        int columnCount = table.get("columnCount").getAsInt();

        System.out.println("Table:");
        System.out.printf("%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s", "|game_id|", "|title|", "|developer|", "|genre|", "|price|", "|release_year|", "|controller_support|", "|reviews|", "|size|", "|file_path|");

        //Iterating through rows
        for (int i = 1; i <= rowCount; i++) {
            JsonArray row = table.get("row" + i).getAsJsonArray();
            //Iterating through columns
            for (int j = 0; j < columnCount; j++) {
                System.out.printf("%-50s", "|" + row.get(j) + "|");
            }
            System.out.println();
        }
    }
    public static void search(){

    }
}
