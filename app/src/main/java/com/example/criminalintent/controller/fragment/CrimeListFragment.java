package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimePagerActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.util.List;

public class CrimeListFragment extends Fragment {

    public static final String TAG = "CLF";
    public static final String BUNDLE_ARG_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";
    public static final int REQUEST_CODE_CRIME_DETAIL = 2;

    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    private IRepository mRepository;
    private LinearLayout mListExist;
    private LinearLayout mListEmpty;
    private Button mBtnAddCrime;
    private boolean mIsSubtitleVisible = false;

    public static CrimeListFragment newInstance() {

        Bundle args = new Bundle();

        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mRepository = CrimeRepository.getInstance();

        if (savedInstanceState != null)
            mIsSubtitleVisible =
                    savedInstanceState.getBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        findViews(view);
        initViews();
        setListener();
        return view;
    }

    private void setListener() {
        mBtnAddCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeRepository.getInstance().insertCrime(crime);

                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cime_list, menu);

        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        setMenuItemSubtitle(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:

                Crime crime = new Crime();
                CrimeRepository.getInstance().insertCrime(crime);

                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);

                return true;
            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;

                updateSubtitle();
                setMenuItemSubtitle(item);

                return true;
            case R.id.delete_crime:
                mRepository.deleteAllSelected();
                mRepository=CrimeRepository.getInstance();
                updateUI();
                return true;
            case R.id.select_all:
                mRepository.selectAll();

                updateUI();
                return true;
            case R.id.unSelect_all:
                mRepository.unSelectAll();

                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
        updateSubtitle();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
        mListExist = view.findViewById(R.id.list_crime);
        mListEmpty=view.findViewById(R.id.empty_layout);
        mBtnAddCrime=view.findViewById(R.id.btn_add_crime);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void updateUI() {
        List<Crime> crimes = mRepository.getCrimes();
        if (crimes.size()==0){
            mListEmpty.setVisibility(View.VISIBLE);
            mListExist.setVisibility(View.GONE);
        }
        else{
            mListEmpty.setVisibility(View.GONE);
            mListExist.setVisibility(View.VISIBLE);

        }
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }

    }

    private void updateSubtitle() {
        int numberOfCrimes = CrimeRepository.getInstance().getCrimes().size();
        String crimesText = mIsSubtitleVisible ? numberOfCrimes + " crimes" : null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(crimesText);
    }

    private void setMenuItemSubtitle(MenuItem item) {
        item.setTitle(
                mIsSubtitleVisible ?
                        R.string.menu_item_hide_subtitle :
                        R.string.menu_item_show_subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        private CheckBox mIsSelected;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);
            mIsSelected = itemView.findViewById(R.id.select_crime);
            mIsSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsSelected.isChecked())
                        mCrime.setSelected(true);
                    else
                        mCrime.setSelected(false);
                }

            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivityForResult(intent, REQUEST_CODE_CRIME_DETAIL);
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mIsSelected.setChecked(crime.isSelected());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data==null)
            return;
        else if (requestCode==REQUEST_CODE_CRIME_DETAIL){
            mRepository=CrimeRepository.getInstance();
            updateUI();
        }

    }
}