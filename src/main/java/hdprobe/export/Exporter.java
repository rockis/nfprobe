package hdprobe.export;

public interface Exporter {

	public void setExportFile(String filepath);
	public void setExportFormat(String format);
	public void export() throws Exception;
	
}
