package com.bdc.common.annotate;



import com.bdc.common.UploadTypeEnum;

import java.lang.annotation.*;

/**
 * upload type annotation
 * @author lps
 *
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadAssetTypeHandler {

	UploadTypeEnum value();
}
