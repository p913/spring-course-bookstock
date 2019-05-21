package com.pvil.otuscourse.bookstockrestajax.restcontroller.matcher;

import net.minidev.json.JSONArray;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonArrayWithPojoListMatcher<T> extends BaseMatcher<T> {
    private final List<T> pojoList;

    private final JsonObjectWithPojoComparator<T> comparator;

    public JsonArrayWithPojoListMatcher(List<T> pojoList, JsonObjectWithPojoComparator<T> comparator) {
        this.pojoList = pojoList;
        this.comparator = comparator;
    }

    @Override
    public boolean matches(Object o) {
        if (o instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray)o;
            if (jsonArray.size() == pojoList.size()) {
                Set<Integer> matchedIndexes = new HashSet<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object j = jsonArray.get(i);
                    if (pojoList.stream().anyMatch(a -> comparator.equals(a, j)))
                            matchedIndexes.add(i);
                }
                return matchedIndexes.size() == pojoList.size();
            }
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("examined object is JSON-array and same as list of POJO: ").appendValue(pojoList);
    }

}
