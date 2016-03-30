package in.clouthink.daas.fss.sample.spring.mvc;

/**
 *
 */
public class ResultWrapper {

	public static ResultWrapper succeed() {
		ResultWrapper result = new ResultWrapper();
		result.setSucceed(true);
		return result;
	}

	public static ResultWrapper succeed(Object data) {
		ResultWrapper result = new ResultWrapper();
		result.setSucceed(true);
		result.setData(data);
		return result;
	}

	public static ResultWrapper failure(String message) {
		ResultWrapper result = new ResultWrapper();
		result.setSucceed(false);
		result.setMessage(message);
		return result;
	}

	public static ResultWrapper failure(Throwable throwable) {
		ResultWrapper result = new ResultWrapper();
		result.setSucceed(false);
		result.setMessage(throwable.getMessage());
		return result;
	}

	private boolean succeed = true;

	private Object data;

	private String message;

	public ResultWrapper() {
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
