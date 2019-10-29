package c201;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utilities {

    public static void articlesToJsonFile(ArrayList<Article> arr, String filename) {
        try {
            File file = new File("articles/" + filename + ".json");
            String json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(arr);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
            System.out.println(filename + ".json written successfully");
        } catch(Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
        }
    }

    public static ArrayList<Article> jsonToArticleArray(String filename) {
        File file = new File("articles/" + filename + ".json");
        if(!file.exists()) {
            System.out.println("File " + filename + ".json not found.");
            return null;
        }

        ArrayList<Article> returnArr = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            returnArr = new Gson().fromJson(br, new TypeToken<ArrayList<Article>>(){}.getType());
        } catch(Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
        }

        return returnArr;
    }

    public static String getAlphanumericString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", " ").replaceAll(" {2}", " ");
    }

    public static ArrayList<String> getStopWords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(Utilities.class.getClassLoader().getResource("en_stop_words.txt").getPath())));
            ArrayList<String> stops = new ArrayList<>();

            String line = reader.readLine();
            while (line != null) {
                stops.add(line);
                line = reader.readLine();
            }
            reader.close();

            stops.sort(Comparator.comparing(String::length));
            return stops;
        } catch(Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        return null;
    }

    public static String removeStopWords(String s, ArrayList<String> stops) {
        s = getAlphanumericString(s).toLowerCase();
        List<String> tokenizedString = new ArrayList<>(Arrays.asList(s.split(" ")));
        tokenizedString.removeAll(stops);
        return String.join(" ", tokenizedString);
    }
}
