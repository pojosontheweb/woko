/*
 * Created on Jun 8, 2005
 *
 */
package woko.hbcompass.util;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * <p>An NGramFilter constructs n-grams from a token stream, that is, combinations
 * of tokens that are indexed as one token.
 * 
 * <p>For example, the sentence "please divide this sentence into ngrams" would be
 * tokenized into the tokens "please divide", "this sentence", "sentence into", and
 *  "into ngrams".
 * 
 * <p>This filter handles position increments > 1 by inserting filler tokens 
 * (tokens with termtext "_"). It does not handle a position increment of 0.
 *
 * @author Sebastian Kirsch <skirsch@sebastian-kirsch.org>
 */
public class NGramFilter extends TokenFilter {

	protected CircularFifoBuffer ngramBuf;
	protected UnboundedFifoBuffer outputBuf;
	protected UnboundedFifoBuffer tokenBuf;
	protected StringBuffer[] ngrams;
	protected String type = "ngram";

	/**
	 * default n-gram size is 2.
	 */
	public final static int DEFAULTNGRAMSIZE = 2;

	/**
	 * By default, we output unigrams as well as n-grams.
	 */
	protected boolean outputUnigrams = true;

	/**
	 * Construct an NGramFilter with the specified n-gram size from the 
	 * TokenStream <code>input</code>
	 * 
	 * @param input input stream
	 * @param ngramsize maximum n-gram size produced by the filter.
	 */
	public NGramFilter(TokenStream input, int ngramsize) {
		super(input);
		this.outputBuf = new UnboundedFifoBuffer();
		this.tokenBuf = new UnboundedFifoBuffer();
		this.setNGramSize(ngramsize);
	}

	/**
	 * Construct an NGramFilter with default n-gram size.
	 *  
	 * @param input input stream
	 */
	public NGramFilter(TokenStream input) {
		this(input, DEFAULTNGRAMSIZE);
	}

	/**
	 * Construct an NGramFilter with the specified field name for n-gram tokens. 
	 * 
	 * @param input input stream
	 * @param fieldname field name for n-gram tokens
	 */
	public NGramFilter(TokenStream input, String fieldname) {
		this(input, DEFAULTNGRAMSIZE);
		this.setType(fieldname);
	}

	/**
	 * Set the type of the n-gram tokens produced by this filter.
	 * (default: "ngram")
	 * 
	 * @param type token type
	 * @return receiver
	 */
	public NGramFilter setType(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Shall the output stream contain the input tokens (unigrams) as well as
	 * n-grams? (default: true.)
	 * 
	 * @param flag
	 * @return receiver
	 */
	public NGramFilter setOutputUnigrams(boolean flag) {
		this.outputUnigrams = flag;
		return this;
	}

	/**
	 * Set the n-gram size (default: 2)
	 * 
	 * @param n n-gram size
	 * @return receiver
	 */
	public NGramFilter setNGramSize(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("N-gram size must be >= 1");
		}
		this.ngramBuf = new CircularFifoBuffer(n);
		this.ngrams = new StringBuffer[n];
		for (int i = 0; i < this.ngrams.length; i++) {
			this.ngrams[i] = new StringBuffer();
		}
		return this;
	}

	/**
	 * Clear the StringBuffers that are used for storing the output n-grams.
	 */
	protected void clearNGrams() {
		for (int i = 0; i < this.ngrams.length; i++) {
			this.ngrams[i].setLength(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#next()
	 */
	public Token next() throws IOException {
		if (outputBuf.isEmpty()) {
			this.fillOutputBuf();
		}
		if (outputBuf // is still empty
				.isEmpty()) {
			return null;
		} else {
			return (Token) outputBuf.remove();
		}
	}

	/**
	 * Get the next token from the input stream and push it on the token buffer.
	 * If we encounter a token with position increment > 1, we put filler tokens 
	 * on the token buffer.
	 * 
	 * Returns null when the end of the input stream is reached. 
	 */
	private Token getNextToken() throws IOException {
		if (tokenBuf.isEmpty()) {
			Token lastToken = input.next();
			if (lastToken != null) {
				for (int i = 1; i < lastToken.getPositionIncrement(); i++) {
					tokenBuf.add(new Token("_", lastToken.startOffset(), lastToken.startOffset()));
				}
				tokenBuf.add(lastToken);
				return this.getNextToken();
			} else {
				return null;
			}
		} else {
			return (Token) tokenBuf.remove();
		}
	}

	/**
	 * Fill the output buffer with new n-grams.
	 * 
	 * @throws java.io.IOException
	 */
	private void fillOutputBuf() throws IOException {
		boolean addedToken = false;

		/*
		 * Try to fill the ngram buffer.
		 */
		do {
			Token token = this.getNextToken();
			if (token != null) {
				ngramBuf.add(token);
				addedToken = true;
			} else {
				break;
			}
		} while (! ngramBuf.isFull());

		/*
		 * If no new token could be added to the ngram buffer, we have reached
		 * the end of the input stream and have to discard the least recent token.
		 */
		if (! addedToken) {
			if (ngramBuf.isEmpty()) {
				return; 
			} else {
				ngramBuf.remove();
			}
		}

		this.clearNGrams();

		int[] endOffsets = new int[ngramBuf.size()];
		for (int i = 0; i < endOffsets.length; i++) {
			endOffsets[i] = 0;
		}

		int i = 0;
		Token token = null;
		for (Iterator it = ngramBuf.iterator(); it.hasNext(); ) {
			token = (Token) it.next();
			for (int j = i; j < ngrams.length; j++) {
				if (ngrams[j].length() != 0) {
					ngrams[j].append(" ");
				}
				ngrams[j].append(token.termText());
			}
			endOffsets[i] = token.endOffset();
			i++;
		}

		if ((! ngramBuf.isEmpty()) && outputUnigrams) {
			Token unigram = (Token) ngramBuf.get();
			unigram.setPositionIncrement(1);
			outputBuf.add(unigram);
		}

		/*
		 * Push new tokens to the output buffer.
		 */
		for (int j = 1; j < ngramBuf.size(); j++) {
			Token ngram = new Token(ngrams[j].toString(), 
					((Token) ngramBuf.get()).startOffset(), 
					endOffsets[j],
					this.type);
			if ((! outputUnigrams) && j == 1) {
				ngram.setPositionIncrement(1);
			} else {
				ngram.setPositionIncrement(0);
			}
			outputBuf.add(ngram);
		}
	}

}
