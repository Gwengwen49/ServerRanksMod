package fr.gwengwen49.server_grades;

import com.google.common.collect.BiMap;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.objectweb.asm.ClassVisitor;

import java.awt.*;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Grade {

    private final String name;
    private final String displayName;
    private final int priorityLevel;
    private final List<String> permissions;
    private static final  Gson gson =  new GsonBuilder().setPrettyPrinting().create();
    private int color;

    public static final Grade NONE = new Grade("", "", 0, "", List.of(), Color.WHITE.getRGB());
    private Grade(String name, String displayName, int priorityLevel, String parent, List<String> permissions, int color) {
        this.name = name;
        this.displayName = displayName;
        this.priorityLevel = priorityLevel;
        this.permissions = permissions;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public Grade withDisplayColor(Color color) {
        this.color = color.getRGB();
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public static Grade create(String name, Optional<String> displayName, Optional<Integer> tabPriority, Optional<String> parent, Color color) throws IOException {
        Grade grade = null;
        File file = new File("grades\\" + name + ".json");
        if (!file.exists()) Files.createFile(file.toPath());
        try {
            grade = new Grade(name, displayName.get(), tabPriority.get(), parent.get(), List.of(), color.getRGB());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(grade, fileWriter);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return grade;
    }

    public static Grade create(String name, Optional<String> displayName, Optional<Integer> tabPriority, Optional<String> parent) throws IOException {
        return create(name, displayName, tabPriority, parent, Color.WHITE);
    }

        public static Grade fromFile(String gradeName){
        File directory = new File("grades");
        for(File file1 : directory.listFiles((dir, name1) -> name1.contains(".json"))){
            if(file1.getName().equals(gradeName+".json")){
                try(FileReader reader = new FileReader(file1)) {
                  return gson.fromJson(reader, Grade.class);
                }
                catch (IOException e){
                   e.printStackTrace(System.out);
                }
            }
        }
        return null;
    }

    public static Text toDisplayable(String playerName){
        Grade grade = PlayerGrades.getHightestGrade(playerName);
        return MutableText.of(PlainTextContent.of( "§f[§r"+grade.displayName+"§f]")).setStyle(Style.EMPTY.withColor(grade.getColor()).withBold(true))
                .append(MutableText.of(PlainTextContent.of(playerName)).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE))));
    }

    public boolean addPermissions(String permission){
       if(!this.permissions.contains(permission)) {
            this.permissions.add(permission);
       }
        this.updateFile();
        return false;
    }

    private void updateFile(){
        File file = new File("grades\\" + this.name + ".json");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(gson.toJson(this));
        }catch (IOException e){
            e.printStackTrace(System.out);
        }

    }
    public boolean removePermission(){
        return false;
    }
}
