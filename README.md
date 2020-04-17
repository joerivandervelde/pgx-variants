# pgx-variants
A tool to extract pharmacogenomic associations from next-generation sequencing data. Currently, this is a proof-of-concept. Use only for demonstrative purposes.

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

## To do
- Unit and integration testing
- Proper dependency management
- Proper cmdline option parsing
- Consider phasing when genotyping
- Check how to handle allele dosage
- Check how to use the annotations
- Verify that sample order is guaranteed
