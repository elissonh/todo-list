package com.devlson.to_do_list.mappers;

public interface Mapper<A, B> {

    B mapTo(A a);

    A mapFrom(B a);
}
