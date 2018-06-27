package com.example.l.seshatmvp.Utils;

import android.util.Log;

import com.example.l.seshatmvp.model.Direction;
import com.example.l.seshatmvp.model.Word;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.l.seshatmvp.Utils.DirectionsUtils.getDirections;

public class WordUtils {

    //create word
    public static Word form_word(String txt, String phrase, String SF) {
        try {
            Word resultWord = new Word(txt, SF + txt + ".png", SF + txt, phrase);
            int version = 0;
            Direction[][] gvVersion = prepareWordGuidedVectors(txt, version, SF);

            do {
                resultWord.setFV(gvVersion);
                version++;
                gvVersion = prepareWordGuidedVectors(txt, version, SF);

            } while (gvVersion[0].length > 0);
            Log.i("MainActivity", "form_word: version: " + version);
            return resultWord;
        } catch (Exception e) {
            Log.e("form_wordE:", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //creating Guided Vectors for words.
    private static Direction[][] prepareWordGuidedVectors(String word, int version, String SF) throws FileNotFoundException {
        char charConnector = 'ـ';
        Direction[][] result_directions = new Direction[word.length()][];
        ArrayList<Character> differentchars = new ArrayList<>();
        Character[] charactersWithoutEndConnector = {'أ', 'إ', 'د', 'ذ', 'ر', 'ز', 'و', 'ؤ', 'ا', 'آ'};
        differentchars.addAll(Arrays.asList(charactersWithoutEndConnector));
        for (int i = 0; i < word.length(); i++) {
            Character character = word.charAt(i);

            if (i == 0) {
                if (differentchars.contains(character)) {
                    result_directions[i] = getDirections(SF + character, version);
                } else {
                    result_directions[i] = getDirections(SF + character + charConnector, version);
                }
            } else if (i == word.length() - 1) {
                if (differentchars.contains(word.charAt(i - 1))) {
                    result_directions[i] = getDirections(SF + character, version);
                } else {
                    result_directions[i] = getDirections(SF + charConnector + character, version);
                }
            } else {
                if (differentchars.contains(word.charAt(i - 1)) && differentchars.contains(character)) {
                    result_directions[i] = getDirections(SF + character, version);
                } else if (differentchars.contains(word.charAt(i - 1)) && !differentchars.contains(character)) {
                    result_directions[i] = getDirections(SF + character + charConnector, version);
                } else if (!differentchars.contains(word.charAt(i - 1)) && differentchars.contains(character)) {
                    result_directions[i] = getDirections(SF + charConnector + character, version);
                } else {
                    result_directions[i] = getDirections(SF + charConnector + character + charConnector, version);
                }
            }
        }
        return result_directions;
    }
}
