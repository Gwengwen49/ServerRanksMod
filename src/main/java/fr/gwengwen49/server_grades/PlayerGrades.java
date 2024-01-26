package fr.gwengwen49.server_grades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PlayerGrades {

    private static final File FILE = new File("grades\\data\\players.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Map<String, Map<Integer, String>> JSON_CONTENT = new HashMap<>();
    public static File getFile() {
        return FILE;
    }

    public static void initialize() {
        if(!FILE.exists()){
            try {
                FILE.createNewFile();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            try(FileWriter writer = new FileWriter(FILE)) {
               GSON.toJson(JSON_CONTENT, writer);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            JSON_CONTENT = deserialize();
        }

    }

    public static boolean playerExists(String playerName){
        return JSON_CONTENT.containsKey(playerName);
    }

    public static Grade getHightestGrade(String playerName){
        int highestPriority = 0;
        Map<Integer, String> map = JSON_CONTENT.get(playerName);
        if(map != null) {
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                if (entry.getKey().intValue() > highestPriority) {
                    highestPriority = entry.getKey();
                }
            }
            Grade grade = Grade.fromFile(map.get(highestPriority));
            if(grade != null) {
                return grade;
            }
        }
        return Grade.NONE;
    }

    public static void addPlayer(String name, Map<Integer, String> grades) {
        JSON_CONTENT.put(name, grades);
        serialize();
    }

    public static void removePlayer(String playerName){

    }

    public static void clearRanks(String playerName){

    }
    public static void rankPlayer(ServerPlayerEntity player, String gradeName, int priority) {
       try {
           String playerName = String.valueOf(player.getName());
           Map<Integer, String> playerGrades = JSON_CONTENT.get(playerName);
           if (playerGrades != null) {
               if (!playerGrades.containsKey(priority) && !Objects.equals(playerGrades.get(priority), gradeName)) {
                   playerGrades.put(priority, gradeName);
                   JSON_CONTENT.put(playerName, playerGrades);
                   serialize();
               } else {
                   System.out.println("grade already present !!!");
               }
           } else {
               JSON_CONTENT.put(playerName, (Map<Integer, String>) Map.of().put(gradeName, priority));
           }
       }catch (Exception e){
           e.printStackTrace(System.out);
       }
    }

    private static void serialize() {
        try(FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(JSON_CONTENT, writer);
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }


    private static Map<String, Map<Integer, String>> deserialize(){
        Map<String, Map<Integer, String>> deserialized = new HashMap<>();
        try(FileReader reader = new FileReader(FILE)){
            deserialized = (Map<String, Map<Integer, String>>) GSON.fromJson(reader, Map.class);
        }
        catch (IOException e){
            e.printStackTrace(System.out);
        }
        return deserialized;
    }
}
