package org.molgenis.pgx.tool.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadPgxSnps {

    private File src;
    private List<PgxSnp> snpList;

    /**
     * Constructor
     * @param src
     */
    public LoadPgxSnps(File src)
    {
        this.src = src;
        this.snpList = new ArrayList<PgxSnp>();
    }

    /**
     * Return data list
     * @return
     */
    public List<PgxSnp> retrieve()
    {
        return this.snpList;
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
            if (line.startsWith("#GenomeBuild")) {
                continue;
            }
            String[] split = line.split("\t", -1);
            PgxSnp ps = new PgxSnp();
            ps.GenomeBuild = split[0];
            ps.Gene = split[1];
            ps.ChrId = split[2];
            ps.Allele = split[3];
            ps.PharmVarId = split[4];
            ps.Chr = split[5];
            ps.Pos = split[6];
            ps.SnpId = split[7];
            ps.Ref = split[8];
            ps.Alt = split[9];
            this.snpList.add(ps);
        }
    }


}
