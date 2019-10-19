package c201;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

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
            System.out.println("File " + filename + ".json not found. Please verify filename. Exiting.");
            System.exit(0);
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
}
