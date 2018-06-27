package com.example.l.seshatmvp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.l.seshatmvp.Utils.SharedPreferenceUtils;
import com.example.l.seshatmvp.layout.AnimationFragment;
import com.example.l.seshatmvp.layout.HelpFragment;
import com.example.l.seshatmvp.layout.LessonFragment;
import com.example.l.seshatmvp.layout.PhrasePickFragment;
import com.example.l.seshatmvp.model.Word;
import com.example.l.seshatmvp.presenter.LessonPresenter;
import com.example.l.seshatmvp.presenter.WordPresenter;
import com.example.l.seshatmvp.repositories.imp.ImpLessonRepository;
import com.example.l.seshatmvp.repositories.imp.ImpWordsRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.l.seshatmvp.Utils.WordUtils.form_word;
import static com.example.l.seshatmvp.layout.LessonFragment.LessonFragment_TAG;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements MainActivityView{

    public static final String WORDS_PREFS_NAME = "WordsPrefsFile", WordIndexKey = "i", WordKey = "w", PhraseKey = "p", LessonKey = "L", WordLoopKey = "wl";
    public static final String SFKEY = "0";
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 122;
    public static String firstTimekey = "1stTime";

    SharedPreferenceUtils sharedPreferenceUtils;
    MediaPlayer mediaPlayer;

    private String WordsFilePath = "WORDS.txt", PhrasesFilePath = "PHRASES.txt", SF = "/SeShatSF/";
    private String FileWordsAchieved = "archive.txt";

    private Map<Integer, Word[]> lessons;
    private int word_index = 0;
    private int lesson_index = 1;
    private String firstPhrase = "أنا إسمي ";


    LessonPresenter presenter;
    WordPresenter wordpresenter;
    MainActivityView mainActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityView = this;
        presenter = new LessonPresenter(mainActivityView, new ImpLessonRepository(getApplication()));
        wordpresenter = new WordPresenter(mainActivityView, new ImpWordsRepository(getApplication()));
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this);
        checkPermission_AndroidVersion();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    //Check Android version
    private void checkPermission_AndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            // write your logic here if while testing under M.Devices
            // not granted!
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.INTERNET) + ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.RECORD_AUDIO)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        v -> requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},
                                PERMISSIONS_MULTIPLE_REQUEST)).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            startApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean RecordAudioPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean InternetPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean write_storagePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean read_storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (RecordAudioPermission && InternetPermission && write_storagePermission && read_storagePermission) {
                        startApp();
                    } else {
                        Log.i("onRequestPermResult", "" + RecordAudioPermission + "," + InternetPermission + "," + write_storagePermission + "," + read_storagePermission);
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to be able to work",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                v -> requestPermissions(
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                                                Manifest.permission.RECORD_AUDIO},
                                        PERMISSIONS_MULTIPLE_REQUEST)).show();
                    }
                }
                break;
        }
    }

    private void startApp() {
        if (Boolean.valueOf(sharedPreferenceUtils.getStringValue(firstTimekey, "true"))) {
            lesson_index = 1;
            word_index = 0;

            //setting file paths
            SF = Environment.getExternalStorageDirectory() + SF;

            WordsFilePath = SF + WordsFilePath;
            PhrasesFilePath = SF + PhrasesFilePath;
            FileWordsAchieved = SF + FileWordsAchieved;
            new File(FileWordsAchieved);
            //saving indexes on shared preferences
            sharedPreferenceUtils.setValue(LessonKey, String.valueOf(lesson_index));
            sharedPreferenceUtils.setValue(WordIndexKey, String.valueOf(word_index));
            sharedPreferenceUtils.setValue(SFKEY, SF);
            sharedPreferenceUtils.setValue(firstTimekey, String.valueOf(false));
            sharedPreferenceUtils.setValue("SF", SF);
            sharedPreferenceUtils.setValue("WordsFilePath", WordsFilePath);
            sharedPreferenceUtils.setValue("PhrasesFilePath", PhrasesFilePath);
            sharedPreferenceUtils.setValue("FileWordsAchieved", FileWordsAchieved);

            //setting lessons
            presenter.loadLessons();
            Word phrase = new Word(firstPhrase + lessons.get(lesson_index)[0].getText());
            openLessonFragment(phrase);


            try {
                voiceoffer(null, getResources().getString(R.string.myname));
                sleep(1000);
                voiceoffer(null, lessons.get(lesson_index)[0].getText());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //starting lesson
                        updateLesson(0);
                    }
                }, 1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

            lesson_index = Integer.parseInt(sharedPreferenceUtils.getStringValue(LessonKey, "1"));

            word_index = Integer.parseInt(sharedPreferenceUtils.getStringValue(WordIndexKey, "0"));

            SF = sharedPreferenceUtils.getStringValue(SFKEY, SF);
            WordsFilePath = SF + WordsFilePath;
            PhrasesFilePath = SF + PhrasesFilePath;
            FileWordsAchieved = SF + FileWordsAchieved;
            presenter.loadLessons();
            openLessonFragment(lesson_index);
        }
    }

    //open Lesson Fragment by Lesson ID
    public void openLessonFragment(int i) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle = new Bundle();
        Word[] lesson = lessons.get(i);
        sharedPreferenceUtils.setValue(LessonKey, String.valueOf(i));
        Log.i("MainActivity", "openLessonFragment: am here with i(lesson_index)= " + i);
        bundle.putParcelableArray(LessonKey, lesson);
        bundle.putInt(WordIndexKey, 0);
        lessonFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_replacement, lessonFragment, LessonFragment_TAG);
        fragmentTransaction.addToBackStack(LessonFragment_TAG);
        fragmentTransaction.commit();

        Log.i("MainActivity", "openLessonFragment:: lesson_index" + lesson_index);
    }

    //open Lesson Fragment for just one word -- using in animation fragment
    public void openLessonFragment(Word word) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle = new Bundle();
        word = form_word(word.getText(), word.getPhrase(), SF);
        bundle.putParcelable(WordKey, word);
        bundle.putInt(WordIndexKey, 0);
        lessonFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_replacement, lessonFragment, LessonFragment_TAG);
        fragmentTransaction.addToBackStack(LessonFragment_TAG);
        fragmentTransaction.commit();
        Log.i("MainActivity", "openLessonFragment:: lesson_index" + lesson_index);
    }


    //Return back to the latest state of Lesson fragment
    public void backToLessonFragment() {
        Log.i("MainActivity", "backToLessonFragment :: am here!");

        FragmentManager fragmentManager = getFragmentManager();
        LessonFragment lessonFragment = (LessonFragment) fragmentManager.findFragmentByTag(LessonFragment_TAG);
        if (lessonFragment != null) {
            Log.i("MainActivity", "backToLessonFragment != null");

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_replacement, lessonFragment, LessonFragment_TAG);
            fragmentTransaction.addToBackStack(LessonFragment_TAG);
            fragmentTransaction.commit();
        } else {
            Log.i("MainActivity", "lessonFragment = null");
        }
    }

    //open phrase fragment to pick the word learned
    public void openPhraseFragment(String phrase, String word) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PhrasePickFragment phrasePickFragment = new PhrasePickFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PhraseKey, phrase);
        bundle.putString(WordKey, word);
        phrasePickFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_replacement, phrasePickFragment);
        //fragmentTransaction.addToBackStack(PhrasePickFragment_TAG);
        fragmentTransaction.commit();
    }

    //open animation fragment to analyse phrases' words and words' chars
    public void openAnimationFragment(String word) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AnimationFragment animationFragment = new AnimationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WordKey, word);
        animationFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_replacement, animationFragment);
        //fragmentTransaction.addToBackStack(LessonFragment_TAG);
        fragmentTransaction.commit();
    }

    //open Help Fragment
    public void OpenHelpFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HelpFragment helpFragment = new HelpFragment();
        Bundle bundle = new Bundle();
        word_index = Integer.parseInt(sharedPreferenceUtils.getStringValue(WordIndexKey, "0"));
        bundle.putInt(WordIndexKey, word_index);
        bundle.putParcelableArray(LessonKey, lessons.get(1));
        helpFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_replacement, helpFragment);
        // fragmentTransaction.addToBackStack(LessonFragment_TAG);
        fragmentTransaction.commit();
    }


    //play voice of any Audio file path
    public void voiceoffer(View view, String DataPath2Bplayed) {
        if (view != null) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.startAnimation(shake);
        }
        if (mediaPlayer != null) {

            if (mediaPlayer.isLooping() || mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(SF + DataPath2Bplayed);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IllegalStateException | IOException e) {
            Log.e("MainActivity", "voiceoffer::e: " + e.toString());
            Log.e("MainActivity", "voiceoffer::DataPath2Bplayed: " + SF + DataPath2Bplayed);
        }
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.stop());
    }

    // stopping the media player
    public void StopMediaPlayer() {
        Log.i("MainActivity", "StopMediaPlayer");
        if (mediaPlayer.isPlaying() || mediaPlayer.isLooping()) {
            mediaPlayer.stop();
        }
    }

    public void updateLesson(int ToFlag) {
        lesson_index = Integer.parseInt(sharedPreferenceUtils.getStringValue(LessonKey, "1"));
        switch (ToFlag) {
            case 0:
                openLessonFragment(lesson_index);
                break;
            case -1:
                if(lesson_index>1) {
                    openLessonFragment(lesson_index - 1);
                }else {
                    openLessonFragment(lesson_index);
                }
                break;
            case 1:
                if(lesson_index<5) {
                    openLessonFragment(lesson_index + 1);
                }else {
                    openLessonFragment(lesson_index);
                }
                break;
        }
    }

    //displaying help Photo
    public void helpbypic(View view, String img2Bdisplayed) {
        if (view != null) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.startAnimation(shake);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_sample_pic_help, null);
        ImageView imageView = dialogLayout.findViewById(R.id.picsample);
        File imgFile = new File(SF + img2Bdisplayed);
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void DisplayLessons(Map<Integer, Word[]> lessons) {
        this.lessons = lessons;
    }

    @Override
    public void DisplayNoLessons() {
        Toast.makeText(MainActivity.this, "no lessons", Toast.LENGTH_LONG).show();

    }

    @Override
    public void DisplayArchiveWords(List<String> words) {
        Toast.makeText(MainActivity.this, ""+words.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void DisplayNoArchiveWords() {

    }
}
