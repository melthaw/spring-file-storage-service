package in.clouthink.daas.fss.util;

import java.util.UUID;

/**
* @author dz
 */
public abstract class IdentityUtils {

	public static String generateId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
