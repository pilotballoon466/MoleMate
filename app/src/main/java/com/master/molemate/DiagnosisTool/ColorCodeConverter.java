package com.master.molemate.DiagnosisTool;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ColorCodeConverter {


    String bodypart;

    private final static Map<Integer, String[]> colorToBodypart;

    static{
        Map<Integer, String[]> tmp = new LinkedHashMap<Integer, String[]>();
        tmp.put(-66338, new String[]{"Linke Hand", "Linker Handrücken"} );
        tmp.put(-57088, new String[]{"Linker Unterarm", "Linker hinterer Unterarm"});
        tmp.put(-27648, new String[]{"Linke Oberarm", "Linker hinterer Oberarm"});
        tmp.put(-16713472, new String[]{"Kopf", "Hinterkopf"});
        tmp.put(-1280, new String[]{"Brust", "Oberer Rücken"});
        tmp.put(-16712193, new String[]{"Bauch", "Unterer Rücken"});
        tmp.put(-16764673, new String[]{"Hüfte", "Gesäß"});
        tmp.put(-10747648, new String[]{"Rechte Hand", "Rechter Handrücken"});
        tmp.put(-11633884, new String[]{"Rechter Unterarm", "Rechter hinterer Unterarm"});
        tmp.put(-5075458, new String[]{"Rechter Oberarm", "Rechter hinterer Oberarm"});
        tmp.put(-49409, new String[]{"Linke Oberschenkel", "Linker hinterer Oberschenkel"});
        tmp.put(-5539264, new String[]{"Linkes Scheinbein", "Linke Wade"});
        tmp.put(-7171438, new String[]{"Linker Fuß", "Fußsole"});
        tmp.put(-7005293, new String[]{"Rechter Oberschenkel", "Rechter hinterer Oberschenkel"});
        tmp.put(-14932, new String[]{"Rechtes Schienbein", "Rechte Wade"});
        tmp.put(-16777216, new String[]{"Rechter Fuß", "Rechte Fußsole"});


        colorToBodypart = Collections.unmodifiableMap(tmp);
    }


    public static String[] getBodyPartFromColor(int colorID){
        return colorToBodypart.get(colorID);
    }

}

