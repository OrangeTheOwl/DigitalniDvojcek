package io.github.some_example_name.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.github.some_example_name.classes.PlayerScore;

public class GameManager {

    FileHandle file = Gdx.files.local("scores.json");
    Json json = new Json();

    public static final GameManager INSTANCE = new GameManager();

    private int result;

    private Timer timer;

    private GameManager() {
    }

    public void resetResult() {
        result = 0;
    }

    public void resetTimer() {

    }

    public void incResult(){
        result += 100;
    }

    public void savePlayerScore(PlayerScore playerScore){
        String scores = file.readString();

        List<PlayerScore> list = json.fromJson(ArrayList.class, PlayerScore.class, file);

        if (list == null){
            list = new ArrayList<PlayerScore>();
        }

        list.add(playerScore);

        scores = json.toJson(list);

        file.writeString(scores, false);
    }

    public List<PlayerScore> getPlayerScore(){
        String scores = file.readString();

        List<PlayerScore> list = json.fromJson(ArrayList.class, PlayerScore.class, file);
        if (list != null){
            for (int i = 0; i < list.size(); i++){
                System.out.println(list.get(i).name);
            }
        }

        return list;
    }

}
