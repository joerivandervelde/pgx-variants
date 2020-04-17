package org.molgenis.pgx.parser;

import java.io.File;

public class Main {

    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println("You must supply 2 arguments:");
            System.out.println("- PharmVar data directory (ie. extracted ZIP, for example ~/Downloads/pharmvar-4.1.4)");
            System.out.println("- Output file location (may not exist), for example ~/data/pharmvar-4.1.4-snps.tsv");
        }

        // TODO check if dir exists and is dir (must)
        // TODO check if output exists (may not)

        File phvdir = new File(args[0]);
        File outfile = new File(args[1]);

        PharmVarParser pvp = new PharmVarParser(phvdir, outfile);
        pvp.run();
    }
}
