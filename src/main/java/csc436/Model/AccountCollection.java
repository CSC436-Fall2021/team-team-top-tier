package csc436.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
/**
 * Program: AccountCollection.java
 * Purpose: AccountCollection class that contains a list/database of Account's of this application.
 *
 * Created: 10/02/2021
 * @author Victor A. Jimenez Granados
 */
public class AccountCollection implements Serializable {
    private Set<Account> accCollection;

    /**
     * Purpose: Creates a new list/database of Account's.
     */
    public AccountCollection() {
        accCollection = new HashSet<Account>();
    }

    /**
     * Purpose: Returns the list of Accounts of this application.
     * @return accCollection The list of Accounts.
     */
    public Set<Account> getAccCollection() {
        return accCollection;
    }

    /**
     * Purpose: Adds a new Account to the current list/database.
     * @param newAcc The new Account created.
     */
    public void addAccToCollection(Account newAcc){
        accCollection.add(newAcc);
    }

    /**
     * Purpose: Attempts to login if an Account with the given username and password is found.
     * @param usrname The username entered.
     * @param pwd The password entered.
     * @return true/false Either T or F if the Account was found in the Account list/database.
     */
    public boolean logIn(String usrname, String pwd) {
        return getAccount(usrname, pwd) != null;
    }

    /**
     * Purpose: Returns the specified Account.
     * @param usrname The username of the Account.
     * @param pwd The password of the Account.
     * @return user Returns the Account found by the given username and password, or null if not found.
     */
    public Account getAccount(String usrname, String pwd) {
        Account user = null;
        for (Account acc : accCollection) {
            if (acc.getUsrname().equals(usrname) && acc.getPwd().equals(pwd)){
                user = acc;
            }
        }
        return user;
    }

    /**
     * Purpose: Serializes and saves the AccountCollection (database).
     * @throws Exception
     */
    public void saveAccountCollection() throws Exception{
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("accountCollection.dat")))){
            oos.writeObject(this);
        }
    }

    /**
     * Loads an AccountCollection (database).
     * @return the Tier instance that was saved on the tierListTitle_tier_index.dat file
     *
     */
    public Object loadAccountCollection() throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("accountCollection.dat")))){
            return ois.readObject();
        }
    }
}
