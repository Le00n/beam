package beam.vortrag;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class BeamExample01 {

	public static void main(String[] args) {
		Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
		Pipeline pipeline = Pipeline.create(options);

		pipeline
				.apply("ReadLines", TextIO.read().from(options.getInputFile()))
				.apply("ConvertToAdvertising", ParDo.of(new StringToAdvertisingConverter()))
				.apply("Windowing", Window.into(FixedWindows.of(Duration.standardHours(1))))
				.apply("SumOfAdvertisings", Sum.longsPerKey())
				.apply("Pricing", ParDo.of(new PricingCalcutlator(options.getPrice())))
				.apply("ConvertToString", ParDo.of(new AdvertisingToStringConverter()))
				.apply("WriteLines", new WriteOneFilePerWindow(options.getOutput(), 1));
		
		pipeline.run().waitUntilFinish();
	}

}
