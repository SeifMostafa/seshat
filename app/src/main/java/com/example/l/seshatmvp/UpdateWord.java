package com.example.l.seshatmvp;

import android.content.Context;
import android.graphics.Typeface;

import com.example.l.seshatmvp.layout.LessonFragment;
/**
 * Created by seif on 11/2/17.
 */

public interface UpdateWord {
    //interface to make a connection between WordView and LessonFragment
    Typeface updateWordLoop(Typeface typeface, int word_loop);

    void setmContext(Context context);

    void setLessonFragment(LessonFragment lessonFragment);
}
