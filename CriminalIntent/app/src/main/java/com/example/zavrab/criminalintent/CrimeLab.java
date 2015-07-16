package com.example.zavrab.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zavrab.criminalintent.database.CrimeBaseHelper;
import com.example.zavrab.criminalintent.database.CrimeDbSchema;
import com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE;
import static com.example.zavrab.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID;

/**
 * Created by zavrab on 7/8/15.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {

        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public List<Crime> getCrimes() {
        return new ArrayList<>();
    }

    public Crime getCrime(UUID id) {
        return null;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void removeCrime(Crime c) {

    }

    public void updateCrime(Crime c) {
        String uuidString = c.getId().toString();
        ContentValues values = getContentValues(c);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString});

    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(UUID, crime.getId().toString());
        values.put(TITLE, crime.getTitle());
        values.put(DATE, crime.getDate().toString());
        values.put(SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }
}
