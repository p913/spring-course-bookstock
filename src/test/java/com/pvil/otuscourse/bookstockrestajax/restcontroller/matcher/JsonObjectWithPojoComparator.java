package com.pvil.otuscourse.bookstockrestajax.restcontroller.matcher;

public interface JsonObjectWithPojoComparator<T> {
    boolean equals(T pojo, Object jsonObject);
}
