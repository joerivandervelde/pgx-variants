package org.molgenis.pgx.tool;

import java.io.File;

public class Main {

    public static void main(String args[]) throws Exception {
        if(args.length != 5)
        {
            System.out.println("Please supply these 5 arguments:");
            System.out.println("- File location of 'pharmvar-4.1.4-snps.tsv' (in pgx-variants/data/)'");
            System.out.println("- File location of 'pv-4.1.4-pgkb28-03-2020-alleles.tsv' (in pgx-variants/data/)'");
            System.out.println("- If you use genome build 37 or 38 (either 'b37' or 'b38')");
            System.out.println("- File location of a GZipped VCF to be analyzed (ending in '.vcf.gz')");
            System.out.println("- Output file location. May not exist yet.");
            System.exit(0);
        }

        File snpF = new File(args[0]);
        if(!snpF.exists())
        {
            System.out.println("Input SNP file not found at " + snpF.getAbsolutePath() + ". Suggesting to supply 'pharmvar-4.1.4-snps.tsv' (located in pgx-variants/data/).");
            System.exit(0);
        }

        File alleleF = new File(args[1]);
        if(!alleleF.exists())
        {
            System.out.println("Input alleles file not found at " + snpF.getAbsolutePath() + ". Suggesting to supply 'pharmvar-alleles-function.tsv' (located in pgx-variants/data/).");
            System.exit(0);
        }

        String gbS = args[2];
        if(!gbS.equals("b37") && !gbS.equals("b38"))
        {
            System.out.println("Genome build must be either 'b37' or 'b38' instead of "+ gbS+".");
            System.exit(0);
        }
        GenomeBuild gb = GenomeBuild.valueOf(gbS);

        File vcfInputF = new File(args[3]);
        if(!vcfInputF.getName().endsWith(".vcf.gz"))
        {
            System.out.println("Input GZipped VCF file name '" + vcfInputF.getName() + "' does not end in '.vcf.gz'. Are you sure this is a valid input?");
            System.exit(0);
        }
        if(!vcfInputF.exists())
        {
            System.out.println("Input GZipped VCF file not found at " + vcfInputF.getAbsolutePath()+".");
            System.exit(0);
        }

        File outputF = new File(args[4]);
        if(outputF.exists())
        {
            System.out.println("Output file already exists at " + outputF.getAbsolutePath()+". Please delete it first, or supply a different output file name.");
            System.exit(0);
        }

        System.out.println("Arguments OK. Starting...");
        long start = System.nanoTime();

        PGxTool pgxt = new PGxTool(snpF, alleleF, gb, vcfInputF, outputF);
        pgxt.run();

        System.out.println("...completed in " + ((System.nanoTime()-start)/1000000)+"ms.");

    }
}
