package com.example.l.seshatmvp.repositories;

import java.util.List;

public interface WordsRepository {
    List<String> readArchiveWords();
    void assignWordAsFinished(String Word);
}
