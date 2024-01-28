
package org.drip.oms.indifference;

import org.drip.function.definition.R1ToR1;
import org.drip.function.r1tor1solver.FixedPointFinderOutput;
import org.drip.function.r1tor1solver.FixedPointFinderZheng;
import org.drip.measure.continuous.R1Univariate;
import org.drip.numerical.common.NumberUtil;
import org.drip.numerical.integration.R1ToR1Integrator;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2024 Lakshmi Krishnamurthy
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
 * <i>ReservationPricer</i> implements the Expectation of the Utility Function using Units of Underlying
 *  Asset. The References are:
 *  
 * 	<br><br>
 *  <ul>
 * 		<li>
 * 			Birge, J. R. (2008): <i>Financial Engineering</i> <b>Elsevier</b> Amsterdam Netherlands
 * 		</li>
 * 		<li>
 * 			Carmona, R. (2009): <i>Indifference Pricing: Theory and Applications</i> <b>Princeton
 * 				University Press</b> Princeton NJ
 * 		</li>
 * 		<li>
 * 			Vassilis, P. (2005): Slow and Fast Markets <i>Journal of Economics and Business</i> <b>57
 * 				(6)</b> 576-593
 * 		</li>
 * 		<li>
 * 			Weiss, D. (2006): <i>After the Trade is Made: Processing Securities Transactions</i> <b>Portfolio
 * 				Publishing</b> London UK
 * 		</li>
 * 		<li>
 * 			Wikipedia (2021): Indifference Price https://en.wikipedia.org/wiki/Indifference_price
 * 		</li>
 *  </ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/oms/README.md">R<sup>d</sup> Order Specification, Handling, and Management</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/oms/indifference/README.md">Reservation Price Good-deal Bounds</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ReservationPricer2
{
	private R1ToR1 _payoffFunction = null;
	private R1ToR1 _privateValuationObjectiveFunction = null;

	/**
	 * ReservationPricer Constructor
	 * 
	 * @param privateValuationObjectiveFunction Private Valuation Utility Function
	 * @param payoffFunction Payoff Function
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public ReservationPricer2 (
		final R1ToR1 privateValuationObjectiveFunction,
		final R1ToR1 payoffFunction)
		throws Exception
	{
		if (null == (_privateValuationObjectiveFunction = privateValuationObjectiveFunction) ||
			null == (_payoffFunction = payoffFunction)) {
			throw new Exception ("ReservationPricer Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Private Valuation Utility Function
	 * 
	 * @return The Private Valuation Utility Function
	 */

	public R1ToR1 privateValuationObjectiveFunction()
	{
		return _privateValuationObjectiveFunction;
	}

	/**
	 * Retrieve the Payoff Function
	 * 
	 * @return The Payoff Function
	 */

	public R1ToR1 payoffFunction()
	{
		return _payoffFunction;
	}

	/**
	 * Compute the Claims Adjusted Price
	 * 
	 * @param optimalUtilityExpectationFunction The Optimal Utility Expectation Function
	 * @param indifferencePrice Indifference Price
	 * 
	 * @return Claims Adjusted Price
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public double claimsAdjustedPrice (
		final R1ToR1 optimalUtilityExpectationFunction,
		final double indifferencePrice)
		throws Exception
	{
		if (null == optimalUtilityExpectationFunction) {
			throw new Exception ("ReservationPricer::claimsAdjustedPrice => Cannot find Root");
		}

		FixedPointFinderOutput fixedPointFinderOutput = new FixedPointFinderZheng (
			indifferencePrice,
			new R1ToR1 (null) {
				@Override public final double evaluate (
					final double claimsAdjustedPrice)
					throws Exception
				{
					return optimalUtilityExpectationFunction.evaluate (claimsAdjustedPrice);
				}
			},
			false
		).findRoot();

		if (null == fixedPointFinderOutput) {
			throw new Exception ("ReservationPricer::claimsAdjustedEndowmentPortfolio => Cannot find Root");
		}

		return fixedPointFinderOutput.getRoot();
	}

	/**
	 * Compute the Claims Unadjusted Utility Value
	 * 
	 * @param risklessUnitsFunction Riskless Units Function
	 * @param terminalRisklessPrice Terminal Riskless Price
	 * @param terminalUnderlierPrice Terminal Underlier Price
	 * @param underlierUnits Underlier Units
	 * 
	 * @return Baseline Utility Value
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public double claimsUnadjustedUtilityValue (
		final R1ToR1 risklessUnitsFunction,
		final double terminalRisklessPrice,
		final double terminalUnderlierPrice,
		final double underlierUnits)
		throws Exception
	{
		return _privateValuationObjectiveFunction.evaluate (
			terminalRisklessPrice * risklessUnitsFunction.evaluate (underlierUnits) +
				terminalUnderlierPrice * underlierUnits
		);
	}

	/**
	 * Compute the Claims Adjusted Utility Value
	 * 
	 * @param risklessUnitsFunction Riskless Units Function
	 * @param terminalRisklessPrice Terminal Riskless Price
	 * @param terminalUnderlierPrice Terminal Underlier Price
	 * @param underlierUnits Underlier Units
	 * @param claimUnits Claims Units
	 * 
	 * @return Claims Adjusted Utility Value
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public double claimsAdjustedUtilityValue (
		final R1ToR1 risklessUnitsFunction,
		final double terminalRisklessPrice,
		final double terminalUnderlierPrice,
		final double underlierUnits,
		final double claimUnits)
		throws Exception
	{
		return _privateValuationObjectiveFunction.evaluate (
			terminalRisklessPrice * risklessUnitsFunction.evaluate (underlierUnits) +
				terminalUnderlierPrice * underlierUnits +
				claimUnits * _payoffFunction.evaluate (terminalUnderlierPrice)
		);
	}

	/**
	 * Compute the Indifference Utility Value
	 * 
	 * @param risklessUnitsFunction Riskless Units Function
	 * @param terminalUnderlierDistribution Terminal Underlier Distribution
	 * @param terminalRisklessPrice Terminal Riskless Price
	 * @param underlierUnits Underlier Units
	 * 
	 * @return The Indifference Utility Value
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public double indifferenceUtilityValue (
		final R1ToR1 risklessUnitsFunction,
		final R1Univariate terminalUnderlierDistribution,
		final double terminalRisklessPrice,
		final double underlierUnits)
		throws Exception
	{
		if (null == risklessUnitsFunction ||
			null == terminalUnderlierDistribution ||
			!NumberUtil.IsValid (terminalRisklessPrice) ||
			!NumberUtil.IsValid (underlierUnits)) {
			throw new Exception (
				"ReservationPricer::indifferenceUtilityValue => Invalid Terminal Distribution"
			);
		}

		double[] terminalUnderlierSupportArray = terminalUnderlierDistribution.support();

		return R1ToR1Integrator.Boole (
			new R1ToR1 (null) {
				@Override public double evaluate (
					double terminalUnderlierPrice)
					throws Exception
				{
					return claimsUnadjustedUtilityValue (
						risklessUnitsFunction,
						terminalRisklessPrice,
						terminalUnderlierPrice,
						underlierUnits
					) * terminalUnderlierDistribution.density (terminalUnderlierPrice);
				}
			},
			terminalUnderlierSupportArray[0],
			terminalUnderlierSupportArray[1]
		);
	}

	/**
	 * Compute Claims Adjusted Utility Value
	 * 
	 * @param risklessUnitsFunction Riskless Units Function
	 * @param terminalUnderlierDistribution Terminal Underlier Distribution
	 * @param terminalRisklessPrice Terminal Riskless Price
	 * @param underlierUnits Underlier Units
	 * @param claimUnits Claims Units
	 * 
	 * @return Claims Adjusted Utility Value
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public double claimsAdjustedUtilityValue (
		final R1ToR1 risklessUnitsFunction,
		final R1Univariate terminalUnderlierDistribution,
		final double terminalRisklessPrice,
		final double underlierUnits,
		final double claimUnits)
		throws Exception
	{
		if (null == risklessUnitsFunction ||
			null == terminalUnderlierDistribution ||
			!NumberUtil.IsValid (terminalRisklessPrice) ||
			!NumberUtil.IsValid (underlierUnits) ||
			!NumberUtil.IsValid (claimUnits)) {
			throw new Exception (
				"ReservationPricer::claimsAdjustedUtilityValue => Invalid Terminal Distribution"
			);
		}

		double[] terminalUnderlierSupportArray = terminalUnderlierDistribution.support();

		return R1ToR1Integrator.Boole (
			new R1ToR1 (null) {
				@Override public double evaluate (
					double terminalUnderlierPrice)
					throws Exception
				{
					return claimsAdjustedUtilityValue (
						risklessUnitsFunction,
						terminalRisklessPrice,
						terminalUnderlierPrice,
						underlierUnits,
						claimUnits
					) * terminalUnderlierDistribution.density (terminalUnderlierPrice);
				}
			},
			terminalUnderlierSupportArray[0],
			terminalUnderlierSupportArray[1]
		);
	}
}
