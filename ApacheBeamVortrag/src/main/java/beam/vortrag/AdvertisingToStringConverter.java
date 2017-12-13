package beam.vortrag;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class AdvertisingToStringConverter extends DoFn<KV<String, Double>, String>{

	private static final long serialVersionUID = -7564964524743452714L;
	
	@ProcessElement
	public void processElement(ProcessContext context) {
		KV<String, Double> input = context.element();
		
		context.output("Werbung: " + input.getKey() + " Preis: " + input.getValue() + " €\r\n");
	}
}
