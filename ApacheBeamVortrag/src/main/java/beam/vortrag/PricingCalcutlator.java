package beam.vortrag;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class PricingCalcutlator extends DoFn<KV<String, Long>, KV<String, Double>> {

	private static final long serialVersionUID = -3490020580831888898L;

	private double price;
	
	public PricingCalcutlator(double price) {
		this.price = price;
	}
	
	@ProcessElement
	public void processElement(ProcessContext context) {
		KV<String, Long> input = context.element();
		
		context.output(KV.of(input.getKey(), input.getValue() * price));
	}
}
