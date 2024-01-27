package fr.gwengwen49.server_grades;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class PlayerGrades {

    private static final File FILE = new File("grades\\data\\players.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Map<String, List<JsonRank>> JSON_CONTENT = new HashMap<>();
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
        }
        else {
            JSON_CONTENT = deserialize();
        }

    }

    public static boolean playerExists(String playerName){
        return JSON_CONTENT.containsKey(playerName);
    }

    public static Grade getHightestGrade(String playerName){
        JsonRank validRank = null;
        List<JsonRank> ranks = JSON_CONTENT.get(playerName).stream().toList();
        if(ranks != null) {
            for (JsonRank rank : ranks) {
                if (validRank == null || rank.getPriority() > validRank.getPriority()) {
                    validRank = rank;
               }
            }
            if(validRank != null) {
                Grade grade = Grade.fromFile(validRank.gradeName);
                if (grade != null) {
                    return grade;
                }
            }
        }
        return Grade.NONE;
    }

    public static void addPlayer(String name, List<JsonRank> playerRanks) {
        JSON_CONTENT.put(name, playerRanks);
        serialize();
    }

    public static void removePlayer(String playerName){

    }

    public static void clearRanks(String playerName){

    }
    public static void rankPlayer(ServerPlayerEntity player, String gradeName, int priority) {
           String playerName = String.valueOf(player.getGameProfile().getName());
           JsonRank rank = new JsonRank(priority, gradeName);
           List<JsonRank> playerGrades = JSON_CONTENT.get(playerName);
           if (playerGrades != null) {
               System.out.println("1.5");
               if (!playerGrades.contains(rank)) {
                   playerGrades.add(rank);
                   System.out.println("3");
                   JSON_CONTENT.put(playerName, playerGrades);
                   System.out.println("4");
                   System.out.println("5");
               } else {
                   System.out.println("grade already present !!!");
               }
           } else {
                System.out.println("tests2");
               List<JsonRank> ranks = new ArrayList<>();
               ranks.add(rank);
               JSON_CONTENT.put(playerName, ranks);
           }
        serialize();
    }

    private static void serialize() {
        try(FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(JSON_CONTENT, writer);
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }


    private static Map<String, List<JsonRank>> deserialize(){
        Map<String, List<JsonRank>> deserialized = new HashMap<>();
        try(FileReader reader = new FileReader(FILE)){
            Type mapType = new TypeToken<Map<String, List<JsonRank>>>(){}.getType();
            deserialized = GSON.fromJson(reader, mapType);
        }
        catch (IOException e){
            e.printStackTrace(System.out);
        }
        return deserialized;
    }

    public static Map<String, List<JsonRank>> getJsonContent() {
        return JSON_CONTENT;
    }

    public static class JsonRank {

        private int priority;
        private String gradeName;
        public JsonRank(int priority, String gradeName){
            this.priority = priority;
            this.gradeName = gradeName;
        }

        public int getPriority() {
            return priority;
        }

        public String getGradeName() {
            return gradeName;
        }
    }
}