package in.clouthink.daas.fss.sample.spring.mvc;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultResponseBody {

}
