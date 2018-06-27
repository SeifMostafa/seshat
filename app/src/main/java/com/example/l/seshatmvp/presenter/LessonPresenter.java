package com.example.l.seshatmvp.presenter;

import com.example.l.seshatmvp.model.Word;
import com.example.l.seshatmvp.repositories.LessonRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import java.util.Map;

public class LessonPresenter {

    MainActivityView mainActivityView;
    LessonRepository lessonRepository;

    //the constructor take 2 parameter the view and the repository to make an access between them
    public LessonPresenter(MainActivityView mainActivityView, LessonRepository lessonRepository) {
        this.mainActivityView = mainActivityView;
        this.lessonRepository = lessonRepository;
    }

    //call repository to get all lessons
    public void loadLessons(){
        Map<Integer, Word[]> lessons = lessonRepository.getLessons();
        if(lessons.isEmpty()){
            mainActivityView.DisplayNoLessons();
        }else {
            mainActivityView.DisplayLessons(lessons);
        }
    }
}
