package com.example.l.seshatmvp.repositories.imp;

import android.content.Context;

import com.example.l.seshatmvp.Utils.SharedPreferenceUtils;

//this class made to facilitate the accessibility to the data stored in sharedPreference
public class FilesRepositoryImp {

    Context context;
    SharedPreferenceUtils sharedPreferenceUtils;


    public FilesRepositoryImp(Context context){
        this.context = context;
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(context);
    }

    public String getWordFilePath(){
        return sharedPreferenceUtils.getStringValue("WordsFilePath", "");
    }

    public String getPhrasesFilePath(){
        return sharedPreferenceUtils.getStringValue("PhrasesFilePath", "");
    }
    public String getSF(){
        return sharedPreferenceUtils.getStringValue("SF", "");
    }
    public String getFileWordsAchieved(){
        return sharedPreferenceUtils.getStringValue("FileWordsAchieved", "");
    }
}
