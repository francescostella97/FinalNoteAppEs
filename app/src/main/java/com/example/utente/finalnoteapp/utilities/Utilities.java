package com.example.utente.finalnoteapp.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class Utilities {
    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
