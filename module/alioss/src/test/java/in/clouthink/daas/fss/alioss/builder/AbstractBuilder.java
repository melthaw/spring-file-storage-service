package in.clouthink.daas.fss.alioss.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * It's a base model builder for unit test. <br />
 * Created on 7/8/15.
 */
public abstract class AbstractBuilder<D, B extends AbstractBuilder> {
    
    protected final D d;
    
    protected AbstractBuilder() {
        d = createDomain();
    }
    
    /**
     * Override this to create a model instance.
     * 
     * @return
     */
    protected D createDomain() {
        Class<D> type = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Create a builder instance by the given builder type.
     * 
     * @param builderType
     * @param <B>
     * @return Builder instance
     */
    public static final <B extends AbstractBuilder> B create(Class<B> builderType) {
        try {
            Constructor<B> builderConstructor = builderType.getDeclaredConstructor();
            builderConstructor.setAccessible(true);
            return builderConstructor.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Add rules to build a valid model instance.
     * 
     * @return builder
     */
    public B valid() {
        return (B) this;
    }
    
    /**
     * Build and return model instance.
     * 
     * @return Domain instance
     */
    public final D build() {
        return d;
    }
}
