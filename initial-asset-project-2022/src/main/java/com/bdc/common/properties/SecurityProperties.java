package com.bdc.common.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置项里面security的配置
 * @author lps
 *
 */
@Component
@Getter
public class SecurityProperties {
	/**
	 * 文件上传白名单
	 */
//	@Value("${server.security.file.whitelist}")
	private String fileWhiteList;


}
