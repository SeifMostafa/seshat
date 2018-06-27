package com.example.l.seshatmvp.presenter;

import com.example.l.seshatmvp.repositories.WrongDirectionsRepository;
import com.example.l.seshatmvp.view.GestureDetectorView;

import java.io.IOException;
import java.util.HashMap;

public class WrongDirectionsPresenter {

    private GestureDetectorView gestureDetectorView;
    private WrongDirectionsRepository wrongDirectionsRepository;

    //the constructor take 2 parameter the view and the repository to make an access between them
    public WrongDirectionsPresenter(WrongDirectionsRepository wrongDirectionsRepository,
                                    GestureDetectorView gestureDetectorView){
        this.wrongDirectionsRepository = wrongDirectionsRepository;
        this.gestureDetectorView = gestureDetectorView;
    }

    //call repository to load wrong directions
    public void loadwrongDirections() throws IOException {
        HashMap<String, Integer> wrongDirections = wrongDirectionsRepository.getWrongDirections();
        if(!(wrongDirections.isEmpty())){
            gestureDetectorView.writeMapToFile(wrongDirections);
        }
    }

    //save wrong direction to repository
    public void saveWrongDirections(HashMap<String, Integer> hashMap){
        try {
            wrongDirectionsRepository.saveWrongDirection(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
