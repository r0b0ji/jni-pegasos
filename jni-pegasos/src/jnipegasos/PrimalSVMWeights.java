/*
 * JNI_Pegasos - A Java Native Interface for Pegasos SVM
 * 
 * Copyright (C) 2012
 * Deepak Nayak 
 * Columbia University, Computer Science MS'13
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package jnipegasos;

import java.util.Map;
import java.util.TreeMap;

/**
 * PrimalSVMWeights weight vector of Primal SVM solution
 * Weight vector in sparse vector format, where only non-zero dimensions are
 * written. weights is a TreeMap which holds <dimension, weight> pair. The weight
 * vector is written by Pegasos trainer and has format dim:weight pairs separated
 * by space, and only non-zero values are written in the model file. So, this
 * TreeMap also stores only those pairs. 
 * @author Deepak Nayak
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
