package com.momentum.dosein.util;

import com.momentum.dosein.model.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User u) { currentUser = u; }
    public static User getCurrentUser()       { return currentUser; }
    public static void clear()                { currentUser = null; }
}
