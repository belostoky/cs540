import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 * 
 * You must add code for the 5 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
	private DecTreeNode root;
	private List<String> labels; // ordered list of class labels
	private List<String> attributes; // ordered list of attributes
	private Map<String, List<String>> attributeValues; // map to ordered
														// discrete values taken
														// by attributes

	/**
	 * Answers static questions about decision trees.
	 */
	DecisionTreeImpl() {
		// no code necessary
		// this is void purposefully
	}

	/**
	 * Build a decision tree given only a training set.
	 * 
	 * @param train: the training set
	 */
	DecisionTreeImpl(DataSet train) {

		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		// TODO: add code here

        root = build_tree(train.instances, copy_attrs(attributes), 1, 1);

	}

	/**
	 * Build a decision tree given a training set then prune it using a tuning
	 * set.
	 * 
	 * @param train: the training set
	 * @param tune: the tuning set
	 */
	DecisionTreeImpl(DataSet train, DataSet tuning_data) {

		// TODO: add code here
        this(train);

        tune(tuning_data);

	}

	@Override
	public String classify(Instance instance) {

		// TODO: add code here
        DecTreeNode node = root;
        //This loop will never break if node isn't reassigned to one of its children,
        //which will be the case if the node isn't terminal but doesn't have a child
        //with an attribute value equal to that of this instance
        boolean node_has_changed = true;
        while (!node.terminal && node_has_changed) {
            //find child node with matching attribute value for node's attribute
            node_has_changed = false;
            for (DecTreeNode child : node.children) {
                if (child.parentAttributeValue.intValue() == instance.attributes.get(node.attribute.intValue()).intValue()) {
                    node_has_changed = true;
                    node = child;
                    break;
                }
            }
        }
		return labels.get(node.label);
	}

	@Override
	/**
	 * Print the decision tree in the specified format
	 */
	public void print() {

		printTreeNode(root, null, 0);
	}
	
	/**
	 * Prints the subtree of the node
	 * with each line prefixed by 4 * k spaces.
	 */
	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if (parent == null) {
			value = "ROOT";
		} else{
			String parentAttribute = attributes.get(parent.attribute);
			value = attributeValues.get(parentAttribute).get(p.parentAttributeValue);
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + labels.get(p.label) + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + attributes.get(p.attribute) + "?}");
			System.out.println(sb.toString());
			for(DecTreeNode child: p.children) {
				printTreeNode(child, p, k+1);
			}
		}
	}

	@Override
	public void rootInfoGain(DataSet train) {

		
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		// TODO: add code here

        for (String attribute : attributes) {
            System.out.format(
              "%s %.5f\n",
              attribute,
              info_gain(attribute, train.instances)
            );
        }
	}

    private float entropy(List<Instance> instances) {
        float sum = 0.0f;
        for (String label : labels) {
            int matches = 0;
            int label_index = labels.indexOf(label);
            for (Instance instance : instances) {
                if (instance.label.intValue() == label_index) {
                    ++matches;
                }
            }
            float prob = ((float)matches)/((float)instances.size());
            if (instances.size() != 0 && prob > 0.0f) {
                sum -= prob * Math.log(prob)/Math.log(2.0);
            }

        }
        return sum;
    }


    private float entropy(List<Instance> instances, String attr) {
        //entropy(attr) = sum_over_values(prob(attr = value)*entropy(attr, value))
        float sum = 0.0f;
        for (String value : attributeValues.get(attr)) {
            int matches = 0;
            for (Instance instance : instances) {
                if (instance.attributes.get(attributes.indexOf(attr)).intValue() == attributeValues.get(attr).indexOf(value)) {
                    ++matches;
                }
            }
            if (instances.size() != 0) {
                sum += ((float)matches)/((float)instances.size())*entropy(instances, attr, value);
            }
        }

        return sum;
    }


    private float entropy(List<Instance> instances, String attr, String value) {
        float sum = 0.0f;
        List<Instance> matching_instances = new ArrayList<Instance>();
        for (Instance instance : instances) {
            if (instance.attributes.get(attributes.indexOf(attr)).intValue() == attributeValues.get(attr).indexOf(value)) {
                matching_instances.add(instance);
            }
        }

        for (String label : labels) {
            int matches = 0;
            int label_index = labels.indexOf(label);
            for (Instance instance : matching_instances) {
                if (instance.label.intValue() == label_index) {
                    ++matches;
                }
            }
            float prob = ((float)matches)/((float)matching_instances.size());
            if (matching_instances.size() != 0 && prob > 0.0f) {
                sum -= prob * Math.log(prob)/Math.log(2.0);
            }
        }
        return sum;
    }


    private float info_gain(String attr, List<Instance> instances) {
        return entropy(instances) - entropy(instances, attr);
    }


    private DecTreeNode build_tree (
        List<Instance> instances,
        List<String> attrs,
        Integer default_label,
        Integer value
    ) {
        if (instances.isEmpty()) {
            return new DecTreeNode(
              default_label,
              null,
              value,
              true
            );
        }
        if (attrs.isEmpty()) {
            return new DecTreeNode(
              get_plurality_label (instances),
              null,
              value,
              true
            );
        }

        //check whether all instances have the same label
        Integer label = instances.get(0).label;
        boolean all_have_same_label = true;
        for (Instance instance : instances) {
            if (instance.label.intValue() != label.intValue()) {
                all_have_same_label = false;
            }
        }
        if (all_have_same_label) {
            return new DecTreeNode(
                label,
                null,
                value,
                true
            );
        }
        //calculate the attribute giving the best gain at this node
        float max_gain = -1;
        String max_gain_attr = "";
        boolean terminal = false;
        for (String attr : attrs) {
            float gain = info_gain(attr, instances);
            if (gain > max_gain) {
                max_gain_attr = attr;
                max_gain = gain;
            }
        }


        //by creating the other copy of attrs, I've probably done something I shouldn't have.
        int attr_index = 0, val_index = 0;
        attr_index = attributes.indexOf(max_gain_attr);
        DecTreeNode this_node;
        String attr_val;
        label = get_plurality_label(instances);
        this_node = new DecTreeNode(
          new Integer(label),
          new Integer(attr_index),
          value,
          false
        );

        List<String> attr_vals = attributeValues.get(max_gain_attr);
        if (attr_vals == null) {
            return this_node;
        }
        for (String val : attr_vals) {
            val_index = attr_vals.indexOf(val);
            List<Instance> filtered_instances = new ArrayList<Instance>();
            for (Instance instance : instances) {
                if (instance.attributes.get(attr_index).intValue() == val_index) {
                    filtered_instances.add(instance);
                }
            }
            List<String> filtered_attrs = copy_attrs(attrs);
            filtered_attrs.remove(max_gain_attr);
            this_node.children.add(build_tree(filtered_instances, filtered_attrs, default_label, val_index));
        }

        return this_node;
    }


    private List<String> copy_attrs(List<String> attributes) {
        //pretty sure this will have to be a new ArrayList<String>();
        List<String> attrs = new ArrayList<String>();
        for (String attr : attributes) {
            attrs.add(attr);
        }
        return attrs;
    }

    private float acc(DataSet tuning_data) {
        int correct_classification_count = 0;
        for (Instance instance : tuning_data.instances) {
            int instance_label_index = instance.label.intValue();
            String instance_label_value = tuning_data.labels.get(instance_label_index);
            if (instance_label_value.equals(classify(instance))) {
                ++correct_classification_count;
            }
        }
        return ((float)correct_classification_count)/((float)tuning_data.instances.size());
    }

    private int get_plurality_label(List<Instance> instances) {

        //count the number of occurences of each label along this path
        //there's definitely a better way to do this
        ArrayList<Integer> labels = new ArrayList<Integer>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        for (Instance instance: instances) {
            //if this label has already been seen, increment the corresponding count
            if (labels.contains(instance.label)) {
                int label_index = labels.indexOf(instance.label);
                counts.set(label_index, counts.get(label_index) + 1);
            //if not, add it to the list with count one.
            } else {
                labels.add(instance.label);
                counts.add(1);
            }
        }

        //find maximum count
        //the order of labels here is effectively random,
        //so using the first element in the list with the maximum count
        //effectively breaks ties randomly
        Integer max_count = -1;
        Integer plurality_label = -1;
        for (int i = 0; i < counts.size(); i++) {
            if (max_count < counts.get(i)) {
                max_count = counts.get(i);
                plurality_label = labels.get(i);
            }
        }
        return plurality_label;
    }


    private void tune(DataSet tuning_data) {
        Stack<DecTreeNode> stack = new Stack<DecTreeNode>();
        stack.push(root);
        //Map<DecTreeNode, Float> accuracy = new HashMap<DecTreeNode, Float>();
        //perform DFS to test accuracy after making every node in the tree
        //independently terminal
        float max_acc = 0.0f;
        DecTreeNode best_accuracy_node = null;
        while (!stack.isEmpty()) {
            DecTreeNode node = stack.pop();
            if (node.children != null) {
                for (DecTreeNode child : node.children) {
                    stack.push(child);
                }
            }
            if (!node.terminal) {
                node.terminal = true;
                float accuracy = acc(tuning_data);
                if (accuracy > max_acc) {
                    max_acc = accuracy;
                    best_accuracy_node = node;
                }
                node.terminal = false;
            }
        }
        best_accuracy_node.terminal = true;

        return;
    }



}
