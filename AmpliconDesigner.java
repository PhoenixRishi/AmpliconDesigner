import java.util.*;

public class AmpliconDesigner 
{
   
    //Finding the first Occurance
    public static int TargetPos(String genome,String target)    
    {
        int pos=genome.indexOf(target);
        if(pos==-1)
        {
            System.out.println(">>> ERROR: Target sequence not found in genome. <<<");
            return -1;
        }
        return pos;
    }

    public static String GenFwdPrimer(String genome,String target)
    {
        int pos=TargetPos(genome,target);
        int start =Math.max(0,pos-10);
        int end = Math.min(genome.length(),pos+10);
        return genome.substring(start,end);
    }

    public static String GenRevPrimer(String genome, String target)
    {
        int pos = TargetPos(genome, target);
        int end = pos+target.length();
        int start =Math.max(0,end-10);
        // First end is finding the end of the target sequence,
        //this endpos is finding the end of the reverse primer without exceeding the genome length
        int endpos=Math.min(genome.length(),end+10);
        return genome.substring(start,endpos);
    }

    public static String revcomplimentPrimer(String sequence)
    {
        StringBuilder compliment = new StringBuilder();
        for(char base :sequence.toCharArray())
    {
        if(base == 'A')
            compliment.append('T');
        else if(base == 'T')
            compliment.append('A');
        else if(base == 'G')
            compliment.append('C');
        else if(base == 'C')
            compliment.append('G');
    }
        return(compliment.reverse().toString());
    }

    public static boolean hairpin( String primer)
    {
        String first= primer.substring(0,4);
        String last=primer.substring(primer.length()-4);
        return (first.equals(revcomplimentPrimer(last)));
    }


    public static boolean dimercheck(String primer1,String primer2)
    {

        String end4= primer1.substring(primer1.length()-4);
        String revCompl4= revcomplimentPrimer(end4);
        return (primer2.contains(revCompl4));
    }

    
    //detecting how many possible primer matches can be done in the genome 
    public static int countmismatch(String primer,String subgenome)
    {
        int count=0;
        for(int i=0;i<primer.length();i++)
        {
            if(primer.charAt(i)!=subgenome.charAt(i))
                count++;
        }
        return count;
    }
    //sliding through the genome length and checking for any suitable matches for the primer
    //lesser the match count , better the specificity i.e more easy annealing.i.e ideal match = 1
    public static int Specificity(String genome,String primer)
    {
        int match=0;
        for(int i=0;i<=genome.length()-primer.length();i++)
        {
            String subgenome=genome.substring(i,i+primer.length());
            int miss=countmismatch(primer,subgenome);
            if(miss<=2)
                match++;
        }
        return match;
    }

    public static double GcContent(String primer)
    {
        int gcCount=0;
        for(int i = 0; i < primer.length(); i++)
            {
                char c = primer.charAt(i);
                if(c == 'G' || c == 'C')
                    gcCount++;
            }
        return ((double)gcCount / primer.length()) * 100;
    }
    
    public static double MelttempCalc(String primer)
    {
        int gcCount = 0;
        for(int i = 0; i < primer.length(); i++)
         {
                char c = primer.charAt(i);
                if(c == 'G' || c == 'C')
                     gcCount++;
            }
        return 64.9+(41*(gcCount-16.4)/primer.length());
    }

