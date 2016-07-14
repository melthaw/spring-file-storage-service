package in.clouthink.daas.fss.sample;

import in.clouthink.daas.fss.alioss.support.impl.DefaultOssProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dz
 */
@ConfigurationProperties(prefix = "in.clouthink.daas.fss.sample.alioss")
public class AliossSampleProperties extends DefaultOssProperties {
}
