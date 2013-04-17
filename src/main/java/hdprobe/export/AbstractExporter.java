package hdprobe.export;

import hdprobe.utils.expr.ExpressionContext;

import org.springframework.scheduling.quartz.JobDetailBean;

public abstract class AbstractExporter extends JobDetailBean implements
		Exporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6177538396099136437L;
	protected String exportFormat;
	protected ExpressionContext exprContext;
	
	public void setExpressionContext(ExpressionContext exprContext) {
		this.exprContext = exprContext;
	}
	
	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}
	
}
