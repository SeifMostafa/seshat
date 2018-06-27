package com.example.l.seshatmvp;

import com.example.l.seshatmvp.presenter.WordPresenter;
import com.example.l.seshatmvp.repositories.WordsRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class WordsRepositoryTest {
    @Mock
    MainActivityView view;
    @Mock
    WordsRepository repository;

    @Test
    public void shouldPassWordsToView(){
        //given
        ArrayList<String> words = new ArrayList<>();
        words.add("first");
        words.add("second");
        Mockito.when(repository.readArchiveWords()).thenReturn(words);

        //when
        WordPresenter presenter = new WordPresenter(view, repository);
        presenter.loadArchiveWords();


        //then
        Mockito.verify(view).DisplayArchiveWords(words);
    }

    @Test
    public void shouldHandleNoWordsFound(){
        //given
        Mockito.when(repository.readArchiveWords()).thenReturn(Collections.<String>emptyList());

        //when
        WordPresenter presenter = new WordPresenter(view, repository);
        presenter.loadArchiveWords();

        //then
        Mockito.verify(view).DisplayNoArchiveWords();
    }
}
