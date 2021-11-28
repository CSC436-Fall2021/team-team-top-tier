package csc436.Model;

import javafx.scene.paint.Color;

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
    private ArrayList<String> tagList;
    private SerializableColor backgroundColor;
    private SerializableColor tierRowColor;

    public TierList(String title) {
        tierListTitle = title;
        tiers = new ArrayList<Tier>();
        tierListStyle = "";
        tiers.add(new Tier("S", 0));
        tiers.add(new Tier("A", 1));
        tiers.add(new Tier("B", 2));
        tiers.add(new Tier("C", 3));
        tagList = new ArrayList<String>();
        backgroundColor = new SerializableColor(Color.BLACK);
        tierRowColor = new SerializableColor(Color.RED);
    }

    /**
     * Sets the tierLists's saved background color to the given color
     * @param c - color to save
     */
    public void setBackgroundColor(Color c) {
        backgroundColor = new SerializableColor(c);
    }

    /**
     * @return the background color that was saved in this tier list
     */
    public Color getBackgroundColor() {
        return backgroundColor.getFXColor();
    }

    /**
     * Sets the tier Rows's saved color to the given color
     * @param c - color to save
     */
    public void setTierRowColor(Color c) {
        tierRowColor = new SerializableColor(c);
    }

    /**
     * @return the tier row color that was saved in this tier list
     */
    public Color getTierRowColor() {
        return tierRowColor.getFXColor();
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

    /**
     * Add a tag to a tier list
     * @param tag
     */
    public void addTag(String tag) {
        // TODO: change/deal with special characters
        if (!tagList.contains(tag)) {
            tagList.add(tag);
        }
    }
    public void removeTag(String tag) {
        if (tagList.contains(tag)) {
            tagList.remove(tag);
        }
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }
    public String getTagListString() {
        String tagContent = "";
        for (String tag: tagList) {
            tagContent += "#" + tag + "  ";
        }
        return tagContent;
    }

    public String formatTag(String tag) {
        String formattedTag = "";
        formattedTag = tag.replaceAll("\s", "-");
        return formattedTag;
    }
}
