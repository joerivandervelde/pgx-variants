package org.molgenis.pgx.parser;

import java.io.*;
import java.util.Scanner;

public class PharmVarParser {

    File phvdir;
    File outfile;
    BufferedWriter bw;

    /**
     * Constructor
     * @param phvdir
     * @param outfile
     * @throws IOException
     */
    public PharmVarParser(File phvdir, File outfile) throws IOException {
        this.phvdir = phvdir;
        this.outfile = outfile;
        FileWriter fw = new FileWriter(outfile);
        this.bw = new BufferedWriter(fw);
    }

    /**
     * Run parser on dir
     * @throws Exception
     */
    public void run() throws Exception {

        // GRCh37	CYP26A1	NC_000010.10	CYP26A1*2.001	PV00381	10
        // 94834638	rs61735552	C	A

        bw.write("#GenomeBuild\tGene\tChrId\tAllele\tPharmVarId\tChr\tPos" +
                "\tSnpId\tRef\tAlt\n");

        for(File geneDir : phvdir.listFiles())
        {
            if(geneDir.getName().equalsIgnoreCase(".DS_Store"))
            {
                continue;
            }

            System.out.println(geneDir);


            for(File genbuildDir : geneDir.listFiles())
            {
                if(!genbuildDir.getName().startsWith("GRCh")){
                    continue;
                }
                System.out.println("\t" + genbuildDir);

                for(File vcf : genbuildDir.listFiles())
                {
                    if(!vcf.getName().endsWith(".vcf")){
                        continue;
                    }

                    String build = genbuildDir.getName();
                    String description = null;

                    Scanner s = new Scanner(vcf);
                    while(s.hasNextLine())
                    {
                        String nextLine = s.nextLine();
                        if(nextLine.startsWith("##description")){
                            String substrDesc = nextLine.substring(nextLine.indexOf("\"")).replace("\"","");
                            String[] descSplit = substrDesc.split(" ", -1);
                            if(descSplit.length != 4)
                            {
                                throw new Exception("Description not 4 elements: " + nextLine);
                            }
                            description = descSplit[0] + "\t" + descSplit[1] +
                                    "\t" + descSplit[2] + "\t" + descSplit[3];
                            continue;
                        }

                        if(!nextLine.startsWith("#")){
                            String[] varSplit = nextLine.split("\t", -1);
                            bw.write(build + "\t" + description +
                                    "\t" + varSplit[0].replace("chr","") + "\t" + varSplit[1] + "\t" + varSplit[2] + "\t" + varSplit[3] + "\t" + varSplit[4] + "\n");
                            continue;
                        }
                    }
                }
            }

        }
        bw.flush();
        bw.close();
    }

}
