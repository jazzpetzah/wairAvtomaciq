package javadoc_exporter.confluence_rest_api;

public class RESTApiError extends Exception {
	private static final long serialVersionUID = 5411421080748258229L;
	
	private int errorCode;
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public RESTApiError(String msg) {
		super(msg);
	}
	
	public RESTApiError(String msg, int errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
