package com.example.l.seshatmvp.repositories.imp;

import android.content.Context;

import com.example.l.seshatmvp.Utils.SharedPreferenceUtils;
import com.example.l.seshatmvp.model.Word;
import com.example.l.seshatmvp.repositories.LessonRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.l.seshatmvp.Utils.WordUtils.form_word;

public class ImpLessonRepository implements LessonRepository{
    Context context;
    FilesRepositoryImp filesRepositoryImp;
    public ImpLessonRepository(Context context){
        this.context = context;
        filesRepositoryImp = new FilesRepositoryImp(context);
    }

    //read all lessons and words in each lesson to lesson hashMap
    @Override
    public Map<Integer, Word[]> getLessons() {
        Map<Integer, Word[]> lessons = new HashMap<>();
        try {
            FileReader wordsReader = new FileReader(filesRepositoryImp.getWordFilePath());
            FileReader phraseReader = new FileReader(filesRepositoryImp.getPhrasesFilePath());
            BufferedReader WordsBufferedReader = new BufferedReader(wordsReader);
            BufferedReader PhrasesBufferedReader = new BufferedReader(phraseReader);
            String StringlessonCapacity = WordsBufferedReader.readLine();
            if (StringlessonCapacity != null) {
                int k = 1;
                while (true) {
                    int lessonCapacity = Integer.parseInt(StringlessonCapacity);
                    Word[] lessonWords = new Word[lessonCapacity];
                    for (int i = 0; i < lessonCapacity; i++) {
                        String word_txt = WordsBufferedReader.readLine();
                        String phrase = PhrasesBufferedReader.readLine();
                        lessonWords[i] = form_word(word_txt, phrase, filesRepositoryImp.getSF());
                    }
                    lessons.put(k, lessonWords);
                    StringlessonCapacity = WordsBufferedReader.readLine();
                    k++;
                    if (StringlessonCapacity == null)
                        break;
                }
            }
            wordsReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
        return lessons;
    }
}
