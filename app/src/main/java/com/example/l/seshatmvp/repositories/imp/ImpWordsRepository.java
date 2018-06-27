package com.example.l.seshatmvp.repositories.imp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.l.seshatmvp.repositories.WordsRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImpWordsRepository implements WordsRepository{
    Context context;
    FilesRepositoryImp filesRepositoryImp;
    public ImpWordsRepository(Context context){
        this.context = context;
        filesRepositoryImp = new FilesRepositoryImp(context);
    }

    //get finished words from archive
    @Override
    public List<String> readArchiveWords() {
        List<String> words = new ArrayList<>();
        try {
            FileReader reader = new FileReader(filesRepositoryImp.getFileWordsAchieved());
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                Log.i("MainActivity", "readArchiveWords: " + line);
                words.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    //store written word to archive
    @Override
    public void assignWordAsFinished(String word) {
        if (!readArchiveWords().contains(word)) {
            try {
                FileWriter writer = new FileWriter(filesRepositoryImp.getFileWordsAchieved(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(word + "\n");
                bufferedWriter.close();
                Log.i("MainActivity", "assignWordAsFinished: " + word);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
