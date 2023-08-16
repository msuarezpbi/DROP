
package org.drip.sample.scaledexponential;

import org.drip.function.definition.R1ToR1;
import org.drip.measure.exponential.R1ScaledDistribution;
import org.drip.service.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.specialfunction.definition.ScaledExponentialEstimator;
import org.drip.specialfunction.gamma.EulerIntegralSecondKind;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2022 Lakshmi Krishnamurthy
 * Copyright (C) 2021 Lakshmi Krishnamurthy
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
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
 * <i>KohlrauschPDFEstimate</i> illustrates the Construction and Usage of the Kohlrausch PDF Estimate
 * 	Function. The References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Gradshteyn, I. S., I. M. Ryzhik, Y. V. Geronimus, M. Y. Tseytlin, and A. Jeffrey (2015):
 * 				<i>Tables of Integrals, Series, and Products</i> <b>Academic Press</b>
 * 		</li>
 * 		<li>
 * 			Hilfer, J. (2002): H-function Representations for Stretched Exponential Relaxation and non-Debye
 * 				Susceptibilities in Glassy Systems <i>Physical Review E</i> <b>65 (6)</b> 061510
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Stretched Exponential Function
 * 				https://en.wikipedia.org/wiki/Stretched_exponential_function
 * 		</li>
 * 		<li>
 * 			Wuttke, J. (2012): Laplace-Fourier Transform of the Stretched Exponential Function: Analytic
 * 				Error-Bounds, Double Exponential Transform, and Open Source Implementation <i>libkw</i>
 * 				<i>Algorithm</i> <b>5 (4)</b> 604-628
 * 		</li>
 * 		<li>
 * 			Zorn, R. (2002): Logarithmic Moments of Relaxation Time Distributions <i>Journal of Chemical
 * 				Physics</i> <b>116 (8)</b> 3204-3209
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FunctionAnalysisLibrary.md">Function Analysis Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">DROP API Construction and Usage</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/scaledexponential/README.md">Scaled Exponential Function - Estimates/Moments</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class KohlrauschPDFEstimate
{

	/**
	 * Entry Point
	 * 
	 * @param argumentArray Command Line Argument Array
	 * 
	 * @throws Exception Thrown on Error/Exception Situation
	 */

	public static final void main (
		final String[] argumentArray)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double[] tArray =
		{
			 0.1,
			 0.2,
			 0.3,
			 0.4,
			 0.5,
			 1.0,
			 2.0,
			 5.0,
			10.0,
		};
		double[] betaArray =
		{
			 0.1,
			 0.2,
			 0.3,
			 0.4,
			 0.5,
			 0.6,
			 0.7,
			 0.8,
			 0.9,
		};
		double[] pValueArray =
		{
			 0.05,
			 0.10,
			 0.15,
			 0.20,
			 0.25,
			 0.30,
			 0.35,
			 0.40,
			 0.45,
		};

		R1ToR1 gammaEstimator = new EulerIntegralSecondKind (null);

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                             PROBABILITY DENSITY FUNCTION ESTIMATE                                            ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|        L -> R:                                                                                                               ||");

		System.out.println ("\t|                - Beta                                                                                                        ||");

		System.out.println ("\t|                - Values for different t                                                                                      ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		for (double beta : betaArray)
		{
			R1ScaledDistribution r1UnivariateScaledExponential = new R1ScaledDistribution (
				new ScaledExponentialEstimator (
					beta,
					1.
				),
				gammaEstimator
			);

			String display = "\t| [" + FormatUtil.FormatDouble (beta, 1, 1, 1., false) + "] => ";

			for (double t : tArray)
			{
				display = display + " " + FormatUtil.FormatDouble (
					r1UnivariateScaledExponential.density (t), 1, 8, 1., false
				) + " |";
			}

			System.out.println (display + "|");
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println();

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                           CUMULATIVE DISTRIBUTION FUNCTION ESTIMATE                                          ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|        L -> R:                                                                                                               ||");

		System.out.println ("\t|                - Beta                                                                                                        ||");

		System.out.println ("\t|                - Values for different t                                                                                      ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		for (double beta : betaArray)
		{
			R1ScaledDistribution r1UnivariateScaledExponential = new R1ScaledDistribution (
				new ScaledExponentialEstimator (
					beta,
					1.
				),
				gammaEstimator
			);

			String display = "\t| [" + FormatUtil.FormatDouble (beta, 1, 1, 1., false) + "] => ";

			for (double t : tArray)
			{
				display = display + " " + FormatUtil.FormatDouble (
					r1UnivariateScaledExponential.cumulative (t), 1, 8, 1., false
				) + " |";
			}

			System.out.println (display + "|");
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println();

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                       INVERSE CUMULATIVE DISTRIBUTION FUNCTION ESTIMATE                                      ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|        L -> R:                                                                                                               ||");

		System.out.println ("\t|                - Beta                                                                                                        ||");

		System.out.println ("\t|                - Values for different p                                                                                      ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		for (double beta : betaArray)
		{
			R1ScaledDistribution r1UnivariateScaledExponential = new R1ScaledDistribution (
				new ScaledExponentialEstimator (
					beta,
					1.
				),
				gammaEstimator
			);

			String display = "\t| [" + FormatUtil.FormatDouble (beta, 1, 1, 1., false) + "] => ";

			for (double p : pValueArray)
			{
				display = display + " " + FormatUtil.FormatDouble (
					r1UnivariateScaledExponential.invCumulative (p), 1, 8, 1., false
				) + " |";
			}

			System.out.println (display + "|");
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------||");

		EnvManager.TerminateEnv();
	}
}
