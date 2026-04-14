#include <iostream>
#include <string>
#include <cctype>
#include <iomanip>

using namespace std;

// --- Calculate Melting Temperature (Tm) ---
int calculateMeltingTemp(string primer) {
    int aCount = 0, tCount = 0, gCount = 0, cCount = 0;
    for (char base : primer) {
        if (base == 'A') aCount++;
        else if (base == 'T') tCount++;
        else if (base == 'G') gCount++;
        else if (base == 'C') cCount++;
    }
    return (2 * (aCount + tCount)) + (4 * (gCount + cCount));
}

// --- Primer Validation ---
void validatePrimer(string primer) {
    int length = primer.length();
    cout << "\n[Primer Validation Report]\n";
    cout << "--------------------------\n";
    
    cout << "Length: " << length << " bases\n";
    if (length >= 18 && length <= 25) cout << "  [PASS] Good primer length\n";
    else cout << "  [WARN] Primer length not ideal (usually 18-25)\n";

    int gcCount = 0;
    for (char c : primer) {
        if (c == 'G' || c == 'C') gcCount++;
    }
    double gcContent = (double)gcCount / length * 100;
    
    cout << fixed << setprecision(1);
    cout << "\nGC Content: " << gcContent << "%\n";
    if (gcContent >= 40 && gcContent <= 60) cout << "  [PASS] GC content optimal\n";
    else cout << "  [WARN] GC content out of bounds (ideal 40-60%)\n";

    int tm = calculateMeltingTemp(primer);
    cout << "\nMelting Temp (Tm): " << tm << "C\n";
    if (tm >= 50 && tm <= 65) cout << "  [PASS] Melting Temperature optimal\n";
    else cout << "  [WARN] Melting Temperature out of bounds (ideal 50-65C)\n";
        
    cout << "--------------------------\n";
}

// --- Generate Complementary Primer ---
string generatePrimer(string target) {
    string primer = "";
    for (char base : target) {
        if (base == 'A') primer += 'T';
        else if (base == 'T') primer += 'A';
        else if (base == 'C') primer += 'G';
        else if (base == 'G') primer += 'C';
    }
    return primer;
}

// --- MAIN PROGRAM (WEB VERSION) ---
int main(int argc, char* argv[]) {
    // This expects the Python server to hand it the genome and target instantly
    if (argc < 3) {
        cout << ">>> ERROR: Missing Genome or Target Inputs. <<<\n";
        return 1;
    }

    string genome = argv[1];
    string targetSequence = argv[2];

    for (char &c : genome) c = toupper(c);
    for (char &c : targetSequence) c = toupper(c);

    cout << ">>> BIOINFORMATICS ENGINE INITIALIZED <<<\n\n";
    cout << "[STEP 1] Target Gene Extracted: " << targetSequence << "\n";
    
    string designedPrimer = generatePrimer(targetSequence);
    cout << "[STEP 2] Optimal Primer Generated: " << designedPrimer << "\n";

    validatePrimer(designedPrimer);

    cout << "\n[STEP 3] Executing PCR Annealing Search...\n";
    size_t foundPosition = genome.find(targetSequence);

    if (foundPosition != string::npos) {
        cout << "\n>>> SUCCESS: Primer annealed at position " << foundPosition << " <<<\n\n";
        
        cout << "Genome:  " << genome.substr(foundPosition, targetSequence.length()) << "\n";
        cout << "         ";
        for (size_t i = 0; i < targetSequence.length(); i++) cout << "|";
        cout << "\n";
        cout << "Primer:  " << designedPrimer << "\n";
    } else {
        cout << "\n>>> ERROR: Target sequence not found in genome. PCR Failed. <<<\n";
    }

    cout << "\n>>> PROCESS COMPLETE <<<\n";
    return 0;
}