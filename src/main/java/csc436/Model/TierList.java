package csc436.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Program: TierList.java
 * Purpose: TierList class that contains several properties of a unique TierList (ie. List of Tiers,
 *          theme of layout, title of TierList, etc.).
 *
 * Created: 09/29/2021
 * @author Victor A. Jimenez Granados
 */
public class TierList implements Serializable {
    private String tierListTitle;
    private List<Tier> tiers;
    private String tierListStyle;

    public TierList(String title) {
        tierListTitle = title;
        tiers = new ArrayList<Tier>();
        tierListStyle = "";
        tiers.add(new Tier("S", 0));
        tiers.add(new Tier("A", 1));
        tiers.add(new Tier("B", 2));
        tiers.add(new Tier("C", 3));
    }

    /**
     * Purpose: Returns the title of the Tier List.
     * @return tierListTitle Name/title of the Tier List.
     */
    public String getTierListTitle() {
        return tierListTitle;
    }

    /**
     * Purpose: Sets a new Tier List title.
     * @param tierListTitle The new title/name of the Tier List.
     */
    public void setTierListTitle(String tierListTitle) {
        this.tierListTitle = tierListTitle;
    }

    /**
     * Purpose: Returns a list of all the Tiers of the TierList.
     * @return tiers List of all the Tiers in the TierList.
     */
    public List<Tier> getTiers() {
        return tiers;
    }

    /**
     * Purpose: Returns the css style for this TierList.
     * @return tierListStyle CSS string which holds the styling of this TierList.
     */
    public String getTierListStyle() {
        return tierListStyle;
    }

    /**
     * Purpose: Sets a new CSS style for the TierList.
     * @param tierListStyle The new CSS for this tier list.
     */
    public void setTierListStyle(String tierListStyle) {
        this.tierListStyle = tierListStyle;
    }

    /*
    TODO: Determine if all Serializable classes need load/save methods.
     */

    /**
     * Purpose: Serializes and saves the TierList.
     * @throws Exception
     */
    public void saveTierList() throws Exception{
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(tierListTitle + "_tierlist.dat")))){
            oos.writeObject(this);
        }
    }

    /**
     * Loads our file with a Tier.
     * @return the Tier instance that was saved on the tierListTitle_tier_index.dat file
     *
     */
    public Object load(String title) throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(title + "_tierlist.dat")))){
            /*if (ois.readObject() instanceof TierList){
                return (TierList) ois.readObject();
            }else{
                return null;
            }*/
            return ois.readObject();
        }
    }

}
