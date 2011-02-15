package woko.hbcompass.util;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * An NGramAnalyzerWrapper wraps an NGramFilter around another analyzer.
 * 
 * @author Sebastian Kirsch <skirsch@sebastian-kirsch.org>
 *
 */
public class NGramAnalyzerWrapper extends Analyzer {

	protected Analyzer defaultAnalyzer;
	protected int nGramSize = 2;
	protected boolean outputUnigrams = true;
	/**
	 * 
	 */
	public NGramAnalyzerWrapper(Analyzer defaultAnalyzer) {
		super();
		this.defaultAnalyzer = defaultAnalyzer;
	}

	public NGramAnalyzerWrapper(Analyzer defaultAnalyzer, int nGramSize) {
		this(defaultAnalyzer);
		this.nGramSize = nGramSize;
	}

	public NGramAnalyzerWrapper() {
		super();
		this.defaultAnalyzer = new StandardAnalyzer();
	}

	public NGramAnalyzerWrapper(int nGramSize) {
		this();
		this.nGramSize = nGramSize;
	}

	/**
	 * Set the maximum size of output n-grams (default: 2)
	 * 
	 * @param n n-gram size
	 * @return receiver
	 */
	public NGramAnalyzerWrapper setNGramSize(int n) {
		this.nGramSize = n;
		return this;
	}

	/**
	 * Shall the filter pass the original tokens (the "unigrams") to the output 
	 * stream? (default: true)
	 * 
	 * @param flag
	 * @return receiver
	 */
	public NGramAnalyzerWrapper setOutputUnigrams(boolean flag) {
		this.outputUnigrams = flag;
		return this;
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new NGramFilter(defaultAnalyzer.tokenStream(fieldName, reader))
		.setNGramSize(nGramSize)
		.setOutputUnigrams(outputUnigrams);
	}
}
