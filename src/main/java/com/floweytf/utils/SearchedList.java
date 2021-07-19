package com.floweytf.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchedList<T> extends AbstractList<T> {
    private String str;
    private List<Integer> locations = new ArrayList<>();
    private final List<T> source;
    private final Function<T, String> searcher;

    public SearchedList(List<T> source, Function<T, String> searcher) {
        this.source = source;
        this.searcher = searcher;
        this.str = "";
    }

    @Override
    public T get(int index) {
        if (str.length() == 0) {
            return source.get(index);
        }
        return source.get(locations.get(index));
    }

    @Override
    public int size() {
        if (str.length() == 0) {
            return source.size();
        }
        return locations.size();
    }

    public void setSearchStr(String str) {
        str = str.toLowerCase();

        if (str.contains(this.str) && str.length() != 1) {
            this.str = str;
            // redo search
            List<Integer> other = new ArrayList<>();
            for (Integer location : locations) {
                String name = searcher.apply(source.get(location)).toLowerCase();
                if (name.contains(str)) {
                    other.add(location);
                }
            }
            locations = other;
        }

        this.str = str;

        locations.clear();
        for (int i = 0; i < source.size(); i++) {
            String name = searcher.apply(source.get(i)).toLowerCase();
            if (name.contains(str)) {
                locations.add(i);
            }
        }
    }

    public void reset() {
        str = "";
    }

    public String getSearchStr() {
        return str;
    }
}