/*
 * FileName: MTestStep.java
 * 
 * Description: create MTestStep annotation
 * 
 * History: 
 * 1.0 SHUYUFANG 2013-08-19 Create
 */


package ARH.framework.annotation;

import java.lang.annotation.*;

/**
 * this annotation is used in customized MBT class
 * @author SHUYUFANG
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MTestStep
{
	String beginState() default "";
	String endState() default "";
	int repeatTime() default 1;
	String param() default "";
	String data() default "";
}