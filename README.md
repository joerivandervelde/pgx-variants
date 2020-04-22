# pgx-variants
A tool to extract pharmacogenomic associations from next-generation 
sequencing data. Currently, this is a proof-of-concept. Use only for 
demonstrative purposes. We thank the main data source of pgx-variants: 
Pharmacogene Variation Consortium (PharmVar) at www.PharmVar.org (Gaedigk et 
al. 2018, CPT 103:399; Gaedigk et al. 2019, CPT 105:29).

## How to compile
Compile using Java 8+ with these dependencies:
```
com.github.samtools:htsjdk:2.21.3
org.molgenis:vcf-io:1.1.1
```

## Quick start

Download the JAR, required data, a demo file, and run.
```
wget https://github.com/joerivandervelde/pgx-variants/releases/download/v0.0.1/pgx-variants-0.0.1.jar
wget https://raw.githubusercontent.com/joerivandervelde/pgx-variants/master/data/pharmvar-4.1.4-snps.tsv
wget https://raw.githubusercontent.com/joerivandervelde/pgx-variants/master/data/pv-4.1.4-pgkb28-03-2020-alleles.tsv
wget https://github.com/joerivandervelde/pgx-variants/raw/master/data/b37demo.vcf.gz
wget https://github.com/joerivandervelde/pgx-variants/raw/master/data/b37demo.vcf.gz.tbi
java -jar pgx-variants-0.0.1.jar pharmvar-4.1.4-snps.tsv \
pv-4.1.4-pgkb28-03-2020-alleles.tsv b37 b37demo.vcf.gz b37demo_out.tsv
```

Typical usage:
```
java -jar pgx-variants.jar [snps] [alleles] [genomebuild] [input vcf] [output]
```

Example usage:
```
java -jar pgx-variants-0.0.1.jar \
path/to/data/pharmvar-4.1.4-snps.tsv \
path/to/data/pv-4.1.4-pgkb28-03-2020-alleles.tsv \
b37 \
patho/to/my/input.vcf.gz \
patho/to/my/output.tsv
```

Example output:
```
HG00404 has been positively genotyped for pgx alleles: NUDT15*3.001 (1 copy), CYP2D6*10 (2 copies). NUDT15*3.001 has severely decreased function compared to wild-type NUDT15 to metabolize mercaptopurine, azathioprine. CYP2D6*10 has decreased function compared to wild-type CYP2D6 to metabolize paroxetine, dextromethorphan, aripiprazole, haloperidol, levomepromazine, quetiapine, risperidone, n-desmethyltamoxifen, nevirapine, ondansetron, bufuralol, methadone, debrisoquine, venlafaxine, methylphenidate, metoprolol, olanzapine, sparteine, citalopram, escitalopram, imipramine, tamoxifen, mianserin, timolol, donepezil, nortriptyline, vortioxetine, hydrocodone, tropisetron, maprotiline, opipramol, tolterodine, fluvoxamine, oxycodone, pimozide, aspirin, thioridazine, antipsychotics, codeine, amitriptyline, antidepressants, clomipramine, desipramine, doxepin, trimipramine, zuclopenthixol, fluoxetine, sertraline, berberine, coptisine, flecainide, propafenone, galantamine, atomoxetine, carvedilol, mirtazapine, dolasetron, granisetron, lovastatin, iloperidone, tramadol, tolperisone, propranolol, dapoxetine, gefitinib.
NA19731 has been positively genotyped for pgx alleles: CYP2B6*9 (2 copies). CYP2B6*9 has decreased function compared to wild-type CYP2B6 to metabolize mirtazapine, efavirenz, propofol, nicotine, cotinine, nevirapine, bupropion, 3,4-methylenedioxymethamphetamine, antivirals for treatment of HIV infections, drugs for treatment of tuberculosis, clopidogrel, methadone, ethambutol, isoniazid, pyrazinamide, rifampin, imatinib, ketamine, cyclophosphamide, doxorubicin, mitotane. 
HG03397 has been positively genotyped for pgx alleles: CYP3A43*2.001 (2 copies), CYP3A43*2.002 (2 copies). CYP3A43*2.001 has no function compared to wild-type CYP3A43 to metabolize olanzapine, ticagrelor. CYP3A43*2.002 has no function compared to wild-type CYP3A43 to metabolize olanzapine, ticagrelor. 
NA12273 has been positively genotyped for pgx alleles: CYP2C9*2 (1 copy), CYP2A13*7.001 (1 copy). CYP2C9*2 has decreased function compared to wild-type CYP2C9 to metabolize warfarin, acenocoumarol, phenytoin, sulfonamides urea derivatives, lornoxicam, ibuprofen, diclofenac, tenoxicam, fluvastatin, phenprocoumon, irbesartan, tolbutamide, valproic acid, celecoxib, busulfan, meloxicam, doxepin, simvastatin, piroxicam, losartan, clopidogrel, trimipramine, anti-inflammatory agents non-steroids, ethambutol, isoniazid, pyrazinamide, rifampin, zafirlukast, flurbiprofen, olanzapine, hormonal contraceptives for systemic use. CYP2A13*7.001 has no function compared to wild-type CYP2A13 to metabolize nicotine. 
```

## To do
- Unit and integration testing
- Proper dependency management
- Report missing and negative markers
- Proper cmdline option parsing
- Consider phasing when genotyping
- Check how to handle allele dosage
- Check how to use the annotations
- Verify that sample order is guaranteed
