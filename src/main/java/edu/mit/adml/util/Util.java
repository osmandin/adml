package edu.mit.adml.util;

import java.util.HashMap;
import java.util.Map;

public class Util {

    // FIXME: clean up

    public static Map<Integer, String> getFormats() {
        final Map<Integer, String> formats = new HashMap<>();

        final String[] formatsStr = new String[] {"CD", "CD-R", "CD-ROM", "CD-RW", "DVD", "DVD+R", "DVD+R DL", "DVD+RW", "DVD-R", "DVD-RW", "Flash Memory: USB", "Floppy disk: 3 1/2 inch", "Floppy disk: 5 1/4 inch", "Hard Drive", "Hard Drive: eSATA", "Hard Drive: Firewire", "Hard Drive: PATA", "Hard Drive: SCSI", "Hard Drive: USB", "Memory Card", "Minidisc", "Zip disk"};


        int i = 0;

        for (String f : formatsStr) {
            formats.put(i, f);
            i++;
        }

        return formats;
    }

    public static Map<Integer, String> getTransferStatus() {
        final Map<Integer, String> formats = new HashMap<>();

        final String[] formatsStr = new String[] {"Not Transferred", "Transferred - Success", "Transferred - Failed"};
        //
        int i = 0;

        for (String f : formatsStr) {
            formats.put(i, f);
            i++;
        }

        return formats;
    }

    public static Map<Integer, String> getDispositions() {
        final Map<Integer, String> formats = new HashMap<>();

        final String[] formatsStr = new String[] {"Retain", "Destroy"};
        //
        int i = 0;

        for (String f : formatsStr) {
            formats.put(i, f);
            i++;
        }

        return formats;
    }

    public static Map<Integer, String> getTransferMethods() {
        final Map<Integer, String> formats = new HashMap<>();

        final String[] formatsStr = new String[] {"Disk Image", "Rip Tracks", "Copy Files"};
        //
        int i = 0;

        for (String f : formatsStr) {
            formats.put(i, f);
            i++;
        }

        return formats;
    }






}
