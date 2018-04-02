package u.master.mars.wrapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by master_c on 2017/5/25.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface TaskProperty {

    String host() default "";

    String path();

    boolean shortChannelSupport() default true;

    boolean longChannelSupport() default false;

    int cid() default -1;

}
