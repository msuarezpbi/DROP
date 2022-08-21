
package org.drip.simm.rates;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.drip.simm.common.DeltaVegaThreshold;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2022 Lakshmi Krishnamurthy
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
 * <i>IRThresholdContainer24</i> holds the ISDA SIMM 2.4 Interest Rate Thresholds - the Currency Risk Groups,
 * and the Delta/Vega Limits defined for the Concentration Thresholds. The References are:
 * 
 * <br><br>
 *  <ul>
 *  	<li>
 *  		Andersen, L. B. G., M. Pykhtin, and A. Sokol (2017): Credit Exposure in the Presence of Initial
 *  			Margin https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2806156 <b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		Albanese, C., S. Caenazzo, and O. Frankel (2017): Regression Sensitivities for Initial Margin
 *  			Calculations https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2763488 <b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		Anfuso, F., D. Aziz, P. Giltinan, and K. Loukopoulus (2017): A Sound Modeling and Back-testing
 *  			Framework for Forecasting Initial Margin Requirements
 *  				https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2716279 <b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		Caspers, P., P. Giltinan, R. Lichters, and N. Nowaczyk (2017): Forecasting Initial Margin
 *  			Requirements - A Model Evaluation https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2911167
 *  				<b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		International Swaps and Derivatives Association (2021): SIMM v2.4 Methodology
 *  			https://www.isda.org/a/CeggE/ISDA-SIMM-v2.4-PUBLIC.pdf
 *  	</li>
 *  </ul>
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/MarginAnalyticsLibrary.md">Initial and Variation Margin Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/simm/README.md">Initial Margin Analytics based on ISDA SIMM and its Variants</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/simm/rates/README.md">SIMM IR Risk Factor Settings</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IRThresholdContainer24
{
	private static final Map<String, Integer> s_CurrencyThresholdMap = new HashMap<String, Integer>();

	private static final Map<Integer, IRThreshold> s_ThresholdMap = new TreeMap<Integer, IRThreshold>();

	private static final boolean SetupCurrencyMap()
	{
		s_CurrencyThresholdMap.put (
			"USD",
			2
		);

		s_CurrencyThresholdMap.put (
			"EUR",
			2
		);

		s_CurrencyThresholdMap.put (
			"GBP",
			2
		);

		s_CurrencyThresholdMap.put (
			"AUD",
			3
		);

		s_CurrencyThresholdMap.put (
			"CAD",
			3
		);

		s_CurrencyThresholdMap.put (
			"CHF",
			3
		);

		s_CurrencyThresholdMap.put (
			"DKK",
			3
		);

		s_CurrencyThresholdMap.put (
			"HKD",
			3
		);

		s_CurrencyThresholdMap.put (
			"KRW",
			3
		);

		s_CurrencyThresholdMap.put (
			"NOK",
			3
		);

		s_CurrencyThresholdMap.put (
			"NZD",
			3
		);

		s_CurrencyThresholdMap.put (
			"SEK",
			3
		);

		s_CurrencyThresholdMap.put (
			"SGD",
			3
		);

		s_CurrencyThresholdMap.put (
			"TWD",
			3
		);

		s_CurrencyThresholdMap.put (
			"JPY",
			4
		);

		return true;
	}

	/**
	 * Initialize the Container
	 * 
	 * @return TRUE - The Container successfully Initialized
	 */

	public static final boolean Init()
	{
		try
		{
			s_ThresholdMap.put (
				1,
				new IRThreshold (
					new CurrencyRiskGroup (
						IRSystemics.VOLATILITY_TYPE_HIGH,
						IRSystemics.TRADE_FREQUENCY_LESS_WELL_TRADED,
						new String[]
						{
							"Other"
						}
					),
					new DeltaVegaThreshold (
						22.,
						83.
					)
				)
			);

			s_ThresholdMap.put (
				2,
				new IRThreshold (
					new CurrencyRiskGroup (
						IRSystemics.VOLATILITY_TYPE_REGULAR,
						IRSystemics.TRADE_FREQUENCY_WELL_TRADED,
						new String[]
						{
							"USD",
							"EUR",
							"GBP"
						}
					),
					new DeltaVegaThreshold (
						240.,
						2600.
					)
				)
			);

			s_ThresholdMap.put (
				3,
				new IRThreshold (
					new CurrencyRiskGroup (
						IRSystemics.VOLATILITY_TYPE_REGULAR,
						IRSystemics.TRADE_FREQUENCY_LESS_WELL_TRADED,
						new String[]
						{
							"AUD",
							"CAD",
							"CHF",
							"DKK",
							"HKD",
							"KRW",
							"NOK",
							"NZD",
							"SEK",
							"SGD",
							"TWD"
						}
					),
					new DeltaVegaThreshold (
						44.,
						270.
					)
				)
			);

			s_ThresholdMap.put (
				4,
				new IRThreshold (
					new CurrencyRiskGroup (
						IRSystemics.VOLATILITY_TYPE_LOW,
						IRSystemics.TRADE_FREQUENCY_WELL_TRADED,
						new String[]
						{
							"JPY"
						}
					),
					new DeltaVegaThreshold (
						120.,
						980.
					)
				)
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}

		return SetupCurrencyMap();
	}

	/**
	 * Retrieve the Interest Rate Threshold Container Bucket Index Set
	 * 
	 * @return The Interest Rate Threshold Container Bucket Index Set
	 */

	public static final Set<Integer> IndexSet()
	{
		return s_ThresholdMap.keySet();
	}

	/**
	 * Retrieve the Interest Rate Threshold Container Currency Set
	 * 
	 * @return The Interest Rate Threshold Container Currency Set
	 */

	public static final Set<String> CurrencySet()
	{
		return s_CurrencyThresholdMap.keySet();
	}

	/**
	 * Indicate if the Entry denoted by the Number is available as an Interest Rate Threshold
	 * 
	 * @param groupNumber The Group Number
	 * 
	 * @return TRUE - The Entry denoted by the Number is available as an Interest Rate Threshold
	 */

	public static final boolean ContainsThreshold (
		final int groupNumber)
	{
		return s_ThresholdMap.containsKey (
			groupNumber
		);
	}

	/**
	 * Indicate if the Currency is available as an Interest Rate Threshold
	 * 
	 * @param currency The Currency
	 * 
	 * @return TRUE - The Currency is available as an Interest Rate Threshold
	 */

	public static final boolean ContainsThreshold (
		final String currency)
	{
		return s_CurrencyThresholdMap.containsKey (
			currency
		);
	}

	/**
	 * Retrieve the Interest Rate Threshold denoted by the Currency
	 * 
	 * @param currency The Currency
	 * 
	 * @return The Interest Rate Threshold
	 */

	public static final IRThreshold Threshold (
		final String currency)
	{
		return ContainsThreshold (
			currency
		) ? s_ThresholdMap.get (
			s_CurrencyThresholdMap.get (
				currency
			)
		) : s_ThresholdMap.get (
			1
		);
	}

	/**
	 * Retrieve the Interest Rate Threshold denoted by the Group Number
	 * 
	 * @param groupNumber The Group Number
	 * 
	 * @return The Interest Rate Threshold
	 */

	public static final IRThreshold Threshold (
		final int groupNumber)
	{
		return ContainsThreshold (
			groupNumber
		) ? s_ThresholdMap.get (
			groupNumber
		) : null;
	}

	/**
	 * Retrieve the Currency Threshold Map
	 * 
	 * @return The Currency Threshold Map
	 */

	public static final Map<String, Integer> CurrencyThresholdMap()
	{
		return s_CurrencyThresholdMap;
	}

	/**
	 * Retrieve the Interest Rate Threshold Map
	 * 
	 * @return The Interest Rate Threshold Map
	 */

	public static final Map<Integer, IRThreshold> ThresholdMap()
	{
		return s_ThresholdMap;
	}
}
