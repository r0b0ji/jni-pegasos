
package jnipegasos;

import java.util.Map;
import java.util.TreeMap;

/**
 * PrimalSVMWeights weight vector of Primal SVM solution
 * Weight vector in sparse vector format, where only non-zero dimensions are
 * written. m_dims contains the index of no-zero dimension and m_vals contain the
 * value of the feature in that dimension
 * 
 * @author Deepak Nayak
 * 
 */
public class PrimalSVMWeights implements java.io.Serializable {

	protected TreeMap<Integer, Double> weights;
	protected double m_factor;

	public PrimalSVMWeights(double factor, TreeMap<Integer, Double> wts) {
		this.m_factor = factor;
		this.weights = new TreeMap<Integer, Double>(wts);
	}

	public PrimalSVMWeights() {
		this.m_factor = 1.0;
		this.weights = new TreeMap<Integer, Double>();
	}

	public PrimalSVMWeights(TreeMap<Integer, Double> wts) {
		this.m_factor = 1.0;
		this.weights = new TreeMap<Integer, Double>(wts);
	}

	public double getFactor() {
		return this.m_factor;
	}

	public TreeMap<Integer, Double> getWeights() {
		return this.weights;
	}


	public void setFactor(double factor) {
		this.m_factor = factor;
	}

	public void addFeature(int dim, double val) {
		this.weights.put(dim, val);
	}

	public void setFeatures(double factor, TreeMap<Integer, Double> wts) {
		this.m_factor = factor;
		this.weights.putAll(wts);

	}

	public void setFeatures(TreeMap<Integer, Double> wts) {
		this.m_factor = 1.0;
		this.weights.putAll(wts);	
	}
	/**
	 * Returns the L2 norm factor of this vector's values.
	 */
	public double getL2Norm() {
		double square_sum = 0.0;
		for (Map.Entry<Integer, Double> entry : this.weights.entrySet()) {
			double value = entry.getValue();
			square_sum += value * value;
		}
		return Math.sqrt(square_sum);
	}

}
