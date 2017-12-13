package beam.vortrag;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StringToAdvertisingConverter extends DoFn<String, KV<String, Long>> {

	private static final long serialVersionUID = -1401243530455650738L;
	private static final DateTimeFormatter dtf = DateTimeFormat //
			.forPattern("MM/dd/yyyy HH:mm:ss") //
			.withZone(DateTimeZone.forID("Europe/Berlin"));

	@ProcessElement
	public void processElement(ProcessContext context) {
		String input = context.element();
		String[] inputSplits = input.split(";");
		
		String advertisingName = inputSplits[0];
		Long advertisingCount = Long.parseLong(inputSplits[1]);
		Instant eventtime = Instant.parse(inputSplits[2], dtf);

		context.outputWithTimestamp(KV.of(advertisingName, advertisingCount), eventtime);
	}
}
