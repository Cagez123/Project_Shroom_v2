package fi.tamk.tiko.shroom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by Kalle on 30.3.2017.
 */

class HighScoreItem implements Comparable<HighScoreItem>{
    private int score;
    private String name;

    public HighScoreItem(String name, int score) {
        setName(name);
        setScore(score);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getScore(){
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString () {
        return "~" + name + "!" + score;
    }


    @Override
    public int compareTo(HighScoreItem item) {
        return this.getScore() - item.getScore();
    }
}
