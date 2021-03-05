
package org.drip.execution.nonadaptive;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2022 Lakshmi Krishnamurthy
 * Copyright (C) 2021 Lakshmi Krishnamurthy
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
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
 * <i>ContinuousPowerImpact</i> contains the Temporary Impact Power Law Trading Trajectory generated by the
 * Almgren and Chriss (2003) Scheme under the Criterion of No-Drift. The References are:
 * 
 * <br><br>
 *  <ul>
 * 		<li>
 * 			Almgren, R., and N. Chriss (1999): Value under Liquidation <i>Risk</i> <b>12 (12)</b>
 * 		</li>
 * 		<li>
 * 			Almgren, R. F., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of
 * 				Risk</i> <b>3 (2)</b> 5-39
 * 		</li>
 * 		<li>
 * 			Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk
 * 				<i>Applied Mathematical Finance</i> <b>10 (1)</b> 1-18
 * 		</li>
 * 		<li>
 * 			Almgren, R., and N. Chriss (2003): Bidding Principles <i>Risk</i> 97-102
 * 		</li>
 * 		<li>
 * 			Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs <i>Journal of Financial
 * 				Markets</i> <b>1</b> 1-50
 * 		</li>
 *  </ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/README.md">Optimal Impact/Capture Based Trading Trajectories - Deterministic, Stochastic, Static, and Dynamic</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/nonadaptive/README.md">Almgren-Chriss Static Optimal Trajectory</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class ContinuousPowerImpact extends org.drip.execution.nonadaptive.StaticOptimalSchemeContinuous {

	/**
	 * Create the Standard ContinuousPowerImpact Instance
	 * 
	 * @param dblStartHoldings Trajectory Start Holdings
	 * @param dblFinishTime Trajectory Finish Time
	 * @param lpep Almgren 2003 Linear Permanent Expectation Market Impact Parameters
	 * @param dblRiskAversion The Risk Aversion Parameter
	 * 
	 * @return The ContinuousPowerImpact Instance
	 */

	public static final ContinuousPowerImpact Standard (
		final double dblStartHoldings,
		final double dblFinishTime,
		final org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep,
		final double dblRiskAversion)
	{
		try {
			return new ContinuousPowerImpact (new org.drip.execution.strategy.OrderSpecification
				(dblStartHoldings, dblFinishTime), lpep, new
					org.drip.execution.risk.MeanVarianceObjectiveUtility (dblRiskAversion));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ContinuousPowerImpact (
		final org.drip.execution.strategy.OrderSpecification os,
		final org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep,
		final org.drip.execution.risk.MeanVarianceObjectiveUtility mvou)
		throws java.lang.Exception
	{
		super (os, lpep, mvou);
	}

	@Override public org.drip.execution.optimum.EfficientTradingTrajectory generate()
	{
		org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep =
			(org.drip.execution.dynamics.LinearPermanentExpectationParameters) priceEvolutionParameters();

		final org.drip.execution.impact.TransactionFunction tfTemporaryExpectation =
			lpep.temporaryExpectation().epochImpactFunction();

		if (!(tfTemporaryExpectation instanceof org.drip.execution.impact.TransactionFunctionPower))
			return null;

		double dblEpochVolatility = java.lang.Double.NaN;
		final org.drip.execution.impact.TransactionFunctionPower tfpTemporaryExpectation =
			(org.drip.execution.impact.TransactionFunctionPower) tfTemporaryExpectation;

		double dblLambda = ((org.drip.execution.risk.MeanVarianceObjectiveUtility)
			objectiveUtility()).riskAversion();

		try {
			dblEpochVolatility = lpep.arithmeticPriceDynamicsSettings().epochVolatility();
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		final double dblSigma = dblEpochVolatility;

		org.drip.execution.strategy.OrderSpecification os = orderSpecification();

		double dblGamma = ((org.drip.execution.impact.TransactionFunctionLinear)
			lpep.linearPermanentExpectation().epochImpactFunction()).slope();

		final double dblK = tfpTemporaryExpectation.exponent();

		final double dblExecutionTime = os.maxExecutionTime();

		double dblEta = tfpTemporaryExpectation.constant();

		final double dblX = os.size();

		final double dblTStar = java.lang.Math.pow (dblK * dblEta * java.lang.Math.pow (dblX, dblK - 1.) /
			(dblLambda * dblSigma * dblSigma), 1. / (dblK + 1.));

		double dblTMax = dblK > 1. ? (dblK + 1.) / (dblK - 1.) * dblTStar : java.lang.Double.NaN;

		double dblE = 0.5 * dblGamma * dblX * dblX + (dblK + 1.) / (3. * dblK + 1.) * dblEta *
			java.lang.Math.pow (dblX / dblTStar, dblK + 1.) * dblTStar;

		double dblV = (dblK + 1.) / (3. * dblK + 1.) * dblSigma * dblSigma * dblTStar * dblX * dblX;

		double dblHyperboloidBoundaryValue = java.lang.Math.pow ((dblK + 1.) / (3. * dblK + 1.), dblK + 1.) *
			dblEta * java.lang.Math.pow (dblSigma, 2. * dblK) * java.lang.Math.pow (dblX, 3. * dblK + 1.);

		final org.drip.function.definition.R1ToR1 r1ToR1Holdings = new org.drip.function.definition.R1ToR1
			(null) {
			@Override public double evaluate (
				final double dblT)
				throws java.lang.Exception
			{
				if (!org.drip.numerical.common.NumberUtil.IsValid (dblT))
					throw new java.lang.Exception
						("ContinuousPowerImpact::generate::evaluate => Invalid Inputs");

				if (1. > dblK)
					return dblX * java.lang.Math.pow (1. + ((1. - dblK) * dblT) / ((1. + dblK) * dblTStar),
						-1. * (1. + dblK) / (1. - dblK));

				if (1. == dblK) return dblX * java.lang.Math.pow (java.lang.Math.E, -1. * dblT/ dblTStar);

				double dblHoldings = dblX *  java.lang.Math.pow (1. - ((dblK - 1.) * dblT) / ((dblK + 1.) *
					dblTStar), (dblK + 1.) / (dblK + 1.));

				return 0. > dblX * dblHoldings ? 0. : dblHoldings;
			}
		};

		final org.drip.function.definition.R1ToR1 r1ToR1TradeRateSquared = new
			org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblTime)
				throws java.lang.Exception
			{
				double dblTradeRate = r1ToR1Holdings.derivative (dblTime, 1);

				double dblTemporaryImpactCoefficient = tfpTemporaryExpectation.evaluate (dblTradeRate);

				return dblTemporaryImpactCoefficient * dblTemporaryImpactCoefficient * dblTradeRate *
					dblTradeRate;
			}
		};

		org.drip.function.definition.R1ToR1 r1ToR1TransactionCostExpectation = new
			org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblTime)
				throws java.lang.Exception
			{
				return r1ToR1TradeRateSquared.integrate (dblTime, dblExecutionTime);
			}
		};

		final org.drip.function.definition.R1ToR1 r1ToR1HoldingsSquared = new
			org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblTime)
				throws java.lang.Exception
			{
				double dblHoldings = r1ToR1Holdings.evaluate (dblTime);

				return dblHoldings * dblHoldings;
			}
		};

		org.drip.function.definition.R1ToR1 r1ToR1TransactionCostVariance = new
			org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblTime)
				throws java.lang.Exception
			{
				return dblSigma * dblSigma * r1ToR1HoldingsSquared.integrate (dblTime, dblExecutionTime);
			}
		};

		return org.drip.execution.optimum.PowerImpactContinuous.Standard (dblExecutionTime, dblE,
			dblV, dblTStar, dblTMax, dblHyperboloidBoundaryValue, dblEta * (dblX / dblExecutionTime) /
				(dblEpochVolatility * java.lang.Math.sqrt (dblExecutionTime)), r1ToR1Holdings,
					r1ToR1TransactionCostExpectation, r1ToR1TransactionCostVariance);
	}
}
