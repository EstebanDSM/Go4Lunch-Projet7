package com.guzzler.go4lunch_p7.utils;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;

import java.util.ArrayList;
import java.util.List;

public class CompareDistance {

    public static List<ResultSearch> compare(List<ResultSearch> mResultSearchList) {


        List<ResultSearch> tempList = new ArrayList<>();


        for (int i = 0; i < mResultSearchList.size(); i++) {


            if (mResultSearchList.get(i).getDistance() < mResultSearchList.get(0).getDistance()) {
                tempList.add(0, mResultSearchList.get(i));
            } else {

                tempList.add(mResultSearchList.get(i));
            }


        }
        return tempList;
    }
}