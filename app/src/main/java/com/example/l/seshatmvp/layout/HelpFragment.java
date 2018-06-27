package com.example.l.seshatmvp.layout;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.l.seshatmvp.MainActivity;
import com.example.l.seshatmvp.R;
import com.example.l.seshatmvp.Utils.SharedPreferenceUtils;
import com.example.l.seshatmvp.model.Word;
import com.example.l.seshatmvp.presenter.WordPresenter;
import com.example.l.seshatmvp.repositories.imp.ImpWordsRepository;
import com.example.l.seshatmvp.view.MainActivityView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HelpFragment extends Fragment implements MainActivityView{

    public static String HelpFragment_TAG = "HelpFragment";
    public Word[] lesson;
    public int wordIndex;
    ImageButton helpiBtn, PrevlessoniBtn, CurrentlessoniBtn, AchievedlessoniBtn, PrevlessoniBtn_help, CurrentlessoniBtn_help, AchievedlessoniBtn_help;
    AlertDialog AchievedDialog;
    WordPresenter wordPresenter;
    ArrayList<String> data_donewords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //getting lesson
            lesson = (Word[]) getArguments().getParcelableArray(MainActivity.LessonKey);
            //getting word index through lesson
            wordIndex = getArguments().getInt(MainActivity.WordIndexKey);
        }
        MainActivityView view = this;
        wordPresenter = new WordPresenter(view, new ImpWordsRepository(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        helpiBtn = getActivity().findViewById(R.id.imagebutton_moreInfo);
        helpiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playing audio of help instruction
                ((MainActivity) getActivity()).voiceoffer(helpiBtn, getActivity().getString(R.string.helpfragmenthelpbtninstr));
            }
        });

        PrevlessoniBtn = view.findViewById(R.id.imagebutton_prevlesson);
        PrevlessoniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //return back to previous lesson
                    ((MainActivity) getActivity()).updateLesson(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("PrevlessoniBtn", e.toString());
                }
            }
        });
        CurrentlessoniBtn = view.findViewById(R.id.imagebutton_currentlesson);
        CurrentlessoniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //return to current lesson
                    LessonFragment.isPicked = false;
                    SharedPreferenceUtils.getInstance(getContext()).setValue(MainActivity.WordIndexKey, String.valueOf(wordIndex));
                    ((MainActivity) getActivity()).updateLesson(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CurrentlessoniBtn", e.toString());
                }
            }
        });
        AchievedlessoniBtn = view.findViewById(R.id.imagebutton_achievedlessons);
        AchievedlessoniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reading archived words (finished) from Archive file
                wordPresenter.loadArchiveWords();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.layout_listview, null);

                ListView listview_donewords = dialogLayout.findViewById(R.id.listview);
                //define adapter to list finished words
                AchievedWordsListAdapter customAdapter = new AchievedWordsListAdapter(getActivity(), R.layout.layout_word_listview_ite, data_donewords);
                listview_donewords.setAdapter(customAdapter);
                //showing as dialog
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.show();
                AchievedDialog = dialog;
            }
        });

        PrevlessoniBtn_help = view.findViewById(R.id.imagebutton_prevlesson_help);
        PrevlessoniBtn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //help instruction for btn played
                    ((MainActivity) getActivity()).voiceoffer(PrevlessoniBtn_help, getActivity().getString(R.string.backprevlesson));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("PrevlessoniBtn_help", e.toString());
                }
            }
        });
        CurrentlessoniBtn_help = view.findViewById(R.id.imagebutton_currentlesson_help);
        CurrentlessoniBtn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //help instruction for btn played
                    ((MainActivity) getActivity()).voiceoffer(CurrentlessoniBtn_help, getActivity().getString(R.string.backcurrentlesson));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CurrentlessoniBtn_help", e.toString());
                }

            }
        });

        AchievedlessoniBtn_help = view.findViewById(R.id.imagebutton_achievedlessons_help);
        AchievedlessoniBtn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //help instruction for btn played
                    ((MainActivity) getActivity()).voiceoffer(AchievedlessoniBtn_help, getActivity().getString(R.string.urachievements));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CurrentlessoniBtn_help", e.toString());
                }
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void DisplayLessons(Map<Integer, Word[]> lessons) {

    }

    @Override
    public void DisplayNoLessons() {

    }

    @Override
    public void DisplayArchiveWords(List<String> words) {
        data_donewords = (ArrayList<String>) words;

    }

    @Override
    public void DisplayNoArchiveWords() {

    }

    //custom list adapter to list finished word
    public class AchievedWordsListAdapter extends ArrayAdapter<String> {

        public AchievedWordsListAdapter(Context context, int resource, ArrayList<String> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.layout_word_listview_ite, null);
            }

            String p = getItem(position);

            if (p != null) {
                TextView textView_word = v.findViewById(R.id.textView_word_item_txt);
                ImageButton imageButton_sound_help = v.findViewById(R.id.imageButton_word_item_soundhelp);
                ImageButton imageButton_photo_help = v.findViewById(R.id.imageButton_word_item_photohelp);
                ImageButton imageButton_redo = v.findViewById(R.id.imageButton_word_item_back_to);
                textView_word.setText(p);
                imageButton_photo_help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("ArchiveListAdapter", getItem(position) + " imageButton_photo_help is clicked");
                        try {
                            //display image for the word selected
                            ((MainActivity) getActivity()).helpbypic(imageButton_photo_help, getItem(position));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("imageButton_photo_help", e.toString());
                        }
                    }
                });
                imageButton_sound_help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("ArchiveListAdapter", getItem(position) + " imageButton_sound_help is clicked");
                        try {
                            //play sound for the word selected
                            ((MainActivity) getActivity()).voiceoffer(imageButton_sound_help, getItem(position));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("imageButton_sound_help", e.toString());
                        }
                    }
                });
                imageButton_redo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i("ArchiveListAdapter", getItem(position) + " imageButton_redo is clicked");
                        try {
                            AchievedDialog.dismiss();
                            AchievedDialog.cancel();
                            //open word again for just writing it
                            ((MainActivity) getActivity()).openLessonFragment(new Word(p));


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("imageButton_redo", e.toString());
                        }
                    }
                });
            }
            return v;
        }
    }
}
