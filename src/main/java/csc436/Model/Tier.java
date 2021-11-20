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
 * Purpose: TierList class that contains several properties of each TierList (ie. List of Tiers,
 *          theme of layout, title of TierList, etc.).
 *
 * Created: 09/29/2021
 * @author Victor A. Jimenez Granados
 */
public class Tier implements Serializable {
    private String tierTitle;
    private List<Picture> pictures;
    private String tierTitleStyle;
    private String tierBackgroundStyle;
    private int tierIndex;

    /**
     * Purpose: Creates a new empty tier with the given rank (title).
     * @param title The rank of this Tier.
     */
    public Tier(String title, int index) {
        tierTitle = title;
        pictures = new ArrayList<Picture>();
        tierTitleStyle = "";
        tierBackgroundStyle = "";
        tierIndex = index;
    }

    /**
     * Purpose: Returns the Tier's title.
     * @return tierTitle
     */
    public String getTierTitle() {
        return tierTitle;
    }

    /**
     * Purpose: Sets a new Tier rank/title.
     * @param tierTitle The new rank of this Tier.
     */
    public void setTierTitle(String tierTitle) {
        this.tierTitle = tierTitle;
    }

    /**
     * Purpose: Returns the array of Pictures (which are file paths for imgs).
     * @return pictures The Picture array which holds file paths of images for this Tier.
     */
    public List<Picture> getPictures() {
        return pictures;
    }

    /**
     * Purpose: Adds a new image (aka Picture) to this Tier.
     * @param pic
     */
    public void addPicture(Picture pic) {
        pictures.add(pic);
    }

    /**
     * Purpose: Removes the selected image (aka Picture) from this Tier.
     * @param pic
     */
    public void removePicture(Picture pic) {
        pictures.remove(pic);
    }

    /**
     * Purpose: Returns the Tier css style for this Tier's rank/title.
     * @return tierTitleStyle CSS string which holds the styling of this Tier's rank/title.
     */
    public String getTierTitleStyle() {
        return tierTitleStyle;
    }

    /**
     * Purpose: Sets a new CSS style for the Tier's rank/title.
     * @param tierTitleStyle The new CSS for this rank/title.
     */
    public void setTierTitleStyle(String tierTitleStyle) {
        this.tierTitleStyle = tierTitleStyle;
    }

    /**
     * Purpose: Returns the Tier CSS style for this Tier's background.
     * @return tierBackgroundStyle CSS string which holds the styling of this Tier's background.
     */
    public String getTierBackgroundStyle() {
        return tierBackgroundStyle;
    }

    /**
     * Purpose: Sets a new CSS style for the Tier's background.
     * @param tierBackgroundStyle The new CSS for this Tier's background.
     */
    public void setTierBackgroundStyle(String tierBackgroundStyle) {
        this.tierBackgroundStyle = tierBackgroundStyle;
    }

    /**
     * Purpose: Returns the index of this Tier within a TierList.
     * @return tierIndex Index of Tier.
     */
    public int getTierIndex() {
        return tierIndex;
    }

    /**
     * Purpose: Sets this Tier's index to an updated index.
     * @param tierIndex The new index of this Tier.
     */
    public void setTierIndex(int tierIndex) {
        this.tierIndex = tierIndex;
    }

    /**
     * Purpose: Serializes and saves the Tier.
     * @throws Exception
     */
    public void saveTier() throws Exception{
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(/* TODO Implement: TierList.getTierTitle() + */ "_tier_" + tierIndex + ".dat")))){
            oos.writeObject(this);
        }
    }
    /**
     * Loads our file with a Tier.
     * @return the Tier instance that was saved on the tierListTitle_tier_index.dat file
     *
     */
    public Object load(String title, String index) throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(title + "_tier_" + index + ".dat")))){
            return ois.readObject();
        }
    }


    @Override
    public String toString() {
        String s = "Tier Title: " + tierTitle + "\nPictures: " + pictures.toString() + "\nTier Title CSS: " + tierTitleStyle
                + "\nTier Background CSS: " + tierBackgroundStyle;
        return s;
    }

}