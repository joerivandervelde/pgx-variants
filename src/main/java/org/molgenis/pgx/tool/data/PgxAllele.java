package org.molgenis.pgx.tool.data;

public class PgxAllele {
    public String allele;
    public String gene;
    public String function;
    public String molecule;

    public PgxAllele(String allele, String gene, String function,
                     String molecule) {
        this.allele = allele;
        this.gene = gene;
        this.function = function;
        this.molecule = molecule;
    }

    @Override public String toString() {
        return allele + "\t" + gene + "\t" + function + "\t" + molecule;
    }
}
