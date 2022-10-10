-- 清理无效账号 无效部门 无效 只保留sa 以及  admin
delete from sys_user_comminfo where logon_id not in ('sa','admin');
delete from sys_user where user_info_id not in (select id from sys_user_comminfo);
delete from sys_user_role where user_id not in (select id from sys_user);
delete from sys_org where id != 'ROOT';

-- 清理现有无效面板数据
truncate table rd_comm_search;
truncate table rd_template;
truncate table rd_template_view;
truncate table rd_template_relation;
truncate table rd_comm_search_params;
truncate table rd_metadata_expression;
truncate table rd_link;

-- 清空附件数据
delete from comm_attach;

-- 清空日志
truncate table syslog;
truncate table exception_log; 

-- 清空报表数据
truncate table bd_report_dataset;
truncate table bd_report_element;
truncate table bd_report_element_class;
truncate table bd_report_element_dataset;
truncate table bd_report_element_function;
truncate table bd_report_function;
truncate table bd_report_info;
truncate table bd_report_sheet;
truncate table bd_report_sheet_freeze;

