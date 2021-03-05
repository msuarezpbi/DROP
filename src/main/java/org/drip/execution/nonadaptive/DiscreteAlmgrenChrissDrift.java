
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
 * <i>DiscreteAlmgrenChrissDrift</i> generates the Trade/Holdings List of Optimal Execution Schedule for the
 * Equally Spaced Trading Intervals based on the Linear Impact Evolution Walk Parameters with Drift
 * specified. The References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Almgren, R., and N. Chriss (1999): Value under Liquidation <i>Risk</i> <b>12 (12)</b>
 * 		</li>
 * 		<li>
 * 			Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of
 * 				Risk</i> <b>3 (2)</b> 5-39
 * 		</li>
 * 		<li>
 * 			Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs <i>Journal of Financial
 * 				Markets</i> <b>1</b> 1-50
 * 		</li>
 * 		<li>
 * 			Chan, L. K. C., and J. Lakonishak (1995): The Behavior of Stock Prices around Institutional
 * 				Trades <i>Journal of Finance</i> <b>50</b> 1147-1174
 * 		</li>
 * 		<li>
 * 			Keim, D. B., and A. Madhavan (1997): Transaction Costs and Investment Style: An Inter-exchange
 * 				Analysis of Institutional Equity Trades <i>Journal of Financial Economics</i> <b>46</b>
 * 				265-292
 * 		</li>
 * 	</ul>
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

public class DiscreteAlmgrenChrissDrift extends org.drip.execution.nonadaptive.StaticOptimalSchemeDiscrete {

	private double KappaTau (
		final double dblKappaTildaSquared,
		final double dblTau)
	{
		double dblKappaTildaSquaredTauSquared = dblKappaTildaSquared * dblTau * dblTau;

		return java.lang.Math.log (0.5 * (2. + dblKappaTildaSquaredTauSquared + dblTau * java.lang.Math.sqrt
			(dblKappaTildaSquared * (dblKappaTildaSquaredTauSquared + 4.))));
	}

	/**
	 * Create the Standard DiscreteAlmgrenChrissDrift Instance
	 * 
	 * @param dblStartHoldings Trajectory Start Holdings
	 * @param dblFinishTime Trajectory Finish Time
	 * @param iNumInterval The Number of Fixed Intervals
	 * @param lpep Linear Impact Price Walk Parameters
	 * @param dblRiskAversion The Risk Aversion Parameter
	 * 
	 * @return The DiscreteAlmgrenChrissDrift Instance
	 */

