package in.clouthink.daas.fss.util;

import java.util.UUID;

/**
 * Created by dz on 16/3/29.
 */
public abstract class IdentityUtils {

	public static String generateId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
