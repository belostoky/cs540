import java.util.Map;
import java.util.HashMap;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	private final double delta = 0.00001;
	
	private Map<Label, Integer> labelCount;
	private Map<Label, Map<String, Integer> > wordTypeCount;
	private Map<Label, Integer> labelTokenCount;
	private int numInstance;
	//private int numTotalToken;
	private int vocabularySize;
	
	
	@Override
	public void train(Instance[] trainingData, int v) {
		//Implement
	    vocabularySize = v;
        //Let's prevent some segfaults here...
        labelCount = new HashMap<Label, Integer>();
        wordTypeCount = new HashMap<Label, Map<String,Integer>>();
        labelTokenCount = new HashMap<Label, Integer>();
        labelCount.put(Label.SPAM, 0);
        labelCount.put(Label.HAM, 0);
        labelTokenCount.put(Label.HAM, 0);
        labelTokenCount.put(Label.SPAM, 0);
        wordTypeCount.put(Label.HAM, new HashMap<String,Integer>());
        wordTypeCount.put(Label.SPAM, new HashMap<String,Integer>());
        numInstance = trainingData.length;
        for (Instance instance : trainingData) {
            //update the three maps with the right counts from training data
            Label label = instance.label;
            int label_count = labelCount.get(label);
            //I could just use this to set label_count, but I'm afraid of using the return value from a remove().
            labelCount.remove(instance.label);
            labelCount.put(instance.label, ++label_count);

            int label_token_count = labelTokenCount.get(label);
            labelTokenCount.remove(label);
            labelTokenCount.put(label, label_token_count + instance.words.length);

            //this one's a little trickier.
            for (String word : instance.words) {
                //search for it in wordTypeCount
                if (wordTypeCount.get(label).get(word) == null) {
                    //if it's not there, add it with multiplicity 1
                    wordTypeCount.get(label).put(word, 1);
                } else {
                    //if it is there, increment its count
                    int wtc = wordTypeCount.get(label).get(word);
                    wordTypeCount.get(label).remove(word);
                    wordTypeCount.get(label).put(word, ++wtc);
                }
            }
        }
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(Label.SPAM) or P(Label.HAM)
	 */
	@Override
	public double p_l(Label label) {				
		//Implement
        //return ((double)labelCount.get(label)/((double)(labelCount.get(Label.SPAM)+labelCount.get(Label.HAM))));
        return ((double)labelCount.get(label))/((double)numInstance);
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|Label.SPAM) or P(word|Label.HAM)
	 */
	@Override
	public double p_w_given_l(String word, Label label) {
        //For some reason, all my probabilities are /slightly/ too large.
		// Implement
        int sum = labelTokenCount.get(label);
        int wtc;
        if (wordTypeCount.get(label).get(word) == null) {
            wtc = 0;
        } else {
            wtc = wordTypeCount.get(label).get(word);
        }
	    double prob = ((double)wtc + delta)/(((double)vocabularySize)*delta+(double)sum);	
		return prob;
	}
	
	/**
	 * Classifies an array of words as either Label.SPAM or Label.HAM. 
	 */
	@Override
	public ClassifyResult classify(String[] words) {
		// Implement
		ClassifyResult result = new ClassifyResult();
        double log_probs_spam = 0.0;
        double log_probs_ham = 0.0;
        for (String word : words) {
            double probability_spam = p_w_given_l(word, Label.SPAM);
            double probability_ham = p_w_given_l(word, Label.HAM);
            log_probs_spam += Math.log(probability_spam);
            log_probs_ham += Math.log(probability_ham);
        }
        //but how to calculate the log probs?
        if (log_probs_spam > log_probs_ham) {
            result.label = Label.SPAM;
        } else {
            result.label = Label.HAM;
        }
        result.log_prob_spam = log_probs_spam;
        result.log_prob_ham = log_probs_ham;
		return result;
	}
	
	/**
	 * Gets the confusion matrix for a test set. 
	 */
	@Override
	public ConfusionMatrix calculateConfusionMatrix(Instance[] testData)
	{
		// Implement
        int true_positives = 0;
        int true_negatives = 0;
        int false_positives = 0;
        int false_negatives = 0;
        for (Instance instance : testData) {
            ClassifyResult result = classify(instance.words);
            if (result.label == Label.SPAM && instance.label == Label.SPAM) {
                //is this a positive or a negative?
                true_positives++;
            } else if (result.label == Label.HAM && instance.label == Label.HAM) {
                true_negatives++;
            } else if (result.label == Label.SPAM && instance.label == Label.HAM) {
                false_positives++;
            } else {
                false_negatives++;
            }
        }
		return new ConfusionMatrix(true_positives, true_negatives,false_positives, false_negatives);
	}
}
