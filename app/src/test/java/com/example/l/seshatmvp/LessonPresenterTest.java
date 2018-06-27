package com.example.l.seshatmvp;

import com.example.l.seshatmvp.model.Word;
import com.example.l.seshatmvp.presenter.LessonPresenter;
import com.example.l.seshatmvp.repositories.LessonRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class LessonPresenterTest {
    @Mock
    LessonRepository repository;
    @Mock
    MainActivityView view;

    @Test
    public void shouldPassLessonsToView() {
        //given
        Map<Integer, Word[]> lessons = new HashMap<>();
        Word[] words = {new Word(), new Word()};
        lessons.put(1, words);
        lessons.put(2, words);
        Mockito.when(repository.getLessons()).thenReturn(lessons);

        //when
        LessonPresenter presenter = new LessonPresenter(view, repository);
        presenter.loadLessons();

        //then
        Mockito.verify(view).DisplayLessons(lessons);
    }

    @Test
    public void shouldHandleNoLessonsFound(){
        //given
        Mockito.when(repository.getLessons()).thenReturn(Collections.<Integer, Word[]>emptyMap());

        //when
        LessonPresenter presenter = new LessonPresenter(view, repository);
        presenter.loadLessons();

        //then
        Mockito.verify(view).DisplayNoLessons();
    }

}
