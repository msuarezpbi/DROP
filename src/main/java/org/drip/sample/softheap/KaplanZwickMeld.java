
package org.drip.sample.softheap;

import org.drip.graph.heap.PriorityQueueEntry;
import org.drip.graph.softheap.KaplanZwickBinaryNode;
import org.drip.graph.softheap.KaplanZwickPriorityQueue;
import org.drip.graph.softheap.KaplanZwickTree;
import org.drip.service.env.EnvManager;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2022 Lakshmi Krishnamurthy
 * Copyright (C) 2021 Lakshmi Krishnamurthy
 * Copyright (C) 2020 Lakshmi Krishnamurthy
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
 * <i>KaplanZwickMeld</i> illustrates the Meld Operation for a Soft Heap as described in Kaplan and Zwick
 * 	(2009). The References are:
 * 
 * <br><br>
 *  <ul>
 *  	<li>
 *  		Chazelle, B. (2000): The Discrepancy Method: Randomness and Complexity
 *  			https://www.cs.princeton.edu/~chazelle/pubs/book.pdf
 *  	</li>
 *  	<li>
 *  		Chazelle, B. (2000): The Soft Heap: An Approximate Priority Queue with Optimal Error Rate
 *  			<i>Journal of the Association for Computing Machinery</i> <b>47 (6)</b> 1012-1027
 *  	</li>
 *  	<li>
 *  		Chazelle, B. (2000): A Minimum Spanning Tree Algorithm with Inverse-Ackerman Type Complexity
 *  			<i>Journal of the Association for Computing Machinery</i> <b>47 (6)</b> 1028-1047
 *  	</li>
 *  	<li>
 *  		Kaplan, H., and U. Zwick (2009): A simpler implementation and analysis of Chazelle's Soft Heaps
 *  			https://epubs.siam.org/doi/abs/10.1137/1.9781611973068.53?mobileUi=0
 *  	</li>
 *  	<li>
 *  		Pettie, S., and V. Ramachandran (2008): Randomized Minimum Spanning Tree Algorithms using
 *  			Exponentially Fewer Random Bits <i>ACM Transactions on Algorithms</i> <b>4 (1)</b> 1-27
 *  	</li>
 *  </ul>
 *
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/GraphAlgorithmLibrary.md">Graph Algorithm Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">DROP API Construction and Usage</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/softheap/README.md">Soft Heap Based Priority Queues</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class KaplanZwickMeld
{

	private static <K extends Comparable<K>, V> void PrintHeap (
		final KaplanZwickPriorityQueue<K, V> softHeap)
	{
		KaplanZwickTree<K, V> tree = softHeap.head();

		while (null != tree)
		{
			KaplanZwickBinaryNode<K, V> node = tree.root();

			System.out.println (
				"\t|\tRank = " + node.k() + "; ckey = " + node.cEntry().key() + "; List = " + node.entryList() +
					"; Parent = " + (null == node.parent() ? "null" : node.parent().cEntry().key())
			);

			System.out.println (
				"\t|\t\tLeft = " + (
					null == node.left() ? "null" : node.left().cEntry().key() + " @ " + node.left().childKeyList() +
						" @ " + node.left().entryList()
				)
			);

			System.out.println (
				"\t|\t\tRight = " + (
					null == node.right() ? "null" : node.right().cEntry().key() + " @ " + node.right().childKeyList() +
						" @ " + node.right().entryList()
				)
			);

			tree = tree.next();
		}
	}

	public static final void main (
		final String[] argumentArray)
		throws Exception
	{
		EnvManager.InitEnv (
			""
		);

		int r = 10;
		int keyCount = 1000;
		boolean doubleInsert = true;

		KaplanZwickPriorityQueue<Double, Double> softHeap1 = KaplanZwickPriorityQueue.Initial (
			false,
			r,
			new PriorityQueueEntry<Double, Double> (
				Math.random(),
				Math.random()
			)
		);

		KaplanZwickPriorityQueue<Double, Double> softHeap2 = KaplanZwickPriorityQueue.Initial (
			false,
			r,
			new PriorityQueueEntry<Double, Double> (
				Math.random(),
				Math.random()
			)
		);

		for (double keyIndex = 1.;
			keyIndex <= keyCount;
			++keyIndex)
		{
			double key1 = Math.random();

			double key2 = Math.random();

			softHeap1.insert (
				key1,
				key1
			);

			softHeap2.insert (
				key2,
				key2
			);

			if (doubleInsert)
			{
				softHeap1.insert (
					key1,
					key1
				);

				softHeap1.insert (
					key2,
					key2
				);
			}
		}

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		System.out.println ("\t| After Sequential Insertion #1");

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		PrintHeap (
			softHeap1
		);

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		System.out.println();

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		System.out.println ("\t| After Sequential Insertion #2");

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		PrintHeap (
			softHeap2
		);

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		System.out.println();

		softHeap1.meld (softHeap2);

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		System.out.println ("\t| After Extraction");

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		int orderIndex = 0;

		while (!softHeap1.isEmpty())
		{
			System.out.println (
				"\t| [" + (++orderIndex) + "] => " +
				softHeap1.extractExtremum()
			);
		}

		System.out.println ("\t|-------------------------------------------------------------------------------------");

		EnvManager.TerminateEnv();
	}
}
