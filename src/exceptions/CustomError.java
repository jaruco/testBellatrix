package exceptions;

public class CustomError extends Exception {

	private static final long serialVersionUID = -5595935083809370482L;
	private int errCode;
	
	public CustomError(int errorCode, String str) {
		super(str);
		this.errCode = errorCode;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	
	
}

