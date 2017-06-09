package edu.ucsd.dj.models;

import android.accounts.Account;
import android.accounts.AccountManager;

import edu.ucsd.dj.managers.DJPhoto;

/**
 * Created by Jake Sutton on 6/4/17.
 * To signify the primary user
 */

public class DJPrimaryUser extends DJUser {

    public DJPrimaryUser() {
        super("Primary User", "Unknown");
        userId = "default";
        loadEmail();
    }

    public void loadEmail() {

        AccountManager accountManager = AccountManager.get( DJPhoto.getAppContext() );
        Account account = getAccount(accountManager);

        if (account != null) {
            this.email = account.name;
            this.userId = email.substring(0, email.indexOf("@"));
        }
    }

    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;

        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }

        return account;
    }
}
