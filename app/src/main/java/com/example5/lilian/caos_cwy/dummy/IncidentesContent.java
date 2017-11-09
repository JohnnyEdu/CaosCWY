package com.example5.lilian.caos_cwy.dummy;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.tasks.IncidenteSELECTTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class IncidentesContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Incidente> ITEMS = new ArrayList<Incidente>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Incidente> ITEM_MAP = new HashMap<String, Incidente>();

    private static final int COUNT = 25;

    //TODO: traer de la base
   /* static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

    public static void addItem(Incidente item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId()), item);
    }

    public IncidentesContent(){
    }


    /*private static Incidente createDummyItem(int position) {
        return new Incidente(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    /*private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/


}
