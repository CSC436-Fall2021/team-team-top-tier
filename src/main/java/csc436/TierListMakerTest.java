package csc436;

import csc436.Model.Picture;
import csc436.Model.Tier;
import csc436.Model.TierList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

public class TierListMakerTest {

    @Test
    public void loadSaveTierList(){
        saveTierList();
        TierList loadedTierList = new TierList("Temp");
        try {
            if((TierList) loadedTierList.load("Testing") != null) {
                loadedTierList = (TierList) loadedTierList.load("Testing");

            }
        }catch(Exception e) {

        }
        System.out.println(loadedTierList.getTiers().size());
        assertEquals(loadedTierList.getTiers().size(), 6);
        assertEquals(loadedTierList.getTiers().get(4).getTierTitle(), "D");
        //assertEquals(loadedTierList.getTiers().get(0).getPictures().get(0).getPath(), "testingPath");
    }


    public void saveTierList(){
        TierList myTier = new TierList("Testing");
        List<Tier> tiers = myTier.getTiers();
        tiers.add(new Tier("D", tiers.size()));
        tiers.add(new Tier("E", tiers.size()));
        //tiers.get(0).addPicture(new Picture("testingPath"));
        System.out.println(tiers.size());
        System.out.println(tiers.get(0).getTierTitle());
        try {
            myTier.saveTierList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
