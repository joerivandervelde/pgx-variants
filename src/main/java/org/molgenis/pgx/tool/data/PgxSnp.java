package org.molgenis.pgx.tool.data;

public class PgxSnp {
    public String GenomeBuild;
    public String Gene;
    public String ChrId;
    public String Allele;
    public String PharmVarId;
    public String Chr;
    public String Pos;
    public String SnpId;
    public String Ref;
    public String Alt;

    @Override public String toString() {
        return GenomeBuild + "\t" + Gene + "\t" + ChrId + "\t" + Allele + "\t" + PharmVarId + "\t" + Chr + "\t" + Pos + "\t" + SnpId + "\t" + Ref + "\t" + Alt;
    }
}
