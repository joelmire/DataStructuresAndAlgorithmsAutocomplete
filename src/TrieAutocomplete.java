 import java.awt.List;
import java.util.*;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 * @author Jeff Forbes
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws NullPointerException
	 *             if either argument is null
	 * @throws IllegalArgumentException
	 *             if terms and weights are different weight
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
		if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			if (weights[i] < 0) throw new IllegalArgumentException("invalid weight");
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 */
	private void add(String word, double weight) {
		// TODO: Implement add
		if (word == null) throw new NullPointerException();
		if (weight < 0) throw new IllegalArgumentException();
		Node current = myRoot;
		for (int k = 0; k < word.length(); k++) {
			if (!current.children.containsKey(word.charAt(k))) {
				current.children.put(word.charAt(k), new Node(word.charAt(k), current, weight));
			}
		    current.mySubtreeMaxWeight = Math.max(current.mySubtreeMaxWeight, weight);
			current = current.getChild(word.charAt(k));
			}
		current.isWord = true;
		current.setWeight(weight);
		current.setWord(word);	
		}
	

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An Iterable of the k words with the largest weights among all
	 *         words starting with prefix, in descending weight order. If less
	 *         than k such words exist, return all those words. If no such words
	 *         exist, return an empty Iterable
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		ArrayList<String> list = new ArrayList<>();
		PriorityQueue<Node> npq = new PriorityQueue<Node>(new Node.ReverseSubtreeMaxWeightComparator());
		Node current = myRoot;
		for (int i = 0; i < prefix.length(); i ++) {
			if(current.getChild(prefix.charAt(i)) == null)
				return new ArrayList<String>();
			current = current.getChild(prefix.charAt(i));
		}
		npq.add(current);
		while (list.size() < k && !npq.isEmpty()){
			Node thisNode = npq.poll();
			if (thisNode.isWord)
				list.add(thisNode.myWord);
			if (!thisNode.children.values().isEmpty())
				npq.addAll(thisNode.children.values());
		}
		return list;
	}

	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from with the largest weight starting with prefix, or an
	 *         empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		Node current = myRoot;
		for (int i = 0; i < prefix.length(); i ++) {
			if(current.getChild(prefix.charAt(i)) == null)
				return new String();
			current = current.getChild(prefix.charAt(i));
		}
		while (current != null && !(current.mySubtreeMaxWeight == current.myWeight))
			current = Collections.min(current.children.values(), new Node.ReverseSubtreeMaxWeightComparator());
		return current.getWord();
	}

	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		// TODO complete weightOf
		return 0.0;
	}
}
