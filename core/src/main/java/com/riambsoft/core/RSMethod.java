package com.riambsoft.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RSMethod {

	/**
	 * 访问控制,默认值为false ,表示不控制访问权限,即任何角色都
	 * 可以调用该方法,如果为true,表示该方法是一个权限控制点.必须
	 * 具有对应角色的用户才可以调用该业务方法.
	 * 
	 * @return
	 */
	public boolean accessControl() default false;

	/**
	 * 访问控制说明,用于标明该方法的业务含义,默认为空,
	 * 如果该方法是一个权限控制点,则必须注明控制说明.
	 * 
	 * @return
	 */
	public String accessDesc() default "";

}
