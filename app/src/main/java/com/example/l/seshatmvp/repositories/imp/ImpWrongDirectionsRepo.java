package com.example.l.seshatmvp.repositories.imp;

import android.os.Environment;

import com.example.l.seshatmvp.repositories.WrongDirectionsRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImpWrongDirectionsRepo implements WrongDirectionsRepository{

    //read all wrong directions from wrongDirection file
    @Override
    public HashMap<String, Integer> getWrongDirections() throws IOException{
        String file = Environment.getExternalStorageDirectory() + "/SeShatSF/wrongDirections.txt";
        HashMap<String, Integer> map = new HashMap<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":", 2);
            if (parts.length >= 2) {
                String key = parts[0];
                String value = parts[1];
                map.put(key, Integer.parseInt(value));
            } else {
                System.out.println("ignoring line: " + line);
            }
        }
        reader.close();
        return map;
    }

    //store wrong directions in wrongDirections file
    @Override
    public void saveWrongDirection(HashMap<String, Integer> hashMap) throws IOException{
        String file = Environment.getExternalStorageDirectory() + "/SeShatSF/wrongDirections.txt";
        FileWriter fStream;
        BufferedWriter out;
        fStream = new FileWriter(file, false);
        out = new BufferedWriter(fStream);
        for (Map.Entry<String, Integer> pairs : hashMap.entrySet()) {
            out.write(pairs.getKey() + ":" + pairs.getValue() + "\n");
        }
        out.close();
    }
}
