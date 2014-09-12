/*
 * FileName: Model.java
 * 
 * Description: create Model annotation
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
@Target(ElementType.TYPE)
@Documented
public @interface Model
{
    
}