--创建于2014-11-11 该文件用于存储所有的数据库变更
--注意：请务必写明注释，按规范添加sql变更语句
-- 记录时机：表结构改变、必须执行的语句
/**
 * 数据库执行语句概要说明
 * add by XXX
 * 日期
 */
--变更的可执行sql语句

--参考示例如下
/**
 * 添加字段XXX
 * add by zhouan
 * 2014-11-21
 */
-- ALTER TABLE `table_name` ADD COLUMN field varchar(32) default null;

/**
 * 参考示例2 ： 前端页面的用户按钮
 * add by wujunliang
 * 2019-2-12
 */
-- INSERT INTO sys_resource(`id`, `parent_id`, `level_id`, `code`, `name`, `url`, `type`, `priority`, `secure_type`, `auth_type`, `OPEN_TARGET_WINDOW`, `FLG_DELETED`, `full_path`, `full_name`, `big_Icon_Path`, `small_Icon_Path`, `menu_class`, `is_account_menu`, `RESOURCE_TYPE`) VALUES ('4028e38168b839fd0168b84b32bb0001', 'ROOT', NULL, 'PERSONAL', '个人中心', '', 1, 500, NULL, NULL, 'parent', 0, 'ROOT-4028e38168b839fd0168b84b32bb0001', '八斗公司', '', '', '', 0, 0);
-- INSERT INTO sys_resource(`id`, `parent_id`, `level_id`, `code`, `name`, `url`, `type`, `priority`, `secure_type`, `auth_type`, `OPEN_TARGET_WINDOW`, `FLG_DELETED`, `full_path`, `full_name`, `big_Icon_Path`, `small_Icon_Path`, `menu_class`, `is_account_menu`, `RESOURCE_TYPE`) VALUES ('4028e38168b839fd0168b85443420002', '4028e38168b839fd0168b84b32bb0001', NULL, 'FRONT-WDZL', '我的资料', 'Personal/UserData', 2, 1, NULL, NULL, 'parent', 0, 'ROOT-4028e38168b839fd0168b84b32bb0001-4028e38168b839fd0168b85443420002', '八斗公司-个人中心', 'icon iconfont bd-user-o', '', '', 0, 0);

/**
 * 参考示例2 ： 修改flw_view_dictionary表的id字段长度
 * add by ccz
 * 2019-02-20
 */
