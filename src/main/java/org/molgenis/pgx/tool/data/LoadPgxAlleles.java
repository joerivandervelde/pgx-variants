package org.molgenis.pgx.tool.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadPgxAlleles {

    private File src;
    private List<PgxAllele> allList;

    /**
     * Constructor
     * @param src
     */
    public LoadPgxAlleles(File src)
    {
        this.src = src;
        this.allList = new ArrayList<PgxAllele>();
    }

    /**
     * Return data list
     * @return
     */
    public List<PgxAllele> retrieve()
    {
        return this.allList;
    }

    /**
     * Load data from file source into list
     * @throws FileNotFoundException
     */
    public void load() throws FileNotFoundException {
        Scanner s = new Scanner(this.src);
        String line;
        while (s.hasNextLine()) {
            line = s.nextLine();
            if (line.startsWith("#")) {
                continue;
            }
            String[] split = line.split("\t", -1);
            PgxAllele pgxa = new PgxAllele(split[0], split[1], split[2],
                    split[3]);
            this.allList.add(pgxa);
        }
    }
}
