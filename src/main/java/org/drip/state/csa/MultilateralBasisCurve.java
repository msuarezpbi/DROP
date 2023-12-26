
package org.drip.state.csa;

import org.drip.analytics.date.JulianDate;
import org.drip.state.discount.MergedDiscountForwardCurve;

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
 * <i>MultilateralBasisCurve</i> implements the CSA Cash Rate Curve as a Basis over an Overnight Curve. It
 *  exports the following Functionality:
 *
 *  <ul>
 *  	<li>Retrieve the Overnight Curve</li>
 *  	<li>Retrieve the Basis to the Overnight Curve</li>
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
 *		<tr><td><b>Project</b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state/README.md">Latent State Inference and Creation Utilities</a></td></tr>
 *		<tr><td><b>Package</b></td> <td><a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state/csa/README.md">Credit Support Annex Latent State</a></td></tr>
 *  </table>
 *
 * @author Lakshmi Krishnamurthy
 */

public class MultilateralBasisCurve implements CashFlowEstimator
{
	private static final int NUM_DF_QUADRATURES = 5;

	private double _basis = Double.NaN;
	private MergedDiscountForwardCurve _overnightMergedDiscountForwardCurve = null;

	/**
	 * Retrieve the Overnight Curve
	 * 
	 * @return The Overnight Curve
	 */

	public MergedDiscountForwardCurve overnightCurve()
	{
		return _overnightMergedDiscountForwardCurve;
	}

	/**
	 * Retrieve the Basis to the Overnight Curve
	 * 
	 * @return The Basis to the Overnight Curve
	 */

	public double basis()
	{
		return _basis;
	}

	@Override public JulianDate epoch()
	{
		return _overnightMergedDiscountForwardCurve.epoch();
	}

	@Override public double df (
		final int date)
		throws Exception
	{
		int iEpochDate = epoch().julian();

		if (iEpochDate >= date) {
			throw new Exception ("MultilateralBasisCurve::df => Invalid Inputs");
		}

		return _overnightMergedDiscountForwardCurve.df (date) *
			Math.exp (_basis * (iEpochDate - date) / 365.25);
	}

	@Override public double df (
		final JulianDate date)
		throws Exception
	{
		if (null == date) {
			throw new Exception ("MultilateralBasisCurve::df => Invalid Inputs");
		}

		return df (date.julian());
	}

	@Override public double df (
		final String tenor)
		throws Exception
	{
		return df (epoch().addTenor (tenor));
	}

	@Override public double effectiveDF (
		final int date1,
		final int date2)
		throws Exception
	{
		if (epoch().julian() > date1 || date1 >= date2) {
			throw new Exception ("MultilateralFlatForwardCurve::effectiveDF => Invalid Inputs");
		}

		int quadratureCount = 0;
		double effectiveDiscountFactor = 0.;
		int quadratureWidth = (date2 - date1) / NUM_DF_QUADRATURES;

		if (0 == quadratureWidth) {
			quadratureWidth = 1;
		}

		for (int date = date1; date <= date2; date += quadratureWidth) {
			++quadratureCount;

			effectiveDiscountFactor += (df (date) + df (date + quadratureWidth));
		}

		return effectiveDiscountFactor / (2. * quadratureCount);
	}

	@Override public double effectiveDF (
		final JulianDate date1,
		final JulianDate date2)
		throws Exception
	{
		if (null == date1 || null == date2) {
			throw new Exception ("MultilateralFlatForwardCurve::effectiveDF => Invalid Inputs");
		}

		return effectiveDF (date1.julian(), date2.julian());
	}

	@Override public double effectiveDF (
		final String tenor1,
		final String tenor2)
		throws Exception
	{
		JulianDate epochDate = epoch();

		return effectiveDF (epochDate.addTenor (tenor1), epochDate.addTenor (tenor2));
	}

	@Override public double rate (
		final int date)
		throws Exception
	{
		int iEpochDate = epoch().julian();

		if (iEpochDate >= date) {
			throw new Exception ("MultilateralFlatForwardCurve::rate => Invalid Inputs");
		}

		return 365.25 * Math.log (df (iEpochDate) / df (date)) / (iEpochDate - date);
	}

	@Override public double rate (
		final JulianDate date)
		throws Exception
	{
		if (null == date) {
			throw new Exception ("MultilateralFlatForwardCurve::rate => Invalid Inputs");
		}

		return rate (date.julian());
	}

	@Override public double rate (
		final String tenor)
		throws Exception
	{
		return rate (epoch().addTenor (tenor));
	}

	@Override public double rate (
		final int date1,
		final int date2)
		throws Exception
	{
		int epochDate = epoch().julian();

		if (epochDate > date1 || date1 >= date2) {
			throw new Exception ("MultilateralFlatForwardCurve::rate => Invalid Inputs");
		}

		return 365.25 * Math.log (df (date1) / df (date2)) / (date2 - date1);
	}

	@Override public double rate (
		final JulianDate date1,
		final JulianDate date2)
		throws Exception
	{
		if (null == date1 || null == date2) {
			throw new Exception ("MultilateralFlatForwardCurve::rate => Invalid Inputs");
		}

		return rate (date1.julian(), date2.julian());
	}

	@Override public double rate (
		final String tenor1,
		final String tenor2)
		throws Exception
	{
		JulianDate epochDate = epoch();

		return rate (epochDate.addTenor (tenor1), epochDate.addTenor (tenor2));
	}
}
