package com.example5.lilian.caos_cwy.dummy;

import android.util.Log;

import com.example5.lilian.caos_cwy.database.Incidente;

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
    public static  ArrayList<Incidente> TODOS_LOS_INCIDENTES = new ArrayList<Incidente>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Incidente> ITEM_MAP = new HashMap<String, Incidente>();
    public static Map<String, List<Incidente>> INCIDENTES_AGRUPADOS = new HashMap<String, List<Incidente>>();


    private static final int COUNT = 25;

    //TODO: traer de la base
   /* static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

   public static void removeItem(Incidente incidente){
       TODOS_LOS_INCIDENTES.remove(incidente);
       INCIDENTES_AGRUPADOS.get(incidente.getTipo()).remove(incidente);
       ITEM_MAP.remove(incidente.getId().toString());
   }

   public static void reset(){
       TODOS_LOS_INCIDENTES = new ArrayList<>();
       INCIDENTES_AGRUPADOS = new HashMap<>();
   }


    public static void fillContent() {
       for(int i =0; i< TODOS_LOS_INCIDENTES.size();i++){
           Incidente item = TODOS_LOS_INCIDENTES.get(i);
           if(item!=null){
               ITEM_MAP.put(String.valueOf(item.getId()),item);
               addListaAgrupada(item.getTipo(),item);
           }else{
               Log.d("ITEM NULL","El item null es : "+ i);
           }

       }

       /*for(Incidente item: TODOS_LOS_INCIDENTES){
           //TODO: ver porque viene null item
           ITEM_MAP.put(String.valueOf(item.getId()),item);
           addListaAgrupada(item.getTipo(),item);
       }*/
    }

    public static void addItem(Incidente item) {

       TODOS_LOS_INCIDENTES.add(item);
       ITEM_MAP.put(String.valueOf(item.getId()),item);
       addListaAgrupada(item.getTipo(),item);
    }



    public static void addListaAgrupada(String grupo, Incidente item) {
        List<Incidente> listaAactualizar = INCIDENTES_AGRUPADOS.get(grupo)!=null?INCIDENTES_AGRUPADOS.get(grupo):new ArrayList<Incidente>();
        listaAactualizar.add(item);
        INCIDENTES_AGRUPADOS.put(grupo,listaAactualizar);
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
