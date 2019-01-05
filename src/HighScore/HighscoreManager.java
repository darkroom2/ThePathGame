package HighScore;

import java.io.*;
import java.util.ArrayList;

public class HighscoreManager {
    private ArrayList<Score> scores;

    private static final String HIGHSCORE_FILE = "res/scores.dat";

    ObjectOutputStream oStream = null;
    ObjectInputStream iStream = null;

    public HighscoreManager() {
        scores = new ArrayList<>();
    }

    public ArrayList<Score> getScores() {
        loadScoreFile();
        scores.sort(Score::compareTo);
        return scores;
    }

    private void sortScores() {
    }

    private void loadScoreFile() {
        File f = new File(HIGHSCORE_FILE);
        if (f.isFile() && f.canRead()) {
            try {
                iStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
                scores = (ArrayList<Score>) iStream.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("[Laad] FNF Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("[Laad] CNF Error: " + e.getMessage());
            } finally {
                try {
                    if (oStream != null) {
                        oStream.flush();
                        oStream.close();
                    }
                } catch (IOException e) {
                    System.out.println("[Laad] IO Error: " + e.getMessage());
                }
            }
        }
    }

    public void addScore(String name, int score) {
        loadScoreFile();
        scores.add(new Score(name, score));
        saveScoreFile();
    }

    private void saveScoreFile() {
        try {
            oStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            oStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (oStream != null) {
                    oStream.flush();
                    oStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    public String getHighscoreString() {
        StringBuilder highscoreString = new StringBuilder();
        int max = 3;

        ArrayList<Score> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString.append(i + 1).append(". ").append(scores.get(i).getName()).append("  ").append(scores.get(i).getScore()).append("\n");
            i++;
        }
        return highscoreString.toString();
    }
}