    public static void printReport(String genome,String target,int targetPos,String fwdPrimer,String revPrimer,double gcFwd,double gcRev,double FwdTemp,double RevTemp,boolean fwdHairpin,
                    boolean revHairpin,boolean selfDimerFwd,boolean selfDimerRev,boolean pairDimer,int fwdSpec,int revSpec,int ampliconLength)
    {

        String lenFwdStatus =(fwdPrimer.length()>=18 && fwdPrimer.length()<=25)? "PASS" : "WARN";
        String lenRevStatus =(revPrimer.length()>=18 && revPrimer.length()<=25)? "PASS" : "WARN";
        String gcFwdStatus =(gcFwd >= 40 && gcFwd <= 60)? "PASS" : "WARN";
        String gcRevStatus =(gcRev >= 40 && gcRev <= 60)? "PASS" : "WARN";
        String tempFwdStatus =(FwdTemp>=50 && FwdTemp<=65)? "PASS" : "WARN";
        String tempRevStatus =(RevTemp>=50 && RevTemp<=65)? "PASS" : "WARN";
        String specFwdStatus= (fwdSpec==1)?"PASS" : "WARN";
        String specRevStatus = (revSpec==1)?"PASS" : "WARN";
        
        System.out.println("\n==================================================");
        System.out.println("              AMPLICON DESIGN REPORT");
        System.out.println("==================================================");

        System.out.println("\n1. INPUT");
        System.out.println("--------------------------------------------------");

        System.out.println("Genome Length   : "+genome.length()+" bp");
        System.out.println("Target Region   : "+(targetPos+1)+" - "+(targetPos + target.length())); 
        System.out.println("Target Length   : "+target.length()+" bp");
        System.out.println("\nTarget Alignment");
        System.out.println("-----------------------------------");
        System.out.println(genome);
        for(int i = 0; i < targetPos; i++)
        {
            System.out.print(" ");}
        for(int i = 0; i < target.length(); i++){
            System.out.print("|");
        }
        System.out.println();
        for(int i = 0; i < targetPos; i++){
            System.out.print(" ");}
        System.out.println(target);


        System.out.println("\n2. PRIMER SEQUENCES");
        System.out.println("--------------------------------------------------");
        System.out.println("\nFORWARD PRIMER");
        System.out.println(fwdPrimer);
        System.out.println("\nREVERSE PRIMER");
        System.out.println(revPrimer);
        System.out.println("\n3. PRIMER METRICS");
        System.out.println("--------------------------------------------------");

        System.out.printf("%-25s %-15s %-15s%n", "","FORWARD","REVERSE");
        System.out.printf("%-20s %-10d %-10s %-10s%n","Length",fwdPrimer.length(),"[18-25]",lenFwdStatus);
        System.out.printf("%-20s %-10d %-10s %-10s%n","",revPrimer.length(),"[18-25]",lenRevStatus);
        System.out.printf("%-20s %-10.2f %-10s %-10s%n","GC Content",gcFwd,"[40-60]",gcFwdStatus);
        System.out.printf("%-20s %-10.2f %-10s %-10s%n","",gcRev,"[40-60]",gcRevStatus);
        System.out.printf("%-20s %-10.2f %-10s %-10s%n","Tm (APPROXIMATION)",FwdTemp,"[50-65]",tempFwdStatus);
        System.out.printf("%-20s %-10.2f %-10s %-10s%n","",RevTemp,"[50-65]",tempRevStatus);        
        
        
        System.out.println("\n4. QUALITY CHECKS");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-30s %-10s%n","Forward Hairpin",fwdHairpin ? "FAIL" : "PASS");
        System.out.printf("%-30s %-10s%n","Reverse Hairpin",revHairpin ? "FAIL" : "PASS");
        System.out.printf("%-30s %-10s%n","Forward Self Dimer",selfDimerFwd ? "FAIL" : "PASS");
        System.out.printf("%-30s %-10s%n","Reverse Self Dimer",selfDimerRev ? "FAIL" : "PASS");
        System.out.printf("%-30s %-10s%n","Primer Pair Dimer",pairDimer ? "FAIL" : "PASS");


        System.out.println("\n5. SPECIFICITY");
        System.out.println("--------------------------------------------------");
        System.out.println("Forward Primer Sites : " + fwdSpec+" (Expected 1) "+specFwdStatus);
        System.out.println("Reverse Primer Sites : " + revSpec+" (Expected 1) "+specRevStatus);


        System.out.println("\n6. AMPLICON");
        System.out.println("--------------------------------------------------");
        System.out.println("Amplicon Length      : "+ ampliconLength+ " bp");

        System.out.println("\n7. FINAL VERDICT");
        System.out.println("--------------------------------------------------");

        String finalStatus = "PASS";
        String reasons = "";

        // LENGTH CHECK
        if(lenFwdStatus.equals("WARN")|| lenRevStatus.equals("WARN"))
        {
            finalStatus = "WARNING";
            reasons += "Primer length outside recommended range\n";
        }
        if(gcFwdStatus.equals("WARN")|| gcRevStatus.equals("WARN"))
        {
            finalStatus="WARNING";
            reasons+="GC content outside recommended range\n";
        }
        //hair pin check and Dimer Check
        if(fwdHairpin || revHairpin){
            finalStatus="WARNING";
            reasons+="Hairpin structure detected\n";
        }

        if(selfDimerFwd || selfDimerRev || pairDimer)
        {
            finalStatus="WARNING";
            reasons+="Dimer formation detected\n";
        }

        if(fwdSpec==0)
        {
            finalStatus="WARNING";
            reasons+="Forward primer has no binding site\n";
        }
        else if(fwdSpec>1)
        {
            finalStatus="WARNING";
            reasons+="Forward primer has multiple binding sites\n"; 
        }
        if(revSpec==0)
        {
            finalStatus="WARNING";
            reasons+="Reverse primer has no binding site\n";
        }
        else if(revSpec>1)
        {
            finalStatus="WARNING";
            reasons+="Reverse primer has multiple binding sites\n";
        }
        if(FwdTemp < 50 || FwdTemp > 65)
        {
            finalStatus = "WARNING";
            reasons += "Forward primer Tm outside range\n";
        }

        if(RevTemp < 50 || RevTemp > 65)
        {
            finalStatus = "WARNING";
            reasons += "Reverse primer Tm outside range\n";
        }

        System.out.println("PCR Design Status : " + finalStatus);

        if(!reasons.isEmpty())
        {
            System.out.println("\nReasons:");
            System.out.println(reasons);
        }

        
        System.out.println("\n==================================================");
        System.out.println("               ANALYSIS COMPLETE");
        System.out.println("==================================================");
    }

