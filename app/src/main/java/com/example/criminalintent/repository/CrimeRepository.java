package com.example.criminalintent.repository;

import com.example.criminalintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository implements IRepository {

    private static final int CRIME_SIZE = 5;
    private static CrimeRepository sInstance;

    public static CrimeRepository getInstance() {
        if (sInstance == null)
            sInstance = new CrimeRepository();

        return sInstance;
    }

    private List<Crime> mCrimes;

    private CrimeRepository() {
        //create dummy object for test.
        mCrimes = new ArrayList<>();
        for (int i = 0; i < CRIME_SIZE; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime#" + (i + 1));
            crime.setSolved(i % 2 == 0);
            crime.setSelected(false);

            mCrimes.add(crime);
        }
    }

    @Override
    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    @Override
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id))
                return crime;
        }

        return null;
    }

    @Override
    public void insertCrime(Crime crime) {
        mCrimes.add(crime);
    }

    @Override
    public void deleteCrime(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crime.getId())) {
                mCrimes.remove(i);
                return;
            }
        }
    }

    @Override
    public void updateCrime(Crime crime) {
        Crime findCrime = getCrime(crime.getId());
        findCrime.setTitle(crime.getTitle());
        findCrime.setSolved(crime.isSolved());
        findCrime.setDate(crime.getDate());
    }

    @Override
    public int getPosition(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                return i;
        }

        return 0;
    }

    @Override
    public void deleteAllSelected() {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).isSelected())
                mCrimes.remove(i);
        }
    }

    @Override
    public void selectAll() {
        for (int i = 0; i < mCrimes.size(); i++) {

                mCrimes.get(i).setSelected(true);
        }
    }

    @Override
    public void unSelectAll() {
        for (int i = 0; i < mCrimes.size(); i++) {

            mCrimes.get(i).setSelected(false);
        }

    }
}
