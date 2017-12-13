/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package beam.vortrag;

import static com.google.common.base.MoreObjects.firstNonNull;

import javax.annotation.Nullable;

import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.FileBasedSink.FilenamePolicy;
import org.apache.beam.sdk.io.FileBasedSink.OutputFileHints;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.fs.ResolveOptions.StandardResolveOptions;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Writer für Windowed-PCollections 
 * 
 * Pro Window werden so viele Dateien erzeugt
 * wie per "numShards"-Parameter übergeben werden. Der Dateiname enthält dabei
 * den übergebenen Prefix sowie die Eventtime des Windows.
 *
 */
public class WriteOneFilePerWindow extends PTransform<PCollection<String>, PDone> {

	private static final long serialVersionUID = -5291203188453940007L;

	private static final DateTimeFormatter FORMATTER = DateTimeFormat //
			.forPattern("dd.MM.yyyy HH-mm-ss") //
			.withZone(DateTimeZone.forID("Europe/Berlin"));
	
	private String filenamePrefix;
	@Nullable
	private Integer numShards;

	public WriteOneFilePerWindow(String filenamePrefix, Integer numShards) {
		this.filenamePrefix = filenamePrefix;
		this.numShards = numShards;
	}

	@Override
	public PDone expand(PCollection<String> input) {
		ResourceId resource = FileBasedSink.convertToFileResourceIfPossible(filenamePrefix);
		TextIO.Write write = TextIO.write() //
				.to(new PerWindowFiles(resource)) //
				.withTempDirectory(resource.getCurrentDirectory()) //
				.withWindowedWrites();
		if (numShards != null) {
			write = write.withNumShards(numShards);
		}
		return input.apply(write);
	}

	public static class PerWindowFiles extends FilenamePolicy {

		private static final long serialVersionUID = 1807480038867047174L;
		private final ResourceId baseFilename;

		public PerWindowFiles(ResourceId baseFilename) {
			this.baseFilename = baseFilename;
		}

		public String filenamePrefixForWindow(IntervalWindow window) {
			String prefix = baseFilename.isDirectory() ? "" : firstNonNull(baseFilename.getFilename(), "");
			return String.format("%s-%s-%s.txt", prefix, FORMATTER.print(window.start()),
					FORMATTER.print(window.end()));
		}

		@Override
		public ResourceId windowedFilename(int shardNumber, int numShards, BoundedWindow window, PaneInfo paneInfo,
				OutputFileHints outputFileHints) {
			IntervalWindow intervalWindow = (IntervalWindow) window;
			String filename = String.format("%s-%s-of-%s%s.txt", filenamePrefixForWindow(intervalWindow), shardNumber,
					numShards, outputFileHints.getSuggestedFilenameSuffix());
			return baseFilename.getCurrentDirectory().resolve(filename, StandardResolveOptions.RESOLVE_FILE);
		}

		@Override
		public ResourceId unwindowedFilename(int shardNumber, int numShards, OutputFileHints outputFileHints) {
			throw new UnsupportedOperationException("Unsupported.");
		}
	}
}
