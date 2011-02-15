package woko.hbcompass.util;

import java.io.*;
import java.util.*;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.WhitespaceAnalyzer;

public class TagCloud {

	public boolean option_skip_three = false;
	public boolean option_skip_numbers = false;
	public boolean option_dashes = false;
	public boolean option_period = false;
	public boolean option_include = false;

	public TagCloud() {}


	/**
	 * Rules file is a comma sep list of junk terms and deonminators.
	 * These terms will be broken down into a list and used to reject items within a cloud.
	 * Following options are supported, and must be specified on a individual line
	 * 		-numbers - ignore numbers
	 * 		-smallwords - skips words with three or less chars
	 * 		-dashes - ignore terms with dashes
	 *  	# - lines starting with # are ignored.
	 */
	private List<String> readRulesFile(String filename) throws IOException {

		if (filename==null)
			return null;

		List<String> rulesList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		String line = null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#") || line.trim().length() == 0) {
				continue;
			} else if (line.startsWith("-numbers")) {
				option_skip_numbers = true;
				continue;
			} else if (line.startsWith("-smallwords")) {
				option_skip_three = true;
				continue;
			} else if (line.startsWith("-dashes")) {
				option_dashes = true;
				continue;
			} else if (line.startsWith("-period")) {
				option_period = true;
				continue;
			} else if (line.startsWith("-include")) {
				option_include = true;
				continue;
			}
			StringTokenizer st = new StringTokenizer(line, ",");
			while (st.hasMoreTokens()) {
				rulesList.add(st.nextToken().trim().replace("\\s+", " ").toLowerCase());
			}
		}

		reader.close();
		return rulesList;
	}


	/**
	 * Get the list of terms to be used in the cloud, the list is randomized before returned.
	 *
	 * @param index   		index directory
	 * @param cloudSize    	top N terms to return
	 * @return ranomzed 	list of top N terms.
	 */
	public List<TermInfo> buildCloud(String index, String junkFile, int cloudSize, int boost, int minFreq) throws Exception 
	{

		if (index == null)
			return null;

		List<String> junkWords = readRulesFile(junkFile);

		IndexReader reader = IndexReader.open(index);

		TermEnum terms = reader.terms();

		List<TermInfo> l_terms = new ArrayList<TermInfo>();

		while (terms.next()) {
			String field = terms.term().field();
			if (!"ngram".equals(field)) continue;

			int freq = terms.docFreq();
			if (minFreq>0 && freq<minFreq) continue;

			String text = terms.term().text().trim();

			if (junkWords != null)
			{
				String text2 = text.replace("'", " ");
				text2 = text2.replace("\\s+", " ");
				text2 = text2.trim().toLowerCase();

				// If the n-gram is a junkword => skip
				if (junkWords.contains(text2)) continue;

				String aText2[] = text2.split(" ");

				// If first or last term of the n-gram are junkwords => skip
				if (junkWords.contains(aText2[0]) || junkWords.contains(aText2[aText2.length-1])) continue;

				// If two successive terms of the n-gram are junkwords => skip
				for (int i=0; i<aText2.length-2; i++)
					if (junkWords.contains(aText2[i]) && junkWords.contains(aText2[i+1])) continue;
			}

			if (option_skip_three && text.length() <= 3) continue;
			if (option_skip_numbers && isNumber(text)) continue;
			if (option_skip_numbers)
			{
				String tmp = text.replace(" ", "");
				if (isNumber(tmp)) continue;
			}
			if (option_dashes && text.indexOf("-") > 0) continue;
			if (option_period && text.indexOf(".") > 0) continue;

			if (terms.term().text().indexOf(" ") > 0)
				freq = freq * boost;

			TermInfo termInfo = new TermInfo(terms.term(),freq);
			if (termInfo==null)
			{
				continue;
			}
			l_terms.add(termInfo);
		}
		terms.close();
		reader.close();

		Collections.sort(l_terms);
		
		if (!option_include) 
			return l_terms;

		ArrayList<TermInfo> temp = new ArrayList<TermInfo>(l_terms.size());
		for (int ndx=0; ndx<l_terms.size(); ndx++)
		{
			boolean bSkip = false;

			String strTerm = l_terms.get(l_terms.size()-ndx-1).term.text();

			for (int ndx2=0; ndx2<temp.size(); ndx2++)
			{
				String strTemp = temp.get(ndx2).term.text();
				if (strTemp.indexOf(strTerm)!=-1)
				{
					bSkip = true;
					break;
				}
				else
				{
					if (strTerm.indexOf(strTemp)!=-1)
					{
						temp.remove(ndx2);
						ndx2--;
					}
				}
			}
			if (!bSkip)
			{
				temp.add(l_terms.get(l_terms.size()-ndx-1));
			}
		}

		return temp;
	}

	private String buildTempIndex(String inputFile, int maxTerm)
	{
		try
		{
			// Créer un index temporaire avec un champ contenant les n-grams
			File tempDir = createTempDirectory("tmp");

			FSDirectory fsd = FSDirectory.getDirectory(tempDir.getPath());
			IndexWriter writer = null;

			Analyzer analyzer = new WhitespaceAnalyzer();

			writer = new IndexWriter(fsd, false, analyzer, true);

			//writer.setMergeFactor(100);
			writer.setRAMBufferSizeMB(32);


			BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile)));
			String line = null;

			//read each line of text file
			while ((line = reader.readLine()) != null) 
			{
				line = line.replaceAll("[;:,\\(\\)\\[\\]\\{\\}]", ".");
				line = line.replaceAll("\\s+-", ".");
				line = line.replaceAll("-\\s+", ".");

				String[] aItems = line.split("\\.");

				for (int i=0; i<aItems.length; i++)
				{
					StringReader nGramSR = new StringReader(aItems[i].toLowerCase().trim());
					NGramAnalyzerWrapper ngramAnalyzer = new NGramAnalyzerWrapper(analyzer, maxTerm);

					Document doc = new Document();
					doc.add(new Field("ngram", ngramAnalyzer.tokenStream("ngram", nGramSR)));
					writer.addDocument(doc, analyzer);
				}
			}

			writer.close();
			fsd.close();

			return tempDir.getPath();


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}    


	public String getCloud(String inputFile, String junkFile, int cloudSize, int boost, int minFreq, int maxTerm) 
	{
		try 
		{
			String tempIndex = null;
			int count=0;

			tempIndex = buildTempIndex(inputFile, maxTerm);    
			if (tempIndex==null)
				return null;

			List<TermInfo> terms = buildCloud(tempIndex, junkFile, cloudSize, boost, minFreq);

			String output = "";
			for (TermInfo ti : terms) {
				if (cloudSize>0 && count>=cloudSize) break;
				output += ti.term.text().replace("_", " ") + "|" + String.valueOf(ti.docFreq) + "\n";
				count++;
			}

			return output;            
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * Vérifie qu'une chaîne de caractères représente un nombre
	 */
	public static boolean isNumber(final String s) {
		for (char c : s.toCharArray())
			if (!Character.isDigit(c))
				return false;
		return true;
	}   

	/**
	 * Créer un répertoire temporaire
	 */
	public static File createTempDirectory(String prefix) throws IOException
	{
		File tempFile = File.createTempFile(prefix, "");
		if (!tempFile.delete())
			throw new IOException();
		if (!tempFile.mkdir())
			throw new IOException();
		return tempFile;
	}

	/**
	 * Ecrit des données dans un fichier 
	 */
	public static void dumpToFile(String fileName, String buffer, boolean append, String encoding)
	{
		try {

			if (!"".equals(encoding))
			{
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)),encoding));
				out.write(buffer);
				out.close();
			}
			else
			{
				BufferedWriter writer = null;
				writer = new BufferedWriter(new FileWriter(fileName, append));
				writer.write(buffer);
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Main
	 */
	public static void main(String[] args) throws Exception 
	{
		if (args.length == 0) 
		{
			Usage();
			System.exit(0);  
		}

		int count = 1000;
		int boost = 1;
		int minFreq = 2;
		int maxTerm = 3;
		String input = null;
		String rules = null;
		String output = null;

		for ( int x = 0; x < args.length; x ++ ) {
			if ( args[x].equals( "-input" ) )
				input = args[++x];
			else if ( args[x].equals( "-output" ) )
				output = args[++x];
			else if ( args[x].equals( "-rules" ) )
				rules = args[++x];
			else if ( args[x].equals( "-count" ) )
				count = Integer.parseInt( args[++x] );
			else if ( args[x].equals( "-boost" ) )
				boost = Integer.parseInt( args[++x] );
			else if ( args[x].equals( "-minfreq" ) )
				minFreq = Integer.parseInt( args[++x] );
			else if ( args[x].equals( "-maxterm" ) )
				maxTerm = Integer.parseInt( args[++x] );
		}

		if (input==null || output==null || rules==null)
		{
			Usage();
			System.exit(0);  
		}

		// Construit les données du nuage
		TagCloud tc = new TagCloud();
		String cloudData = tc.getCloud(input, rules, count, boost, minFreq, maxTerm);

		// Ecrit les données du nuage
		dumpToFile(output, cloudData, false, "UTF8");    	       	 
	}

	private static void Usage()
	{
		System.out.println("Usage: org.apache.demo.TagCloud -input [inputfile] -output [outputfile] -rules [rulesfile] -count [count] -boost [boost] -minfreq [minfreq] -maxterm [maxterm]");
		System.out.println("            input - input file with data to be clouded");
		System.out.println("            output - output file with cloud data");
		System.out.println("            rules - rules file");
		System.out.println("            count - max number of items within the cloud (default = 1000)");  	 
		System.out.println("            boost - boost value for multi-terms tags (default = 1)");  	 
		System.out.println("            minfreq - minimum frequence for a ngram (default = 2)");  	 
		System.out.println("            maxterm - maximum number of terms in expressions in the cloud (default = 3)");  	 
	}

	/**
	 * Inner class to hold the contents of the cloud terms.
	 */
	public class TermInfo implements Comparable
	{
		TermInfo(Term t, double df) {
			term = t;
			docFreq = df;
		}

		public int compareTo (Object o) {
			TermInfo otherTI = (TermInfo) o;

			if (docFreq > otherTI.docFreq)
				return 1;

			if (docFreq < otherTI.docFreq)
				return -1;

			return 0;
		}
		public double docFreq;
		public Term term;
	}
}
