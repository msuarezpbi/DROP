
package org.drip.spline.bspline;

import org.drip.function.definition.R1ToR1;
import org.drip.numerical.common.NumberUtil;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2025 Lakshmi Krishnamurthy
 * Copyright (C) 2024 Lakshmi Krishnamurthy
 * Copyright (C) 2023 Lakshmi Krishnamurthy
 * Copyright (C) 2022 Lakshmi Krishnamurthy
 * Copyright (C) 2021 Lakshmi Krishnamurthy
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	graph builder/navigator, and computational support.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Product Core - https://lakshmidrip.github.io/DROP-Product-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Computational Core - https://lakshmidrip.github.io/DROP-Computational-Core/
 * 
 * 	DROP Product Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Loan Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 *  - Asset Liability Management Analytics
 * 	- Capital Estimation Analytics
 * 	- Exposure Analytics
 * 	- Margin Analytics
 * 	- XVA Analytics
 * 
 * 	DROP Computational Core implements libraries for the following:
 * 	- Algorithm Support
 * 	- Computation Support
 * 	- Function Analysis
 *  - Graph Algorithm
 *  - Model Validation
 * 	- Numerical Analysis
 * 	- Numerical Optimizer
 * 	- Spline Builder
 *  - Statistical Learning
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>SegmentBasisFunction</i> is the abstract class over which the local ordered envelope functions for the
 * 	B Splines are implemented. It exposes the following stubs:
 * 
 *  <ul>
 * 		<li>Retrieve the Order of the B Spline</li>
 * 		<li>Retrieve the Leading Predictor Ordinate</li>
 * 		<li>Retrieve the Following Predictor Ordinate</li>
 * 		<li>Retrieve the Trailing Predictor Ordinate</li>
 * 		<li>Compute the complete Envelope Integrand - this will serve as the Envelope Normalizer</li>
 * 		<li>Evaluate the Cumulative Normalized Integrand up to the given ordinate</li>
 *  </ul>
 *
 *  <br>
 *  <style>table, td, th {
 *  	padding: 1px; border: 2px solid #008000; border-radius: 8px; background-color: #dfff00;
 *		text-align: center; color:  #0000ff;
 *  }
 *  </style>
 *  
 *  <table style="border:1px solid black;margin-left:auto;margin-right:auto;">
 *		<tr><td><b>Module </b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></td></tr>
 *		<tr><td><b>Library</b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics</a></td></tr>
 *		<tr><td><b>Project</b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline/README.md">Basis Splines and Linear Compounders across a Broad Family of Spline Basis Functions</a></td></tr>
 *		<tr><td><b>Package</b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline/bspline/README.md">de Boor Rational/Exponential/Tension B-Splines</a></td></tr>
 *  </table>
 *  <br>
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class SegmentBasisFunction
	extends R1ToR1
{
	private int _bSplineOrder = -1;
	private double _leadingPredictorOrdinate = Double.NaN;
	private double _trailingPredictorOrdinate = Double.NaN;
	private double _followingPredictorOrdinate = Double.NaN;

	protected SegmentBasisFunction (
		final int bSplineOrder,
		final double leadingPredictorOrdinate,
		final double followingPredictorOrdinate,
		final double trailingPredictorOrdinate)
		throws Exception
	{
		super (null);

		if (!NumberUtil.IsValid (_leadingPredictorOrdinate = leadingPredictorOrdinate) ||
			!NumberUtil.IsValid (_followingPredictorOrdinate = followingPredictorOrdinate) ||
			!NumberUtil.IsValid (_trailingPredictorOrdinate = trailingPredictorOrdinate) ||
			_leadingPredictorOrdinate >= _followingPredictorOrdinate ||
			_followingPredictorOrdinate >= _trailingPredictorOrdinate || 2 > (_bSplineOrder = bSplineOrder)
		) {
			throw new Exception ("SegmentBasisFunction ctr: Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Order of the B Spline
	 * 
	 * @return The Order of the B Spline
	 */

	public int bSplineOrder()
	{
		return _bSplineOrder;
	}

	/**
	 * Retrieve the Leading Predictor Ordinate
	 * 
	 * @return The Leading Predictor Ordinate
	 */

	public double leading()
	{
		return _leadingPredictorOrdinate;
	}

	/**
	 * Retrieve the Following Predictor Ordinate
	 * 
	 * @return The Following Predictor Ordinate
	 */

	public double following()
	{
		return _followingPredictorOrdinate;
	}

	/**
	 * Retrieve the Trailing Predictor Ordinate
	 * 
	 * @return The Trailing Predictor Ordinate
	 */

	public double trailing()
	{
		return _trailingPredictorOrdinate;
	}

	/**
	 * Compute the complete Envelope Integrand - this will serve as the Envelope Normalizer.
	 * 
	 * @return The Complete Envelope Integrand.
	 * 
	 * @throws java.lang.Exception Thrown if the Complete Envelope Integrand cannot be calculated.
	 */

	public abstract double normalizer()
		throws java.lang.Exception;

	/**
	 * Evaluate the Cumulative Normalized Integrand up to the given ordinate
	 * 
	 * @param dblPredictorOrdinate The Predictor Ordinate
	 * 
	 * @return The Cumulative Normalized Integrand up to the given ordinate
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public abstract double normalizedCumulative (
		final double dblPredictorOrdinate)
		throws java.lang.Exception;
}
