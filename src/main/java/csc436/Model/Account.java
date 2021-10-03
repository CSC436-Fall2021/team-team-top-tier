package csc436.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Program: Account.java
 * Purpose: Account class that contains several properties of each Account (ie. Username,
 *          Password, list of TierList's).
 *
 * Created: 10/02/2021
 * @author Victor A. Jimenez Granados
 */
public class Account implements Serializable {
    private String usrname;
    private String pwd;
    private List<TierList> tierLists;

    /**
     * Purpose: Creates a new Account of the user.
     * @param usrname New Username
     * @param pwd New Password
     */
    public Account(String usrname, String pwd){
        this.usrname = usrname;
        this.pwd = pwd;
        tierLists = new ArrayList<TierList>();
    }

    /**
     * Purpose: Returns the password of this account.
     * @return pwd Account's password.
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Purpose: Returns the username of this account.
     * @return usrname Account's username.
     */
    public String getUsrname(){
        return usrname;
    }

    /**
     * Purpose: Sets the current password to a new password.
     * @param newPwd The new password for the Account.
     */
    public void changePwd(String newPwd){
        pwd = newPwd;
    }

    /**
     * Purpose: Returns the list of TierList's of this Account.
     * @return tierList The list of TierList's of the account.
     */
    public List<TierList> getTierLists() {
        return tierLists;
    }
}
