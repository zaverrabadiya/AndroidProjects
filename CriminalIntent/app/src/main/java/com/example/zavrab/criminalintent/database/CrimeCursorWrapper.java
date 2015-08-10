package com.example.zavrab.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.zavrab.criminalintent.Crime;

import java.util.Date;
import java.util.UUID;

import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SUSPECT;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID;

/**
 * Created by Zaver on 7/16/15.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(UUID));
        String title = getString(getColumnIndex(TITLE));
        long date = getLong(getColumnIndex(DATE));
        int isSolved = getInt(getColumnIndex(SOLVED));
        String suspect = getString(getColumnIndex(SUSPECT));

        Crime crime = new Crime(java.util.UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return  crime;
    }
}
