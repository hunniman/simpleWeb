package dao.pojo.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * float列
 * @author yxh
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnFloat {
	String field() default "";//默认取类字段名
	int len() default 11;//列长度
	boolean isNull() default false;//是否为null,默认不允许
	float defVal() default 0;//默认值,默认为0
	int dec() default 4;//精度 默认4位
	String comment();//注释
}
