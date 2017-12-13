package beam.vortrag;

import org.apache.beam.sdk.transforms.SerializableFunction;

public class SumAdvertisings implements SerializableFunction<Iterable<Integer>, Integer> {
	  
	private static final long serialVersionUID = 472009454789714999L;

	@Override
	  public Integer apply(Iterable<Integer> input) {
	    int sum = 0;
	    for (Integer item : input) {
	      sum += 1;
	    }
	    return sum;
	  }
	}
