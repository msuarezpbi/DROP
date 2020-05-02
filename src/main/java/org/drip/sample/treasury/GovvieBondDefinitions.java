
package org.drip.sample.treasury;

import org.drip.market.issue.*;
import org.drip.service.env.EnvManager;

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	and computational support.
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
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
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
 * <i>GovvieBondDefinitions</i> contains the Details of the Standard Built-in Govvie Bonds.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ProductCore.md">Product Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">DROP API Construction and Usage</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/treasury/README.md">G20 Govvie Bond Definitions YAS</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class GovvieBondDefinitions {

	private static final void DisplayDetails (
		final String strTreasuryCode)
	{
		TreasurySetting ts = TreasurySettingContainer.TreasurySetting (strTreasuryCode);

		System.out.println (
			"\t| " + ts.code() +
			" | " + ts.currency() +
			" | " + ts.frequency() +
			" | " + ts.dayCount() +
			" | " + ts.calendar() + " ||"
		);
	}

	private static final void DefaultTreasuryCode (
		final String strCurrency)
	{
		System.out.println ("\t| " + strCurrency + " => " + TreasurySettingContainer.CurrencyBenchmarkCode (strCurrency) + " ||");
	}

	public static final void main (
		final String[] args)
		throws Exception
	{
		EnvManager.InitEnv ("");

		System.out.println ("\n\t|-------------------------------------||");

		System.out.println ("\t| BUILT-IN GOVVIE BOND STATIC DETAILS ||");

		System.out.println ("\t| -------- ------ ---- ------ ------- ||");

		System.out.println ("\t|                                     ||");

		System.out.println ("\t|      L -> R                         ||");

		System.out.println ("\t|            Treasury Code            ||");

		System.out.println ("\t|            Currency                 ||");

		System.out.println ("\t|            Frequency                ||");

		System.out.println ("\t|            Day Count                ||");

		System.out.println ("\t|            Calendar                 ||");

		System.out.println ("\t|                                     ||");

		System.out.println ("\t|-------------------------------------||");

		DisplayDetails ("AGB");

		DisplayDetails ("BTPS");

		DisplayDetails ("CAN");

		DisplayDetails ("DBR");

		DisplayDetails ("DGB");

		DisplayDetails ("FRTR");

		DisplayDetails ("GGB");

		DisplayDetails ("GILT");

		DisplayDetails ("GSWISS");

		DisplayDetails ("JGB");

		DisplayDetails ("MBONO");

		DisplayDetails ("NGB");

		DisplayDetails ("NZGB");

		DisplayDetails ("SGB");

		DisplayDetails ("SPGB");

		DisplayDetails ("UST");

		System.out.println ("\t|-------------------------------------||");

		System.out.println ("\n\n\t|------------||");

		System.out.println ("\t|            ||");

		System.out.println ("\t|   GOVVIE   ||");

		System.out.println ("\t|    BOND    ||");

		System.out.println ("\t|  CURRENCY  ||");

		System.out.println ("\t|  DEFAULTS  ||");

		System.out.println ("\t|            ||");

		System.out.println ("\t|------------||");

		DefaultTreasuryCode ("AUD");

		DefaultTreasuryCode ("CAD");

		DefaultTreasuryCode ("CHF");

		DefaultTreasuryCode ("EUR");

		DefaultTreasuryCode ("GBP");

		DefaultTreasuryCode ("JPY");

		DefaultTreasuryCode ("MXN");

		DefaultTreasuryCode ("NOK");

		DefaultTreasuryCode ("SEK");

		DefaultTreasuryCode ("USD");

		DefaultTreasuryCode ("AUD");

		System.out.println ("\t|------------||");

		EnvManager.TerminateEnv();
	}
}
