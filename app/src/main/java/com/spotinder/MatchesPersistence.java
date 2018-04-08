package com.spotinder;

import android.util.Log;

import com.spotinder.entities.Match;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by aymeric on 30/03/2018.
 */

public class MatchesPersistence {
    private static final String TAG = "MatchesPersistence";

    public static final String MATCHES_PERSIST_FILE = "matches.dat";
    public static void save(List<Match> lst) {
        try {
            FileOutputStream fos = new FileOutputStream(MATCHES_PERSIST_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lst);
            oos.close();
        } catch(IOException err) {
            Log.e(TAG, "save: IOError while saving matches!", err);
        }
    }

    public static List<Match> load() {
        try {
            FileInputStream fis = new FileInputStream("t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Match> matches = (List<Match>) ois.readObject();
            ois.close();
            return matches;
        } catch (IOException err) {
            Log.e(TAG, "load: IOError while saving matches!", err);
        } catch (ClassNotFoundException err) {
            Log.e(TAG, "load: ClassNotFoundException while saving matches!", err);
        }

        return null;
    }
}
