package model;

public class Session {
    private static String currentUser;
    private static int currentUserId;

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void clear() {
        currentUser = null;
        currentUserId = 0;
    }
}
