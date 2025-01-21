package io.github.some_example_name.classes;

public class PlayerScore implements Comparable<PlayerScore>{
    public String name;
    public String score;

    public PlayerScore(){}

    public PlayerScore(String name, String score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(PlayerScore playerScore) {


        return Integer.parseInt(this.score) -  Integer.parseInt(playerScore.score);
    }
}
