
package jnipegasos;

/**
 * PrimalSVMWeights weight vector of Primal SVM solution
 * Weight vector in sparse vector format, where only non-zero dimensions are
 * written. m_dims contains the index of no-zero dimension and m_vals contain the
 * value of the feature in that dimension
 * 
 * @author deepak
 * 
 */
public class PrimalSVMWeights implements java.io.Serializable {

  protected int[] m_dims;

  protected double m_factor;

  protected double[] m_vals;
  
  protected int max_idx;

  public PrimalSVMWeights(double factor, int[] dims, double[] vals) {
    this.m_factor = factor;
    this.m_dims = dims;
    this.m_vals = vals;
    this.max_idx = dims.length;
  }

  public PrimalSVMWeights(int size) {
    this.m_factor = 1.0;
    this.m_dims = new int[size];
    this.m_vals = new double[size];
    this.max_idx = size;
  }

  public PrimalSVMWeights(int[] dims, double[] vals) {
    this.m_factor = 1.0;
    this.m_dims = dims;
    this.m_vals = vals;
    this.max_idx = dims.length;
  }

  public int getDimAt(int index) {
    return m_dims[index];
  }

  public double getFactor() {
    return m_factor;
  }

  /**
   * Returns the linear norm factor of this vector's values (i.e., the sum of
   * it's values).
   */
  public double getL1Norm() {
    double sum = 0.0;
    for (int i = 0; i < m_vals.length; i++) {
      sum += m_vals[i];
    }
    return sum;
  }

  /**
   * Returns the L2 norm factor of this vector's values.
   */
  public double getL2Norm() {
    double square_sum = 0.0;
    for (int i = 0; i < m_vals.length; i++) {
      square_sum += (m_vals[i] * m_vals[i]);
    }
    return Math.sqrt(square_sum);
  }

  public double getValueAt(int index) {
    return m_vals[index];
  }

  /**
   * Performs a linear normalization to the value 1.
   */
  public void normalizeL1() {
    normalizeL1(getL1Norm());
  }

  /**
   * Performs a linear normalization to the given norm value.
   */
  public void normalizeL1(double norm) {
    for (int i = 0; i < m_vals.length; i++) {
      if (m_vals[i] > 0) {
        m_vals[i] /= norm;
      }
    }
  }

  /**
   * Performs an L2 normalization to the value 1.
   */
  public void normalizeL2() {
    double norm = Math.pow(getL2Norm(), 2);
    for (int i = 0; i < m_vals.length; i++) {
      m_vals[i] = Math.pow(m_vals[i], 2) / norm;
    }
  }

  public void setFactor(double factor) {
    this.m_factor = factor;
  }

  public void setFeatures(int[] dims, double[] vals, int size) {
    this.m_dims = dims;
    this.m_vals = vals;
    this.max_idx = size;
  }

  public int size() {
    return m_dims.length;
  }

  public String toString() {
    String s = "";
    for (int i = 0; i < m_vals.length; i++) {
      s += "" + m_dims[i] + ":" + m_vals[i] + ""
          + (i < m_vals.length - 1 ? " " : "");
    }
    return s;
  }
}
