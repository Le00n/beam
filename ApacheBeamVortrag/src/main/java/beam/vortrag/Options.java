package beam.vortrag;

import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface Options extends PipelineOptions{

	@Required
    String getInputFile();
    void setInputFile(String value);

    @Required
    String getOutput();
    void setOutput(String value);
	
    @Required
    double  getPrice();
    void setPrice(double price);
}
