package com.example.criminalintent.repository;

import com.example.criminalintent.model.Crime;

import java.util.List;
import java.util.UUID;

public interface IRepository {
    List<Crime> getCrimes();

    Crime getCrime(UUID crimeId);

    void insertCrime(Crime crime);

    void updateCrime(Crime crime);

    void deleteCrime(Crime crime);

    int getPosition(UUID crimeId);

    void deleteAllSelected();

    void selectAll();

    void unSelectAll();
}
