
package org.drip.oms.exchange;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2023 Lakshmi Krishnamurthy
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
 * <i>VenueSettings</i> maintains the Settings that Relate to a Venue. The References are:
 *  
 * 	<br><br>
 *  <ul>
 * 		<li>
 * 			Chen, J. (2021): Time in Force: Definition, Types, and Examples
 * 				https://www.investopedia.com/terms/t/timeinforce.asp
 * 		</li>
 * 		<li>
 * 			Cont, R., and A. Kukanov (2017): Optimal Order Placement in Limit Order Markets <i>Quantitative
 * 				Finance</i> <b>17 (1)</b> 21-39
 * 		</li>
 * 		<li>
 * 			Vassilis, P. (2005b): Slow and Fast Markets <i>Journal of Economics and Business</i> <b>57
 * 				(6)</b> 576-593
 * 		</li>
 * 		<li>
 * 			Weiss, D. (2006): <i>After the Trade is Made: Processing Securities Transactions</i> <b>Portfolio
 * 				Publishing</b> London UK
 * 		</li>
 * 		<li>
 * 			Wikipedia (2023): Central Limit Order Book
 * 				https://en.wikipedia.org/wiki/Central_limit_order_book
 * 		</li>
 *  </ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/oms/README.md">R<sup>d</sup> Order Specification, Handling, and Management</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/oms/exchange/README.md">Implementation of Venue Order Handling</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class VenueSettings
{
	private String _code = "";
	private String _jurisdiction = "";
	private boolean _isInverted = false;
	private String _localIdentifier = "";
	private PricingRebateFunction _pricingRebateFunction = null;

	/**
	 * Generate a Regular Venue
	 * 
	 * @param code Venue Code
	 * @param pricingRebateFunction Pricing Rebate Function
	 * 
	 * @return Regular Venue
	 */

	public static final VenueSettings Regular (
		final String code,
		final PricingRebateFunction pricingRebateFunction)
	{
		try
		{
			return new VenueSettings (
				code,
				pricingRebateFunction,
				false
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generate an Inverted Venue
	 * 
	 * @param code Venue Code
	 * @param pricingRebateFunction Pricing Rebate Function
	 * 
	 * @return Inverted Venue
	 */

	public static final VenueSettings Inverted (
		final String code,
		final PricingRebateFunction pricingRebateFunction)
	{
		try
		{
			return new VenueSettings (
				code,
				pricingRebateFunction,
				true
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * VenueSettings Constructor
	 * 
	 * @param code Venue Code
	 * @param pricingRebateFunction Pricing Rebate Function
	 * @param isInverted TRUE - The Venue is Inverted
	 * 
	 * @throws Exception Thrown if the Inputs are Invalid
	 */

	public VenueSettings (
		final String code,
		final PricingRebateFunction pricingRebateFunction,
		final boolean isInverted)
		throws Exception
	{
		if (null == (_code = code) || code.isEmpty() ||
			null == (_pricingRebateFunction = pricingRebateFunction)
		)
		{
			throw new Exception (
				"VenueSettings Constructor => Invalid Inputs"
			);
		}

		_isInverted = isInverted;
	}

	/**
	 * Retrieve the Venue Jurisdiction
	 * 
	 * @return Venue Jurisdiction
	 */

	public String jurisdiction()
	{
		return _jurisdiction;
	}

	/**
	 * Retrieve the Venue Local Identifier
	 * 
	 * @return Venue Local Identifier
	 */

	public String localIdentifier()
	{
		return _localIdentifier;
	}

	/**
	 * Retrieve the Venue Code
	 * 
	 * @return Venue Code
	 */

	public String code()
	{
		return _code;
	}

	/**
	 * Indicate if the Venue is Inverted
	 * 
	 * @return TRUE - The Venue is Inverted
	 */

	public boolean isInverted()
	{
		return _isInverted;
	}

	/**
	 * Retrieve the Pricing Rebate Function
	 * 
	 * @return The Pricing Rebate Function
	 */

	public PricingRebateFunction pricingRebateFunction()
	{
		return _pricingRebateFunction;
	}

	/**
	 * Estimate Liquidity Posting Fee for the specified Ticker at the Venue at the Price/Size. Positive
	 *  number indicates fees paid; negative indicates rebates given.
	 * 
	 * @param ticker Ticker
	 * @param price Price
	 * @param size Size
	 * 
	 * @return Fee for Liquidity Posting
	 * 
	 * @throws Exception Thrown if the Liquidity Posting Fee cannot be calculated
	 */

	public double postFee (
		final String ticker,
		final double price,
		final double size)
		throws Exception
	{
		return _pricingRebateFunction.makerFee (
			ticker,
			price,
			size
		);
	}

	/**
	 * Estimate Liquidity Sweeping Fee for the specified Ticker at the Venue at the Price/Size. Positive
	 *  number indicates fees paid; negative indicates rebates given.
	 * 
	 * @param ticker Ticker
	 * @param price Price
	 * @param size Size
	 * 
	 * @return Fee for Liquidity Sweeping
	 * 
	 * @throws Exception Thrown if the Liquidity Sweeping Fee cannot be calculated
	 */

	public double sweepFee (
		final String ticker,
		final double price,
		final double size)
		throws Exception
	{
		return _pricingRebateFunction.makerFee (
			ticker,
			price,
			size
		);
	}
}
