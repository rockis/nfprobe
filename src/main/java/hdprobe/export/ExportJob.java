package hdprobe.export;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ExportJob extends QuartzJobBean {

	private Exporter exporter;
	
	public void setExporter(Exporter exporter) {
		this.exporter = exporter;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			System.out.println("begin export");
			exporter.export();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
