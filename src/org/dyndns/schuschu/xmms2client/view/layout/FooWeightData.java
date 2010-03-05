package org.dyndns.schuschu.xmms2client.view.layout;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.view.element.FooSashForm;
import org.w3c.dom.Element;

public class FooWeightData {

	public static void registerFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				int[] weights;

				if (element != null) {

					Element father = (Element) element.getParentNode();
					FooSashForm sash = getSash(father.getAttribute("name"));

					String string = element.getAttribute("weights");
					String[] split = string.split(" ");
					weights = new int[split.length];
					for (int i = 0; i < weights.length; i++) {
						weights[i] = Integer.parseInt(split[i]);
					}
					System.out.println(sash.getSash().getChildren().length);
					sash.setWeights(weights);
					return weights;
				}
				return null;
			}

		};
		FooFactory.factories.put("WeightData", factory);
	}

	private static FooSashForm getSash(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooSashForm) {
			return ((FooSashForm) o);
		}
		return null;
	}

}
