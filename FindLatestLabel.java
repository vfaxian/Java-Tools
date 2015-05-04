package com.tools.liyf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class is to find the latest label. if we have there labels:
 * 6.2.B.0.155, 6.2.B.0.180, 6.2.B.0.192, 6.2.B.0.196, 6.2.B.0.197.
 * The latest label is 6.2.B.0.197. JB-MR2-BLUE-140424-1222,
 * JB-MR2-BLUE-140526-0359, JB-MR2-BLUE-140528-1406 The latest label
 * is JB-MR2-BLUE-140528-1406. LAGAN-1.4-150415-0630,
 * LAGAN-1.4-150415-0711, LAGAN-1.4-150415-0724 The latest label is
 * LAGAN-1.4-150415-0724.
 * 
 * @author Jerry.li
 */
public class FindLatestLabel {

    public FindLatestLabel() {
    }

    public static void main(String[] args) {
        FindLatestLabel fll = new FindLatestLabel();

        // The args[0] is the name of file which has a list of the
        // labels, group by labels name and order by version.
        fll.startDoIt(args[0]);
    }

    /**
     * the job
     */
    public void startDoIt(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("------read files by line------");
            reader = new BufferedReader(new FileReader(file));
            String label = null;
            String sameString = null;
            String previousLabel = null;
            while ((label = reader.readLine()) != null) {
                if (sameString != null && label.startsWith(sameString)) {
                } else {
                    if (previousLabel != null) {
                        System.out.println(previousLabel);
                    }
                    if (label.split("\\.").length > 2) {
                        sameString = label.substring(0, label.lastIndexOf("."));
                    } else if (label.split("-").length >= 3) {
                        Matcher m = Pattern.compile("^(.*([a-zA-Z]|[a-zA-Z]\\d))+-").matcher(label);
                        while (m.find()) {
                            sameString = m.group(1);
                        }
                    }
                }
                previousLabel = label;
            }
            // print the last one.
            System.out.println(previousLabel);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

}
