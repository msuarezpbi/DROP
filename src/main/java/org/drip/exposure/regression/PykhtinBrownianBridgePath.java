
package org.drip.exposure.regression;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
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
 * PykhtinBrownianBridgePath generates the Regression Based Path Exposures off of the Pillar Vertexes using
 *  the Pykhtin (2009) Scheme. The References are:
 *  
 *  - Andersen, L. B. G., M. Pykhtin, and A. Sokol (2017): Re-thinking Margin Period of Risk,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2902737, eSSRN.
 *  
 *  - Andersen, L. B. G., M. Pykhtin, and A. Sokol (2017): Credit Exposure in the Presence of Initial Margin,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2806156, eSSRN.
 *  
 *  - Albanese, C., and L. Andersen (2014): Accounting for OTC Derivatives: Funding Adjustments and the
 *  	Re-Hypothecation Option, eSSRN, https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2482955.
 *  
 *  - Burgard, C., and M. Kjaer (2017): Derivatives Funding, Netting, and Accounting, eSSRN,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2534011.
 * 
 *  - Piterbarg, V. (2010): Funding Beyond Discounting: Collateral Agreements and Derivatives Pricing, Risk
 *  	21 (2) 97-102.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PykhtinBrownianBridgePath
{
	private java.util.Map<java.lang.Integer, java.lang.Double> _sparseVertexExposureTrajectory = null;
	private java.util.Map<java.lang.Integer, org.drip.function.definition.R1ToR1> _localVolatilityTrajectory
		= null;

	/**
	 * PykhtinBrownianBridgePath Constructor
	 * 
	 * @param sparseVertexExposureTrajectory The Sparse Vertex Exposure Amount Trajectory
	 * @param localVolatilityTrajectory The R^1 To R^1 Local Volatility Trajectory
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PykhtinBrownianBridgePath (
		final java.util.Map<java.lang.Integer, java.lang.Double> sparseVertexExposureTrajectory,
		final java.util.Map<java.lang.Integer, org.drip.function.definition.R1ToR1>
			localVolatilityTrajectory)
		throws java.lang.Exception
	{
		if (null == (_sparseVertexExposureTrajectory = sparseVertexExposureTrajectory) ||
			null == (_localVolatilityTrajectory = localVolatilityTrajectory))
		{
			throw new java.lang.Exception ("PykhtinBrownianBridgePath Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Path Sparse Vertex Exposure Trajectory
	 * 
	 * @return The Path Sparse Vertex Exposure Trajectory
	 */

	public java.util.Map<java.lang.Integer, java.lang.Double> sparseVertexExposureTrajectory()
	{
		return _sparseVertexExposureTrajectory;
	}

	/**
	 * Retrieve the Path Sparse Vertex Local Volatility Trajectory
	 * 
	 * @return The Path Sparse Vertex Local Volatility Trajectory
	 */

	public java.util.Map<java.lang.Integer, org.drip.function.definition.R1ToR1> localVolatilityTrajectory()
	{
		return _localVolatilityTrajectory;
	}

	/**
	 * Generate the Dense (Complete) Segment Exposures
	 * 
	 * @param wanderTrajectory The Wander Date Trajectory
	 * 
	 * @return The Dense (Complete) Segment Exposures
	 */

	public java.util.Map<java.lang.Integer, java.lang.Double> denseExposure (
		final java.util.Map<java.lang.Integer, java.lang.Double> wanderTrajectory)
	{
		int sparseLeftPillarDate = -1;

		java.util.Map<java.lang.Integer, java.lang.Double> denseExposureTrajectory = new
			java.util.TreeMap<java.lang.Integer, java.lang.Double>();

		for (java.util.Map.Entry<java.lang.Integer, java.lang.Double> sparseExposureTrajectoryEntry :
			_sparseVertexExposureTrajectory.entrySet())
		{
			int sparseRightPillarDate = sparseExposureTrajectoryEntry.getKey();

			if (-1 == sparseLeftPillarDate)
			{
				sparseLeftPillarDate = sparseRightPillarDate;
				continue;
			}

			try
			{
				new PykhtinBrownianBridgeSegment (
					new org.drip.exposure.regression.PillarVertex (
						sparseLeftPillarDate,
						_sparseVertexExposureTrajectory.get (sparseLeftPillarDate)
					),
					new org.drip.exposure.regression.PillarVertex (
						sparseRightPillarDate,
						_sparseVertexExposureTrajectory.get (sparseRightPillarDate)
					),
					_localVolatilityTrajectory.get (sparseRightPillarDate)
				).denseExposureTrajectoryUpdate (
					denseExposureTrajectory,
					wanderTrajectory
				);
			}
			catch (java.lang.Exception e)
			{
				e.printStackTrace();

				return null;
			}

			sparseLeftPillarDate = sparseRightPillarDate;
		}

		return denseExposureTrajectory;
	}
}
