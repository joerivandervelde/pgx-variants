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

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.molgenis.pgx.tool.GenomeBuild.b37;
import static org.molgenis.pgx.tool.GenomeBuild.b38;

public class PGxTool {

    private final static String HETZYG = " (1 copy)";
    private final static String HOMZYG = " (2 copies)";

    private File snpF;
    private File alleleF;
    private GenomeBuild gb;
    private File vcfInput;
    private File outputF;

    /**
     * The one and only constructor for this class.
     * @param snpF
     * @param alleleF
     * @param gb
     * @param vcfInput
     * @param outputF
     * @throws IOException
     */
    public PGxTool(File snpF, File alleleF, GenomeBuild gb, File vcfInput,
                   File outputF) throws IOException {
        this.snpF = snpF;
        this.alleleF = alleleF;
        this.gb = gb;
        this.vcfInput = vcfInput;
        this.outputF = outputF;
    }

    /**
     * Run the actual PGx tool.
     * @throws IOException
     */
    public void run() throws IOException {

        // load snp data
        LoadPgxSnps lps = new LoadPgxSnps(snpF);
        lps.load();
        List<PgxSnp> pgxSnp = lps.retrieve();

        // load allele data
        LoadPgxAlleles lpa = new LoadPgxAlleles(alleleF);
        lpa.load();
        List<PgxAllele> pgxAll = lpa.retrieve();

        System.out.println("Loaded " + pgxSnp.size() + " SNPs and " + pgxAll.size() + " allele-functions.");

        // make hashmap of alleles with functional annotation
        Map<String, PgxAllele> pgxAllMap = new HashMap<String, PgxAllele>();
        for(PgxAllele pgxa : pgxAll)
        {
            pgxAllMap.put(pgxa.allele, pgxa);
        }

        // select only snps that have exact match with alleles-function
        // to 'exclude' subtypes that add no further information
        // also, select for genome build here
        Map<String, List<PgxSnp>> pgxAllToSnp = new HashMap<String, List<PgxSnp>>();
        int snpsForTyping = 0;
        for(PgxSnp pgxs : pgxSnp)
        {
            // allele for this SNP is exactly present in allele-function
            if(pgxAllMap.containsKey(pgxs.Allele) && ((gb == b37 && pgxs.GenomeBuild.equals("GRCh37")) || (gb == b38 && pgxs.GenomeBuild.equals("GRCh38"))))
            {
                // add empty list if this is the first SNP to be added
                if(!pgxAllToSnp.containsKey(pgxs.Allele))
                {
                    pgxAllToSnp.put(pgxs.Allele, new ArrayList<PgxSnp>());
                }
                pgxAllToSnp.get(pgxs.Allele).add(pgxs);
                snpsForTyping++;
            }
        }

        System.out.println("Going to type on " + snpsForTyping + " SNPs for " + pgxAllToSnp.size() + " alleles.");

        // alleles without match
        StringBuffer sb = new StringBuffer();
        for(PgxAllele pgxa : pgxAll)
        {
            if(!pgxAllToSnp.containsKey(pgxa.allele)){
                sb.append(pgxa.allele + ", ");
            }
        }
        sb.delete(sb.length()-2, sb.length());
        System.out.println("No match for: " + sb.toString() + ". These should" +
                " be hybrid genes or full gene deletion haplotypes. Please check.");

        // init vcf and tabix
        TabixReader t = new TabixReader(vcfInput.getAbsolutePath());
        BlockCompressedInputStream is = new BlockCompressedInputStream(vcfInput);
        VcfReader r = new VcfReader(is);
        VcfMeta vm = r.getVcfMeta();

        // init output objects
        Map<String, Set<String>> sampleToPgx = new HashMap<String, Set<String>>();
        List<String> sampleNames = new ArrayList<String>();
        for(String sample: vm.getSampleNames()){
            sampleToPgx.put(sample, new HashSet<String>());
            sampleNames.add(sample);
        }

        // FIXME TMP!!
        FileWriter fw2 = new FileWriter(new File("/Users/joeri/github/pgx-variants/data/variants.vcf"));
        BufferedWriter bw2 = new BufferedWriter(fw2);

        for(String allele : pgxAllToSnp.keySet())
        {
            // for this allele, how many heterozyg SNPs observed for each sample
            Map<String, AtomicInteger> sampleHetSnpCt = new HashMap<String, AtomicInteger>();
            Map<String, AtomicInteger> sampleHomSnpCt = new HashMap<String, AtomicInteger>();

            for(PgxSnp pgxs : pgxAllToSnp.get(allele))
            {
                String tabixQ = pgxs.Chr + ":" + pgxs.Pos + "-" + pgxs.Pos;

                TabixReader.Iterator tri = t.query(tabixQ);
                String nextVcfLine;
                VcfRecord vr = null;
                while((nextVcfLine = tri.next()) != null)
                {
                    String[] split = nextVcfLine.split("\t", -1);
                    if(!split[1].equals(pgxs.Pos))
                    {
                        continue;
                    }
                    if(!split[3].equals(pgxs.Ref))
                    {
                        System.out.println("Ref allele did not match for: " + pgxs.Chr +
                                ":" + pgxs.Pos + ". Expected " + pgxs.Ref +
                                ", found: " + split[3]);
                        continue;
                    }
                    vr = new VcfRecord(vm, nextVcfLine.split("\t", -1));
                }

                if(vr == null)
                {
                    System.out.println("Variant not present: " + pgxs.Chr +":" + pgxs.Pos);
                    continue;
                }

                bw2.write(vr.toString() + "\n"); //FIXME TMP

                // not necessary since VCF-IO gives us the alleles as a string
                // however - checking here saves time if allele is not present!
                boolean altFound = false;
                sb = new StringBuffer();
                for(int i = 0; i < vr.getAlternateAlleles().size(); i ++)
                {
                    sb.append(vr.getAlternateAlleles().get(i).getAlleleAsString() + " ");
                    if(pgxs.Alt.equalsIgnoreCase(vr.getAlternateAlleles().get(i).getAlleleAsString()))
                    {
                        altFound = true;
                        break;
                    }
                }
                if(!altFound)
                {
                    System.out.println("Alt allele not in VCF: " + pgxs.Chr +
                            ":" + pgxs.Pos + ". Expected " + pgxs.Alt +
                            ", found: " + sb.toString());
                    continue;
                }

                // now that we have the correct variant in terms of chr, pos,
                // ref, and at least 1 matching alt, we can match the genotypes
                //TODO verify that sample name order is guarenteed
                Iterator<VcfSample> vi = vr.getSamples().iterator();
                VcfSample nextSample;
                int sampleIndex = 0;
                while(vi.hasNext())
                {
                    int altCount = 0;
                    for(Allele a: vi.next().getAlleles())
                    {
                        if(a.getAlleleAsString().equalsIgnoreCase(pgxs.Alt))
                        {
                            altCount++;
                        }
                    }

                    // TODO also take phasing into account
                    // count number of SNPs typing for this allele
                    if(altCount == 1)
                    {
                        if(!sampleHetSnpCt.containsKey(sampleNames.get(sampleIndex))){
                            sampleHetSnpCt.put(sampleNames.get(sampleIndex), new AtomicInteger(0));
                        }
                        sampleHetSnpCt.get(sampleNames.get(sampleIndex)).incrementAndGet();
                    }
                    else if(altCount == 2)
                    {
                        if(!sampleHomSnpCt.containsKey(sampleNames.get(sampleIndex))){
                            sampleHomSnpCt.put(sampleNames.get(sampleIndex), new AtomicInteger(0));
                        }
                        sampleHomSnpCt.get(sampleNames.get(sampleIndex)).incrementAndGet();
                    }
                    sampleIndex++;
                }
            }

            for(String sample : sampleHetSnpCt.keySet())
            {
                // if all SNPs that type this allele have been observed
                // heterozygously, it counts as heterozygous allele
                if(sampleHetSnpCt.get(sample).intValue() ==
                        (pgxAllToSnp.get(allele).size()))
                {
                    sampleToPgx.get(sample).add(allele + HETZYG);
                }
            }
            for(String sample : sampleHomSnpCt.keySet())
            {
                // if all SNPs that type this allele have been observed
                // homozygously, it counts as homozygously allele
                if(sampleHomSnpCt.get(sample).intValue() ==
                        (pgxAllToSnp.get(allele).size()))
                {
                    sampleToPgx.get(sample).add(allele + HOMZYG);
                }
            }
        }

        // create output file
        FileWriter fw = new FileWriter(outputF);
        BufferedWriter bw = new BufferedWriter(fw);

        // print results
        for(String sample : sampleNames) {
            if (!sampleToPgx.containsKey(sample) || sampleToPgx.get(sample).size() == 0) {
                bw.write(sample +
                        " has not been positively genotyped for any pgx alleles." + "\n");
                continue;
            }

            sb = new StringBuffer();
            sb.append(sample +
                    " has been positively genotyped for pgx alleles: ");
            for (String pgx : sampleToPgx.get(sample)) {
                sb.append(pgx + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(". ");
            for (String pgx : sampleToPgx.get(sample)) {
                String pgx_no_gt = pgx.replace(HETZYG, "").replace(HOMZYG, "");
                sb.append(pgx_no_gt + " has " +
                        pgxAllMap.get(pgx_no_gt).function + " compared to " +
                        "wild-type " + pgxAllMap.get(pgx_no_gt).gene + " to " +
                        "metabolize " +
                        pgxAllMap.get(pgx_no_gt).molecule + ". ");
            }
            bw.write(sb.toString() + "\n");
        }
        bw.flush();
        bw.close();

        //FIXME TMP
        bw2.flush();bw2.close();
    }
}
