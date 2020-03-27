package org.molgenis.pgx.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println("You must supply 2 arguments:");
            System.out.println("- PharmVar data directory (ie. extracted ZIP)");
            System.out.println("- Output file location (may not exist)");
            System.out.println("For instance:");
            System.out.println("java -jar pgx-variants.jar " +
                    "/Users/joeri/Downloads/pharmvar-4.1.4 " +
                    "/Users/joeri/Downloads/pharmvar-4.1.4-snps.tsv");
        }

        // todo check if dir exists and is dir (must)
        // todo check if output exists (may not)

        File phvdir = new File(args[0]);
        File outfile = new File(args[1]);

        PharmVarParser pvp = new PharmVarParser(phvdir, outfile);
        pvp.run();
    }
}
