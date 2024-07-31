package net.vassbo.vanillaemc.data;

import java.util.ArrayList;
import java.util.List;
// https://fabricmc.net/wiki/tutorial:persistent_states#more_involved_player_data
public class PlayerData {
    public int EMC = 0;
    public List<String> LEARNED_ITEMS = new ArrayList<>();

    // not stored
    // public int learnedItemsSize = 0;
    public String MESSAGE = "";
}
