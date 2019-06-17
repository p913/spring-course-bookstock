package com.pvil.otuscourse.bookstockmvcjpaacl.etalondata;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.User;

public class EtalonUsersForTests {
    private static User reader =
            new User(2, "reader", "$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq",
                    "Reader", "r@bookstock.com", false);

    private static User stockKeeper =
            new User(3, "stockkeeper", "$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq",
                    "StockKeeper", "sk@bookstock.com", true);

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
