package com.pvil.otuscourse.bookstockmvcformlogin.changelog;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;
import org.bson.types.ObjectId;

public class EtalonUsersForTests {
    private static User reader =
            new User(new ObjectId().toHexString(), "reader", "password", "Full Name", "e@mail.com", false);

    public static User getReferenceToReader() {
        return reader;
    }

    public static User getExistentReader() {
        return new User(reader);
    }

    public static User getCanBeAddedReader() {
        return new User("added", "password2", "Full Name 2", "e2@mail.com", false);
    }
}
