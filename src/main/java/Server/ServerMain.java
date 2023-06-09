package Server;

import Shared.Game;
import Shared.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerMain {
    public static Connection conn;
    public static void main(String[] args) throws SQLException {
        insertDate();
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is listening!");

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("New client is connected(remote address): " + socket.getRemoteSocketAddress());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JsonObject selectAllGames(){
        JsonObject rows = new JsonObject();

        //Query to database
        String query = "SELECT * FROM Games";
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            int counter = 1;
            while (resultSet.next()){
                //Storing each row as JsonArray
                JsonArray row = new JsonArray();
                for (int i = 1; i <= 10; i++){
                    row.add(resultSet.getString(i));
                }

                //Adding each row to rows
                rows.add("row" + counter, row);

                //Counting rows
                counter++;
            }

            //Adding row and column counts to the Json
            rows.addProperty("rowCount", counter - 1);
            rows.addProperty("columnCount", 10);
            return rows;
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return null;
    }

    public static JsonObject selectSpecificGame(String game_id){
        //Query to database
        try {
            Connection conn = Database.getConnection();
            String query = "SELECT * FROM Games WHERE game_id = ?";
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, game_id);
            ResultSet resultSet = psmt.executeQuery();
//            if (resultSet.next()){
            JsonObject result = new JsonObject();
            JsonArray row = new JsonArray();
            for (int i = 1; i <= 10; i++){
                row.add(resultSet.getString(i));
            }
            result.addProperty("rowCount", 1);
            result.addProperty("columnCount", 10);
            result.add("row1", row);
            return result;
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        return null;
    }

    public static void insertFileToDB(ArrayList<String> arr, String pngPath){
        //getting items from array
        String game_id = arr.get(0);
        String title = arr.get(1);
        String developer = arr.get(2);
        String genre = arr.get(3);
        double price = Double.parseDouble(arr.get(4));
        int release_year = Integer.parseInt(arr.get(5));
        boolean controller_support = Boolean.parseBoolean(arr.get(6));
        int reviews = Integer.parseInt(arr.get(7));
        int size = Integer.parseInt(arr.get(8));

        //Query to database
        try {
            String query = "INSERT INTO Games (game_id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path) VALUES (?,?,?,?,?,?,?,?,?,?)";
            Connection conn = Database.getConnection();
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, game_id);
            psmt.setString(2, title);
            psmt.setString(3, developer);
            psmt.setString(4, genre);
            psmt.setDouble(5, price);
            psmt.setInt(6, release_year);
            psmt.setBoolean(7, controller_support);
            psmt.setInt(8, reviews);
            psmt.setInt(9, size);
            psmt.setString(10, pngPath);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean usernameExist(String username){
        //Query to database
        String query = "SELECT * FROM Accounts WHERE username = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet resultSet = psmt.executeQuery();
            //check if username found or not
            return resultSet.next();
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return false;
    }

    public static User accountLogin(String username, String password){
        //Query to database
        String query = "SELECT account_id , username, password, date_of_birth FROM Accounts WHERE username = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet resultSet = psmt.executeQuery();
            String hashedPass = resultSet.getString("password");
            if (User.checkPassword(password, hashedPass)){   //compare actual password's hash with provided one
                //username and password is valid
                //Creating account and returning it back
                //Parsing result set
                String account_id = (resultSet.getString("account_id"));
                String date_of_birth = (resultSet.getString("date_of_birth"));
                User account = new User(account_id, username, password, date_of_birth);
                return account;
            }
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return null;
    }
    private static class clientHandler implements Runnable{

        private Socket socket;
        public clientHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {

        }
    }
    private static ArrayList<String> findFiles(){
        File folder = new File("D:\\Users\\Desktop\\practice\\tamrin 8\\Eighth-Assignment-Steam\\src\\main\\java\\Server\\Resources");
        ArrayList<String> FileNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.txt");
        File[] files = folder.listFiles();

        for (File file : files){
            String fileName = file.getName();

            Matcher matcher = pattern.matcher(fileName);
            if (matcher.matches()){
                FileNames.add(fileName);
            }
        }

        return FileNames;
    }

    private static void insertDate() throws SQLException {
        Database database = new Database();
        for (String name : findFiles()){
            File file = new File(name);
            // Create a list to store the words
            ArrayList<String> words = new ArrayList<>();
            // Create a buffered reader to read the file line by line
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Read each line and add the word to the list
                String line;
                while ((line = reader.readLine()) != null) {
                    words.add(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Game game = new Game();

            game.setId(words.get(0));
            game.setTitle(words.get(1));
            game.setDeveloper(words.get(2));
            game.setGenre(words.get(3));
            game.setPrice(Double.parseDouble(words.get(4)));
            game.setRelease_year(Integer.parseInt(words.get(5)));
            game.setController_support(Boolean.parseBoolean(words.get(6)));
            game.setReviews(Integer.parseInt(words.get(7)));
            game.setSize(Integer.parseInt(words.get(8)));
            game.setFile_path("D:\\Users\\Desktop\\practice\\tamrin 8\\Eighth-Assignment-Steam\\src\\main\\java\\Server\\Resources" + words.get(0) + ".png");

//            CRUDGame crudGame = new CRUDGame(database);
//            crudGame.createGame(game);
        }
    }
}
