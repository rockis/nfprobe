package hdprobe.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import hdprobe.aggregation.Aggregator;
import hdprobe.aggregation.AggregatorContainer;
import hdprobe.aggregation.AggregatorVisitor;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;
import hdprobe.utils.expr.Expression;

public class FileExporter extends AbstractExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865645640652012997L;

	protected String exportFile;
	
	private List<AggregatorContainer> aggregatorContainers;

	public void setAggregatorContainers(List<AggregatorContainer> aggregatorContainers) {
		this.aggregatorContainers = aggregatorContainers;
	}

	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}

	@Override
	public void export() throws Exception {
		File exportFile = new File(exprContext.parse(this.exportFile));
		if (!exportFile.getParentFile().exists()) {
			exportFile.getParentFile().mkdirs();
		}
		FileExportVisitor visitor = new FileExportVisitor(
				exportFile, exprContext.parseExpression(exportFormat));
		for (AggregatorContainer container : aggregatorContainers) {
			container.traversal(visitor);
		}
		visitor.close();
	}
	
	class FileExportVisitor implements AggregatorVisitor {
		private FileWriter writer;
		private Expression expression;

		public FileExportVisitor(File exportFile, Expression exportFormat) throws IOException{
			this.expression = exportFormat;
			writer = new FileWriter(exportFile);
		}

		@Override
		public boolean visit(Aggregator aggregator) {
			if (!aggregator.check(null)) {
				return false;
			}
			KVGroup g = aggregator.export();
			g.addEntry(new KVEntry("stat_time", new Date()));
			try {
				writer.write(expression.parse(g));
				writer.write("\n");
			} catch (IOException e) {
			}
			return true;
		}
		
		public void close() {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
}
