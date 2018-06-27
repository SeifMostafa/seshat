package com.example.l.seshatmvp.repositories;

import com.example.l.seshatmvp.model.Word;

import java.util.ArrayList;
import java.util.Map;

public interface LessonRepository {
    Map<Integer, Word[]> getLessons();
}
