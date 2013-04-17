package hdprobe.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hdprobe.aggregation.Aggregator;
import hdprobe.aggregation.AggregatorContainer;
import hdprobe.aggregation.AggregatorVisitor;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;
import hdprobe.utils.expr.Expression;

public class TopNFileExporter extends AbstractExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865645640652012997L;

	protected String exportFile;
	
	protected String orderBy;
	
	protected int limit;
	
	private List<AggregatorContainer> aggregatorContainers;

	public void setAggregatorContainers(List<AggregatorContainer> aggregatorContainers) {
		this.aggregatorContainers = aggregatorContainers;
	}

	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public void export() throws Exception {
		File exportFile = new File(exprContext.parse(this.exportFile));
		if (!exportFile.getParentFile().exists()) {
			exportFile.getParentFile().mkdirs();
		}
		TopNFileExportVisitor visitor = new TopNFileExportVisitor(
				exportFile, exprContext.parseExpression(exportFormat));
		for (AggregatorContainer container : aggregatorContainers) {
			container.traversal(visitor);
		}
		visitor.close();
	}
	
	class TopNFileExportVisitor implements AggregatorVisitor {
		private FileWriter writer;
		private Expression expression;
		private List<KVGroup> groups = new ArrayList<KVGroup>();

		public TopNFileExportVisitor(File exportFile, Expression exportFormat) throws IOException{
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
			groups.add(g);
			
			return true;
		}
		
		public void close() {
			try {
				Collections.sort(groups, new Comparator<KVGroup>() {
					@Override
					public int compare(KVGroup o1,
							KVGroup o2) {
						Comparable c1 = (Comparable<?>)o1.getEntry(orderBy).getValue();
						Comparable c2 = (Comparable<?>)o2.getEntry(orderBy).getValue();
						return c2.compareTo(c1) ;
					}
				});
				try {
					int c = 0;
					for (KVGroup g : groups) {
						writer.write(expression.parse(g));
						writer.write("\n");
						if (c++ > limit) {
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
