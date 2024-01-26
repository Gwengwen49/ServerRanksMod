package fr.gwengwen49.server_grades.commands.arguments;

import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;

public class StringToList {



    public static StringToList instance() {
        return new StringToList();
    }

    public List<String> parse(String toParse){
        List<String> elements = new ArrayList<String>();
        int count = 0;
        while(count < toParse.toCharArray().length){
            char c = toParse.toCharArray()[count];
            if(c == ' '){

            }
            count++;
        }

        return elements;
    }
}