-- ALTER TABLE flw_view_dictionary MODIFY id varchar(64);
/**
 * create bdc_asset_defined 资产定义表
 * add by lps
 * 2020-10-09
 */
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bdc_asset_defined
-- ----------------------------
DROP TABLE IF EXISTS `bdc_asset_defined`;
CREATE TABLE `bdc_asset_defined` (
  `ID` varchar(64) NOT NULL,
  `creator_name` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updator_name` varchar(255) DEFAULT NULL,
  `updator` varchar(255) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `flg_deleted` int(11) DEFAULT NULL,
  `asset_name` varchar(255) NOT NULL COMMENT '资产名称',
  `asset_code` varchar(255) NOT NULL COMMENT '资产编码',
  `asset_desc` varchar(255) NOT NULL COMMENT '资产描述',
  `mdCode` varchar(64) DEFAULT NULL COMMENT '模型编码',
  `asset_key` varchar(255) DEFAULT NULL COMMENT '资产模型主键',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
/**
  add column 接口访问权限 in I_INTER_PERMISSION
  add by lps
  2020-10-12
 */

ALTER TABLE `I_INTER_PERMISSION` ADD COLUMN INTERFACE_PERMISSION int(11)  COMMENT '接口访问权限' ;

/**
  add column 企业名称 in I_NET_INFORMATION
  add by lps
  2020-10-12
 */
ALTER TABLE `I_NET_INFORMATION` ADD COLUMN  COMPANY_NAME VARCHAR(255) COMMENT '企业名称' ;

-- ----------------------------END TEST ENV-- ----------------------------
/**
  add column 是否已初始化到区块链 in  asset

 */
ALTER TABLE `bdc_asset_demo` ADD COLUMN  IS_CREATE INT(11) COMMENT '是否已初始化到区块链' DEFAULT 0;
ALTER TABLE `bdc_asset_apple` ADD COLUMN  IS_CREATE INT(11) COMMENT '是否已初始化到区块链' DEFAULT 0;

/**
  上链日志
  add by zhongzhilong
  2020-11-05
 */
CREATE TABLE `bdc_upload_asset_log` (
  `ID` varchar(64) NOT NULL COMMENT '主键',
  `CREATOR_NAME` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CREATOR` varchar(255) DEFAULT NULL COMMENT '创建者',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATOR` varchar(255) DEFAULT NULL COMMENT '修改者',
  `UPDATOR_NAME` varchar(255) DEFAULT NULL COMMENT '修改者名称',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `FLG_DELETED` int(11) DEFAULT '0' COMMENT '逻辑删除符',
  `ASSET_ID` varchar(64) DEFAULT NULL COMMENT '资产ID',
  `ASSET_TYPE_NAME` varchar(255) DEFAULT NULL COMMENT '资产类型名称',
  `ASSET_CODE` varchar(64) DEFAULT NULL COMMENT '资产编码',
  `ASSET_KEY` varchar(64) DEFAULT NULL COMMENT '资产主键',
  `UPLOAD_STATUS` int(11) DEFAULT NULL COMMENT '上链状态',
  `HASH` varchar(255) DEFAULT NULL COMMENT '交易hash',
  `LOG` varchar(255) DEFAULT NULL COMMENT '日志',
  `UPLOAD_TIME` datetime DEFAULT NULL COMMENT '上链时间',
  `BDC_BAAS_LOG_ID` varchar(64) DEFAULT NULL COMMENT 'baas平台logID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `bdc_upload_asset_log` ADD COLUMN  ASSET_NAME_FIELD varchar(64) COMMENT '资产主键NAME' DEFAULT NULL ;

ALTER TABLE `bdc_blockchain_config` ADD COLUMN STATUS int(11)  COMMENT '状态' ;

/**
 * 接口返回结果
 * add by zzl 2020-11-20 10:03:30
 */
ALTER TABLE `i_interface_log` ADD COLUMN request_type varchar(255) COMMENT '请求类型' ;

/**
 * 数据字典添加上链日志定时器
 * add by zhongzhilong 2020-11-23 09:36
 */
INSERT INTO `comm_dictionary_item` (`id`, `dictionary_id`, `value`, `name`, `priority`, `create_time`, `creator`, `creator_name`, `remark`, `flg_default`, `code`) VALUES ('3e6e9b8f9ccc4279ad38f2e729e0e57c', 'bbff6f5f78354cc6a8696a81c34529b6', '0 0/3 * * * ? ', '上链定时器', '0.00', '2020-11-21 16:55:29', 'U00001', '超级管理员', 'null', '0', 'ASSET_UPLOAD_CRON');
INSERT INTO `comm_dictionary_item` (`id`, `dictionary_id`, `value`, `name`, `priority`, `create_time`, `creator`, `creator_name`, `remark`, `flg_default`, `code`) VALUES ('a89c5b30564243308946ebea0acda9d9', 'bbff6f5f78354cc6a8696a81c34529b6', '3', '上链日志状态维持时间', '3.00', '2020-11-21 16:55:29', 'U00001', '超级管理员', '分钟', '0', 'UPLOAD_LOG_UPDATE_TIME');
INSERT INTO `comm_dictionary_item` (`id`, `dictionary_id`, `value`, `name`, `priority`, `create_time`, `creator`, `creator_name`, `remark`, `flg_default`, `code`) VALUES ('8efd2b8bdda3497e8e646f3a97e7f818', 'bbff6f5f78354cc6a8696a81c34529b6', 'm', '上链日志状态维持时间类型', '4.00', '2020-11-23 09:37:29', 'ff80808153c624e60153c62d40670002', '系统管理员', '时间格式类型:y,M,d,H,m,s', '0', 'UPLOAD_LOG_UPDATE_TIME_TYPE');

/**
 * 公示平台配置
 */
 CREATE TABLE `publicity_setting` (
  `ID` varchar(64) NOT NULL COMMENT 'ID',
  `publicity_title` varchar(255) DEFAULT NULL COMMENT '公示平台标题',
  `publicity_background` varchar(64) DEFAULT NULL COMMENT '公示平台背景',
  `attach_id` varchar(64) DEFAULT NULL COMMENT 'attachid',
  `CREATOR` varchar(64) DEFAULT NULL COMMENT '创建者ID',
  `CREATOR_NAME` varchar(45) DEFAULT NULL COMMENT '创建者名称',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATOR` varchar(64) DEFAULT NULL COMMENT '更新者ID',
  `UPDATOR_NAME` varchar(45) DEFAULT NULL COMMENT '更新者名称',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `FLG_DELETED` int(1) DEFAULT '0' COMMENT '逻辑删除标识',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;




