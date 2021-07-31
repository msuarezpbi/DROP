
package org.drip.sample.distancetest;

import org.drip.measure.continuous.R1UnivariateExponential;
import org.drip.service.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.validation.distance.GapTestOutcome;
import org.drip.validation.distance.GapTestSetting;
import org.drip.validation.distance.GapLossWeightFunction;
import org.drip.validation.evidence.Ensemble;
import org.drip.validation.evidence.Sample;
import org.drip.validation.evidence.TestStatisticEvaluator;
import org.drip.validation.hypothesis.ProbabilityIntegralTransformTest;

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
 * <i>ExponentialCramersVonMisesGapDiscriminant</i> demonstrates the Generation of the Sample Distance
 * Discriminant Metrics for Different Ensemble Hypotheses.
 * 
 *  <br><br>
 *  <ul>
 *  	<li>
 *  		<b>Reference Distribution  </b> - <i>Univariate Exponential</i>
 *  	</li>
 *  	<li>
 *  		<b>Gap Loss Function       </b> - <i>Anfuso, Karyampas, and Nawroth (2017)</i>
 *  	</li>
 *  	<li>
 *  		<b>Gap Loss Weight Function</b> - <i>Cramers and von Mises</i>
 *  	</li>
 *  </ul>
 *
 *  <br><br>
 *  <ul>
 *  	<li>
 *  		Anfuso, F., D. Karyampas, and A. Nawroth (2017): A Sound Basel III Compliant Framework for
 *  			Back-testing Credit Exposure Models
 *  			https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2264620 <b>eSSRN</b>
 *  	</li>
 *  	<li>
 *  		Diebold, F. X., T. A. Gunther, and A. S. Tay (1998): Evaluating Density Forecasts with
 *  			Applications to Financial Risk Management, International Economic Review 39 (4) 863-883
 *  	</li>
 *  	<li>
 *  		Kenyon, C., and R. Stamm (2012): Discounting, LIBOR, CVA, and Funding: Interest Rate and Credit
 *  			Pricing, Palgrave Macmillan
 *  	</li>
 *  	<li>
 *  		Wikipedia (2018): Probability Integral Transform
 *  			https://en.wikipedia.org/wiki/Probability_integral_transform
 *  	</li>
 *  	<li>
 *  		Wikipedia (2019): p-value https://en.wikipedia.org/wiki/P-value
 *  	</li>
 *  </ul>
 *
 *  <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ModelValidationAnalyticsLibrary.md">Model Validation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">DROP API Construction and Usage</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/validation">Empirical Univariate Gap Distance Tests</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExponentialCramersVonMisesGapDiscriminant
{

	private static final double UnivariateRandom (
		final double lambda)
		throws Exception
	{
		return new R1UnivariateExponential (lambda).random();
	}

	private static final Sample GenerateSample (
		final double lambda,
		final int drawCount)
		throws Exception
	{
		double[] univariateRandomArray = new double[drawCount];

		for (int drawIndex = 0; drawIndex < drawCount; ++drawIndex)
		{
			univariateRandomArray[drawIndex] = UnivariateRandom (lambda);
		}

		return new Sample (univariateRandomArray);
	}

	private static final Sample[] GenerateSampleArray (
		final double lambda,
		final int drawCount,
		final int sampleCount)
		throws Exception
	{
		Sample[] sampleArray = new Sample[sampleCount];

		for (int sampleIndex = 0; sampleIndex < sampleCount; ++sampleIndex)
		{
			sampleArray[sampleIndex] = GenerateSample (
				lambda,
				drawCount
			);
		}

		return sampleArray;
	}

	private static final Ensemble GenerateEnsemble (
		final double lambda,
		final int drawCount,
		final int sampleCount)
		throws Exception
	{
		return new Ensemble (
			GenerateSampleArray (
				lambda,
				drawCount,
				sampleCount
			),
			new TestStatisticEvaluator[]
			{
				new TestStatisticEvaluator()
				{
					public double evaluate (
						final double[] drawArray)
						throws Exception
					{
						return 1.;
					}
				}
			}
		);
	}

	private static final GapTestOutcome DistanceTest (
		final Sample sample,
		final Ensemble ensemble,
		final GapTestSetting gapTestSetting)
		throws Exception
	{
		return new ProbabilityIntegralTransformTest (
			ensemble.nativeProbabilityIntegralTransform()
		).distanceTest (
			sample.nativeProbabilityIntegralTransform(),
			gapTestSetting
		);
	}

	private static final double DistanceTest (
		final double hypothesisLambda,
		final int drawCount,
		final int sampleCount,
		final Sample sample,
		final GapTestSetting gapTestSetting)
		throws Exception
	{
		return DistanceTest (
			sample,
			GenerateEnsemble (
				hypothesisLambda,
				drawCount,
				sampleCount
			),
			gapTestSetting
		).distance();
	}

	public static final void main (
		final String[] argumentArray)
		throws Exception
	{
		EnvManager.InitEnv ("");

		int drawCount = 2000;
		int sampleCount = 600;
		double sampleLambda = 1.;
		double[] hypothesisLambdaArray =
		{
			0.20,
			0.30,
			0.40,
			0.50,
			0.60,
			0.70,
			0.80,
			0.90,
			1.00,
			1.10,
			1.20,
			1.30,
			1.40,
			1.50,
			1.60,
			1.70,
			1.80,
			1.90,
			2.00,
			2.10,
			2.20,
			2.30,
			2.40,
			2.50
		};

		GapTestSetting gapTestSetting = GapTestSetting.RiskFactorLossTest (
			GapLossWeightFunction.AndersonDarling()
		);

		Sample sample = GenerateSample (
			sampleLambda,
			drawCount
		);

		System.out.println ("\t|------------------------------||");

		System.out.println ("\t|    DISCRIMINANT GRID SCAN    ||");

		System.out.println ("\t|------------------------------||");

		System.out.println ("\t|    L -> R:                   ||");

		System.out.println ("\t|        - Hypothesis Lambda   ||");

		System.out.println ("\t|        - Distance Metric     ||");

		System.out.println ("\t|------------------------------||");

		for (double hypothesisLambda : hypothesisLambdaArray)
		{
			System.out.println (
				"\t| " +
				FormatUtil.FormatDouble (hypothesisLambda, 1, 2, 1.) + " => " +
				FormatUtil.FormatDouble (
					DistanceTest (
						hypothesisLambda,
						drawCount,
						sampleCount,
						sample,
						gapTestSetting
					),
					1, 8, 1.
				) + "         ||"
			);
		}

		System.out.println ("\t|------------------------------||");

		EnvManager.TerminateEnv();
	}
}
