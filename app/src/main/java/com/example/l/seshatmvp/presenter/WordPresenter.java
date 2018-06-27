package com.example.l.seshatmvp.presenter;

import android.util.Log;

import com.example.l.seshatmvp.repositories.WordsRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class WordPresenter {

    private MainActivityView mainActivityView;
    private WordsRepository wordsRepository;

    //the constructor take 2 parameter the view and the repository to make an access between them
    public WordPresenter(MainActivityView mainActivityView, WordsRepository wordsRepository) {
        this.mainActivityView = mainActivityView;
        this.wordsRepository = wordsRepository;
    }
    //call wordRepository interface to read words from archive file
    public void loadArchiveWords(){
        List<String> words = wordsRepository.readArchiveWords();
        if(words.isEmpty()){
            mainActivityView.DisplayNoArchiveWords();
        }else {
            mainActivityView.DisplayArchiveWords(words);
        }
    }

    ////call wordRepository interface to save word to archive file
    public void saveWordstoArchive(String word){
        try {
            wordsRepository.assignWordAsFinished(word);
            Log.i(TAG, "saveWordstoArchive: Success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
