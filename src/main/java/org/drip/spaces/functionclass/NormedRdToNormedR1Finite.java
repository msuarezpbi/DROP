
package org.drip.spaces.functionclass;

import org.drip.function.definition.RdToR1;
import org.drip.spaces.rxtor1.NormedRdToNormedR1;

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
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * <i>NormedRdToNormedR1Finite</i> implements the Class F of f : Normed R<sup>d</sup> To Normed R<sup>1</sup>
 * 	Spaces of Finite Functions. The Reference we've used is:
 *
 * <br><br>
 * 	<ul>
 *  	<li>
 *  		Carl, B., and I. Stephani (1990): <i>Entropy, Compactness, and the Approximation of Operators</i>
 *  			<b>Cambridge University Press</b> Cambridge UK 
 *  	</li>
 *  </ul>
 *
 *  It provides the following Functionality:
 *
 *  <ul>
 * 		<li><i>NormedRdToNormedR1Finite</i> Function Class Constructor</li>
 * 		<li>Retrieve the Finite Class of R<sup>d</sup> To R<sup>1</sup> Functions</li>
 *  </ul>
 *
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/StatisticalLearningLibrary.md">Statistical Learning Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces/README.md">R<sup>1</sup> and R<sup>d</sup> Vector/Tensor Spaces (Validated and/or Normed), and Function Classes</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces/functionclass/README.md">Normed Finite Spaces Function Class</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedRdToNormedR1Finite extends NormedRxToNormedR1Finite
{

	/**
	 * <i>NormedRdToNormedR1Finite</i> Function Class Constructor
	 * 
	 * @param maureyConstant The Maurey Constant
	 * @param normedRdToNormedR1Array Array of the Function Spaces
	 * 
	 * @throws java.lang.Exception Thrown if NormedRdToNormedR1Class Instance cannot be created
	 */

	public NormedRdToNormedR1Finite (
		final double maureyConstant,
		final NormedRdToNormedR1[] normedRdToNormedR1Array)
		throws java.lang.Exception
	{
		super (maureyConstant, normedRdToNormedR1Array);

		for (int i = 0; i < normedRdToNormedR1Array.length; ++i) {
			if (null == normedRdToNormedR1Array[i]) {
				throw new Exception ("NormedRdToNormedR1Finite ctr: Invalid Input Function");
			}
		}
	}

	/**
	 * Retrieve the Finite Class of R<sup>d</sup> To R<sup>1</sup> Functions
	 * 
	 * @return The Finite Class of R<sup>d</sup> To R<sup>1</sup> Functions
	 */

	public RdToR1[] functionRdToR1Set()
	{
		NormedRdToNormedR1[] normedRdToNormedR1Array = (NormedRdToNormedR1[]) functionSpaces();

		if (null == normedRdToNormedR1Array) {
			return null;
		}

		int functionCount = normedRdToNormedR1Array.length;
		RdToR1[] rdToR1Array = new RdToR1[functionCount];

		for (int i = 0; i < functionCount; ++i) {
			rdToR1Array[i] = normedRdToNormedR1Array[i].function();
		}

		return rdToR1Array;
	}
}
