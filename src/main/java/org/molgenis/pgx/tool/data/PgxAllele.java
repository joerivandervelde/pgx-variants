package org.molgenis.pgx.tool.data;

public class PgxAllele {
    public String allele;
    public String function;
    public String molecule;

    public PgxAllele(String allele, String function, String molecule) {
        this.allele = allele;
        this.function = function;
        this.molecule = molecule;
    }
}
