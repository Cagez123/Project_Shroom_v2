package fi.tamk.tiko.shroom;

import java.util.TreeSet;

/**
 * Created by Kalle on 30.3.2017.
 */

public class HighScoreList {

    private TreeSet<HighScoreItem> list;

    public HighScoreList() {
        list = new TreeSet<HighScoreItem>();
    }
    public HighScoreList(String highscores) {
        list = new TreeSet<HighScoreItem>();

        String [] items = highscores.split("~");

        for(String item : items) {
            int score = Integer.parseInt(item.split("!")[0]);
            String name = item.split("!")[1];
            list.add(new HighScoreItem(name,score));
        }
    }
    public void addItem(HighScoreItem item) {
        list.add(item);
        if(list.size() >= 11) {
            list.remove(list.last());
        }
    }
    public String toString() {
        String result = "";

        for (HighScoreItem c : list) {
            result += c;
        }
        return result;

    }

}


