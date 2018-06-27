package com.example.l.seshatmvp.view;

import com.example.l.seshatmvp.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MainActivityView {
    void DisplayLessons(Map<Integer, Word[]> lessons);
    void DisplayNoLessons();
    void DisplayArchiveWords(List<String> words);
    void DisplayNoArchiveWords();
}