	public static final DiscreteAlmgrenChrissDrift Standard (
		final double dblStartHoldings,
		final double dblFinishTime,
		final int iNumInterval,
		final org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep,
		final double dblRiskAversion)
	{
		try {
			return new DiscreteAlmgrenChrissDrift
				(org.drip.execution.strategy.DiscreteTradingTrajectoryControl.FixedInterval (new
					org.drip.execution.strategy.OrderSpecification (dblStartHoldings, dblFinishTime),
						iNumInterval), lpep, new org.drip.execution.risk.MeanVarianceObjectiveUtility
							(dblRiskAversion));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private DiscreteAlmgrenChrissDrift (
		final org.drip.execution.strategy.DiscreteTradingTrajectoryControl dttc,
		final org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep,
		final org.drip.execution.risk.MeanVarianceObjectiveUtility mvou)
		throws java.lang.Exception
	{
		super (dttc, lpep, mvou);
	}

	@Override public org.drip.execution.optimum.EfficientTradingTrajectoryDiscrete generate()
	{
		org.drip.execution.strategy.DiscreteTradingTrajectoryControl dttc = control();

		double[] adblTNode = dttc.executionTimeNodes();

		org.drip.execution.dynamics.LinearPermanentExpectationParameters lpep =
			(org.drip.execution.dynamics.LinearPermanentExpectationParameters) priceEvolutionParameters();

		org.drip.execution.impact.TransactionFunction tfTemporaryExpectation =
			lpep.temporaryExpectation().epochImpactFunction();

		if (!(tfTemporaryExpectation instanceof org.drip.execution.impact.TransactionFunctionLinear))
			return null;

		double dblEpochVolatility = java.lang.Double.NaN;
		org.drip.execution.impact.TransactionFunctionLinear tflTemporaryExpectation =
			(org.drip.execution.impact.TransactionFunctionLinear) tfTemporaryExpectation;

		double dblX = dttc.startHoldings();

		org.drip.execution.parameters.ArithmeticPriceDynamicsSettings apds =
			lpep.arithmeticPriceDynamicsSettings();

		double dblAlpha = apds.drift();

		double dblEta = tflTemporaryExpectation.slope();

		try {
			dblEpochVolatility = apds.epochVolatility();
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		double dblGamma = lpep.linearPermanentExpectation().epochLiquidityFunction().slope();

		int iNumNode = adblTNode.length;
		final double dblSigma = dblEpochVolatility;
		double dblTau = adblTNode[1] - adblTNode[0];
		double dblSigmaSquared = dblSigma * dblSigma;
		double[] adblHoldings = new double[iNumNode];
		double[] adblTradeList = new double[iNumNode - 1];
		double dblT = adblTNode[iNumNode - 1] - adblTNode[0];
		double dblEtaTilda = dblEta - 0.5 * dblGamma * dblTau;
		double[] adblHoldingsDriftAdjustment = new double[iNumNode];
		double[] adblTradeListDriftAdjustment = new double[iNumNode - 1];

		double dblLambdaSigmaSquared = ((org.drip.execution.risk.MeanVarianceObjectiveUtility)
			objectiveUtility()).riskAversion() * dblSigmaSquared;

		double dblResidualHolding = 0.5 * dblAlpha / dblLambdaSigmaSquared;
		double dblKappaTildaSquared = dblLambdaSigmaSquared / dblEtaTilda;

		double dblKappaTau = KappaTau (dblKappaTildaSquared, dblTau);

		double dblHalfKappaTau = 0.5 * dblKappaTau;
		double dblKappa = dblKappaTau / dblTau;
		double dblKappaT = dblKappa * dblT;

		double dblSinhKappaT = java.lang.Math.sinh (dblKappaT);

		double dblSinhHalfKappaTau = java.lang.Math.sinh (dblHalfKappaTau);

		double dblInverseSinhKappaT = 1. / dblSinhKappaT;
		double dblTrajectoryScaler = dblInverseSinhKappaT * dblX;
		double dblTradeListScaler = 2. * dblSinhHalfKappaTau * dblTrajectoryScaler;
		double dblTrajectoryAdjustmentScaler = dblInverseSinhKappaT * dblResidualHolding;
		double dblTradeListAdjustmentScaler = 2. * dblSinhHalfKappaTau * dblTrajectoryAdjustmentScaler;

		for (int i = 0; i < iNumNode; ++i) {
			adblHoldingsDriftAdjustment[i] = dblResidualHolding * (1. - dblInverseSinhKappaT *
				(java.lang.Math.sinh (dblKappa * (dblT - adblTNode[i])) + java.lang.Math.sinh (dblKappa *
					adblTNode[i])));

			adblHoldings[i] = dblTrajectoryScaler * java.lang.Math.sinh (dblKappa * (dblT - adblTNode[i])) +
				adblHoldingsDriftAdjustment[i];

			if (i < iNumNode - 1) {
				adblTradeListDriftAdjustment[i] = -1. * dblTradeListAdjustmentScaler * (java.lang.Math.cosh
					(dblKappa * dblTau * (0.5 + i)) - java.lang.Math.cosh (dblKappa * (dblT - dblTau * (0.5 +
						i))));

				adblTradeList[i] = -1. * dblTradeListScaler * java.lang.Math.cosh (dblKappa * (dblT - dblTau
					* (0.5 + i))) + adblTradeListDriftAdjustment[i];
			}
		}

		try {
			org.drip.measure.gaussian.R1UnivariateNormal r1un = (new
				org.drip.execution.capture.TrajectoryShortfallEstimator (new
					org.drip.execution.strategy.DiscreteTradingTrajectory (adblTNode, adblHoldings,
						adblTradeList))).totalCostDistributionSynopsis (lpep);

			return null == r1un ? null : new org.drip.execution.optimum.AlmgrenChrissDriftDiscrete
				(adblTNode, adblHoldings, adblTradeList, adblHoldingsDriftAdjustment,
					adblTradeListDriftAdjustment, java.lang.Math.sqrt (dblKappaTildaSquared), dblKappa,
						dblResidualHolding, dblAlpha * dblResidualHolding * dblT * (1. - (dblTau *
							java.lang.Math.tanh (0.5 * dblKappa * dblT) / (dblT * java.lang.Math.tanh
								(dblHalfKappaTau)))), r1un.mean(), r1un.variance(), dblEpochVolatility * dblX
									/ (dblT * dblEpochVolatility * java.lang.Math.sqrt (dblT)));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
