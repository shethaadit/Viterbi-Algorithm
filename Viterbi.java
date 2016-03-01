import java.io.*;

//CLASS HAVING MAIN FUNCTION
public class Viterbi 
{
	//MAIN FUNCTION
	public static void main(String[] args) throws IOException 
	{
		//CHECK FOR VALID 2 COMMAND LINE ARGUMENTS 
		if(args.length != 2)    
		{                       
			System.out.println("You should provide exactly two arguments: 1.Model File 2.Test File ");
		    System.exit(1);
		}
		
		//READING DATA FROM MODEL-FILE
		
		//MODEL-FILE AS FIRST ARGUMENT
		String modelFile = args[0];
		FileReader frModel = new FileReader(new File(modelFile));
		BufferedReader brModel = new BufferedReader(frModel);
		
		//STORES TOTAL NUMBER OF STATES IN MODEL FILE
		int noOfStates = Integer.parseInt(brModel.readLine());
		
		//INITIAL PI PROBABILITY FOR ALL STATES
		String[] initialPi = brModel.readLine().split(" ");
		
		//ARRAY TO STORE INITIAL PI PROBABILITIES
		float[] pi = new float[noOfStates];
		
		//ARRAY TO STORE STATE TRANSITION 
		String[] transition = brModel.readLine().split(" ");
		
		//TO STORE NUMBER OF OUTPUT SYMBOL FROM MODEL-FILE
		int noOfOutputSymbol = Integer.parseInt(brModel.readLine());
		
		//ARRAY TO STORE GIVEN OUTPUT CHARACTERS FROM MODEL-FILE
		String outputChar[] = brModel.readLine().split(" ");
		
		//READING DATA FROM TEST-FILE
		
		//TEST-FILE AS SECOND ARGUMENT
		String testFile = args[1];
		FileReader frTest = new FileReader(new File(testFile));
		BufferedReader brTest = new BufferedReader(frTest);
		        
		int i=0;
		for(String s : initialPi)
			//TAKING AND STORING VALUES OF PI PROBABILITIES
			pi[i++] = Float.parseFloat(s);
	
			//ARRAY TO STORE PROBABILITIES OF TRANSITION MATRIX
		    float[][] transMatrix = new float[noOfStates][noOfStates];
		        
		        int m = 0;
		        
		        //STORING PROBABILITIES OF TRANSITIONS
		        for(i = 0; i < noOfStates; i++)
		        {
		        	for(int j = 0; j < noOfStates; j++)
		        	{
		        		transMatrix[i][j] = Float.parseFloat(transition[m++]);
		        	}
		        }
		     
		        String[] tempProb = brModel.readLine().split(" ");
		        
		        //2-D ARRAY TO STORE OUTPUT CHARACTERS PROBABILITIES MATRIX
		        float[][] outputCharProb = new float[noOfStates][noOfOutputSymbol];
		        
		        m=0;
		        
		        //STORING OUTPUT CHARACTERS PROBABILITIES
		        for(i = 0; i < noOfStates; i++)
		        {
		        	for(int t = 0; t < noOfOutputSymbol; t++)
		        	{
		        		outputCharProb[i][t] = Float.parseFloat(tempProb[m++]);
		        	}
		        }
		        
		   String s = null;
		   
		   System.out.println("VITERBI OUTPUT");
		   System.out.println("______________");
		   System.out.println();
		   
		   float[] tempStateHold = new float[noOfStates];
	       float[] currentStateProb = new float[noOfStates];
	       int c = 0;
	       
		   
		   //TO READ GIVEN OUTPUT SYMBOLS
		   while((s = brTest.readLine())!= null)
		   {
			   int track = 0;
			   String lastState = s;
		        
		       //TO OMIT WHITE SPACES FROM OUTPUT SYMBOLS
			   String seqOfOpSymbol=s.replaceAll("\\s","");
		        
		        int op=0;
		        for(i = 0; i < outputChar.length; i++)
		        {
		        	if(outputChar[i].charAt(0) == seqOfOpSymbol.charAt(0))
		        	{
		        		op=i;
		        		break;
		        	}
		        }
		        
		        //ARRAY TO STORE THE VALUES OF STATE SEQUENCES GETTING FROM BACKTRACKING
		        int[][] backTrackingPath = new int[noOfStates][seqOfOpSymbol.length()];
		        
		        //VARIABLE TO STORE MAXIMUM VALUE OF GIVEN COLUMN
		        int a = 0;
		        
		        //MULTIPLYING INITIAL PROBABILITIES WITH OUTPUT CHARACTER SYMBOLS
		        for(i = 0; i < noOfStates; i++)
		        {
		        	currentStateProb[i] = pi[i]*outputCharProb[i][op];
		        	if(currentStateProb[i] >= a)
		        	{
		        		a=i;
		        	}
		        }
		        
		        //KEEPING TRACK OF ALL MAXIMUM VALUES FROM COLUMNS
		        for(i = 0; i < noOfStates; i++)
		        {
		        	backTrackingPath[i][0] = a;
		        }
		           
		        for(i = 1; i < seqOfOpSymbol.length(); i++)
		        {
		        	op=0;
		        	for(int w=0; w < outputChar.length; w++)
			        {
			        	if(outputChar[w].charAt(0) == seqOfOpSymbol.charAt(i))
			        	{
			        		op=w;
			        		break;
			        	}
			        }
		        	
		        	
		        	//TO CALCULATE TOTAL PROBABILITIES OF NEXT COLUMNS
		        	for(int j = 0; j < noOfStates; j++)
		        	{
		        		
		        		float columnMax=0;
		        		for(m = 0; m < noOfStates; m++)
		        		{
		        			//FORMULA TO FIND PROBABILITIES
		        			float temp = currentStateProb[m]*transMatrix[m][j]*outputCharProb[j][op];
		        			if(temp >= columnMax)
		        			{
		        				track = m;
		        				columnMax = temp;
		        			}
		        		}
		        		tempStateHold[j]= columnMax;
		        		
		        		//KEEPING TRACK FOR BACKTRACKING
		        		backTrackingPath[j][i] = track;
		        	}
		        	currentStateProb = tempStateHold.clone();
		        }
		        
		        float maxFinal = 0;
		        int finalState = 0;
		        
		        //FINDING MAX PROBABILITIES FROM ALL STATES FOR THAT COLUMN
		        for(i = 0; i < noOfStates; i++)
		        {
		        	if(currentStateProb[i] >= maxFinal)
		        	{
		        		maxFinal = currentStateProb[i];
		        		finalState = i;
		        	}
		        }
		        int currentState = finalState;
		        
		        //STRING BUILDER VARIABLE TO PRINT MOST LIKELY STATE SEQUENCE
		        StringBuilder stateSeq = new StringBuilder();
		        
		        
		        //TO PRINT STATES
		        for(i = seqOfOpSymbol.length()-1; i >= 0; i--)
		        {
		        	//FIRST STATE WILL PRINT
		        	if(i == 0)
		        	{
		        		stateSeq.insert(0, "S" + String.valueOf(currentState + 1));
		        	}
		        	else
		        	{
			        	stateSeq.insert(0, " - S"+ String.valueOf(currentState + 1));
			        	currentState = backTrackingPath[currentState][i];
			        }
		        }
		     
		       //PRINTING OUTPUT
		       System.out.println("Given output sequence-" + ++c +": ");
		       System.out.println(lastState);
		       System.out.println("Most likely state sequence for above output sequence:");
		       System.out.println(stateSeq);
		       System.out.println();
		       System.out.println("**************************************************************************");
		       System.out.println();
		  }        
	}
}
