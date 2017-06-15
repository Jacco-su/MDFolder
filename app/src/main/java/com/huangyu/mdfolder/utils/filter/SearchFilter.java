package com.huangyu.mdfolder.utils.filter;

import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by huangyu on 2017-5-24.
 */
public class SearchFilter implements FilenameFilter {

    private String mSearchStr;

    public SearchFilter(String searchStr) {
        this.mSearchStr = searchStr;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (TextUtils.isEmpty(mSearchStr)) {
            return !name.startsWith(".");
        }
        return name.contains(mSearchStr) && !name.startsWith(".");
    }

}