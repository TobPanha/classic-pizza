package com.scentify.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Lightweight storage for demo authentication state.
 * Handles a single user email/password pair for the sample app.
 */
public class SessionManager {
    private static final String PREF_NAME = "scentify_auth";
    private static final String KEY_EMAIL = "auth_email";
    private static final String KEY_PASSWORD = "auth_password";
    private static final String KEY_SIGNED_IN = "auth_signed_in";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isSignedIn() {
        return preferences.getBoolean(KEY_SIGNED_IN, false) && !TextUtils.isEmpty(getEmail());
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public boolean hasAccount() {
        return !TextUtils.isEmpty(preferences.getString(KEY_EMAIL, ""));
    }

    public boolean signIn(String email, String password) {
        String savedEmail = preferences.getString(KEY_EMAIL, "");
        String savedPassword = preferences.getString(KEY_PASSWORD, "");
        boolean valid = !TextUtils.isEmpty(savedEmail)
                && savedEmail.equalsIgnoreCase(email.trim())
                && savedPassword.equals(password);
        if (valid) {
            preferences.edit().putBoolean(KEY_SIGNED_IN, true).apply();
        }
        return valid;
    }

    public void register(String email, String password) {
        preferences.edit()
                .putString(KEY_EMAIL, email.trim())
                .putString(KEY_PASSWORD, password)
                .putBoolean(KEY_SIGNED_IN, true)
                .apply();
    }

    public void signOut() {
        preferences.edit().putBoolean(KEY_SIGNED_IN, false).apply();
    }
}
