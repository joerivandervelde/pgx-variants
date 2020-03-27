package org.molgenis.pgx.tool.data;

public class PgxAllele {
    public String allele;
    public String function;
    public String drug;

    public PgxAllele(String allele, String function, String drug) {
        this.allele = allele;
        this.function = function;
        this.drug = drug;
    }
}
