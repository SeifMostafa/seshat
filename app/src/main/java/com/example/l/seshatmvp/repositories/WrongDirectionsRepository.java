package com.example.l.seshatmvp.repositories;

import java.io.IOException;
import java.util.HashMap;

public interface WrongDirectionsRepository {
    HashMap<String, Integer> getWrongDirections() throws IOException;
    void saveWrongDirection(HashMap<String, Integer> hashMap) throws IOException;
}
