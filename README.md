# AmpliconDesigner

A simple bioinformatics tool for designing PCR amplicons by searching a target sequence within a genome sequence.
🔗 Live Demo: https://amplicondesigner.onrender.com/

## Features

- Generates Forward and Reverse Primers
- Calculates GC Content
- Estimates Melting Temperature
- Performs Hairpin Checks
- Performs Dimer Checks
- Estimates Primer Specificity
- Calculates Amplicon Length
- Generates a Structured PCR Design Report

## Tech Stack

- Java (Core Analysis Engine)
- Python Flask (Backend)
- HTML/CSS (Frontend)
- Docker
- Render

## How It Works

1. Enter a genome sequence.
2. Enter a target sequence.
3. The Java engine:
   - Checks if the target region is present
   - Generates primers
   - Calculates primer metrics
   - Performs quality checks
   - Generates an amplicon report
4. Results are displayed through the Flask web interface.

## Notes

- Hairpin and dimer checks use simplified heuristics.
- Melting temperature is an approximation.
- Intended for educational and learning purposes.
- This tool is not a replacement for professional primer design software.

## Future Scope

- More accurate melting temperature calculations
- Integration with GenBank accession records

## Author

Rishi Spandan