    public static void main(String args[])
    {
        if(args.length<2)
        {
            System.out.println("ERROR: Missing Genome or Target");
            return;
        }
        //real inputs
        String genome = args[0].toUpperCase();
        String target = args[1].toUpperCase();
        
        //sample test
        // Scanner sc = new Scanner(System.in);
        // System.out.print("Enter Genome : ");
        // String genome = sc.nextLine().toUpperCase();

        // System.out.print("Enter Target : ");
        // String target = sc.nextLine().toUpperCase();
        
        
        int targetPos = TargetPos(genome,target);
        if(targetPos == -1)
        {
            return;
        }
     
        String fwdPrimer=GenFwdPrimer(genome,target);

        String revPrimer=revcomplimentPrimer(GenRevPrimer(genome,target));

        double gcFwd=GcContent(fwdPrimer);
        double gcRev= GcContent(revPrimer);
        double FwdTemp = MelttempCalc(fwdPrimer);
        double RevTemp= MelttempCalc(revPrimer);

        boolean fwdHairpin=hairpin(fwdPrimer);
        boolean revHairpin=hairpin(revPrimer);

        boolean selfDimerFwd=dimercheck(fwdPrimer,fwdPrimer);
        boolean selfDimerRev=dimercheck(revPrimer,revPrimer);

        boolean pairDimer= dimercheck(fwdPrimer,revPrimer);

        int fwdSpec = Specificity(genome,fwdPrimer);
        int revSpec = Specificity(genome,revcomplimentPrimer(revPrimer));

        int fwdStart =Math.max(0,targetPos-10);
        int revEnd=Math.min(genome.length(),targetPos + target.length() + 10);
        int ampliconLength =revEnd - fwdStart;

        printReport(genome,target,targetPos,fwdPrimer,revPrimer,gcFwd,gcRev,FwdTemp,RevTemp,fwdHairpin,
                    revHairpin,selfDimerFwd,selfDimerRev,pairDimer,fwdSpec,revSpec,ampliconLength);

    
    }
}