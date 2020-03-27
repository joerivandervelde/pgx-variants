package org.molgenis.pgx.tool;

import org.molgenis.pgx.tool.data.LoadPgxAlleles;
import org.molgenis.pgx.tool.data.LoadPgxSnps;
import org.molgenis.pgx.tool.data.PgxAllele;
import org.molgenis.pgx.tool.data.PgxSnp;
import htsjdk.tribble.readers.TabixReader;
import net.sf.samtools.util.BlockCompressedInputStream;
import org.molgenis.genotype.Allele;
import org.molgenis.vcf.VcfReader;
import org.molgenis.vcf.VcfRecord;
import org.molgenis.vcf.VcfSample;
import org.molgenis.vcf.meta.VcfMeta;

import java.io.File;
import java.util.List;

import static org.molgenis.pgx.tool.GenomeBuild.b37;

public class Main {

    public static void main(String args[]) throws Exception {
        if(args.length != 5)
        {
            System.out.println("Please supply:");
            System.out.println("- File location of 'pharmvar-4.1.4-snps.tsv'");
            System.out.println("- File location of 'pharmvar-alleles-function.txt'");
            System.out.println("- If you use genome build 37 or 38 ('b37' or 'b38')");
            System.out.println("- File location of GZipped VCF to be analyzed");
            System.out.println("- Output file location");
        }


        File allF = new File("/Users/joeri/github/pgx-variants/data/pharmvar" +
                "-alleles-function.txt");
        File snpF = new File("/Users/joeri/github/pgx-variants/data/pharmvar" +
                "-4.1.4-snps.tsv");
        GenomeBuild gb = b37;
        File vcf = new File("\"/Users/joeri/Projects/GAVIN_Plus" +
                "/1000G_diag_FDR/exome/ALL.chr1to22plusXYMT" +
                ".phase3_shapeit2_mvncall_integrated_v5.20130502.genotypes" +
                ".snpEffNoIntergenicNoIntronic.exac.gonl.cadd.vcf.gz\"");
        File out = new File("/Users/joeri/github/pgx-variants/data/TMP.tsv");

        // load allele data
        LoadPgxAlleles lpa = new LoadPgxAlleles(allF);
        lpa.load();
        List<PgxAllele> pgxAll = lpa.retrieve();

        // load snp data
        LoadPgxSnps lps = new LoadPgxSnps(snpF);
        lps.load();
        List<PgxSnp> pgxSnp = lps.retrieve();

        // vcf
        BlockCompressedInputStream is = new BlockCompressedInputStream(vcf);
        VcfReader r = new VcfReader(is);
        VcfMeta vm = r.getVcfMeta();







    }
}
