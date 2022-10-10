package com.bdc.base.web;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.vo.BaseAssetIndexAttentionVO;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * 资产统计接口实现类
 * @author lps
 *
 */
@Controller
@RequestMapping("/bdc/baseAssetStat")
public class BaseAssetStatisticalAction {
	@Autowired
	private IAssetDefinedService assetDefinedService;
	@Autowired
	private IUploadAssetLogService uploadAssetLogService;

	/**
	 * 获取首页顶部栏数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAssetStat")
	@ResponseBody
	public BaseAssetIndexAttentionVO getBaseAssetIndexAttention(){
		BaseAssetIndexAttentionVO vo = uploadAssetLogService.getBaseAssetIndexAttention();
		return vo;
	}

	/**
	 * 获取首页顶部栏数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllAssetType")
	@ResponseBody
	public Map<String,String> getAllAssetType(){
		Map<String,String> assetTypeMap = assetDefinedService.getAllAssetType();
		return assetTypeMap;
	}
	/**
	 * 按年度统计文化馆资产数据的上链数据量
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAssetByYear")
	@ResponseBody
	public List getAssetByYear(@NonNull String startYear, @NonNull String endYear, String assetCode){
		List list = uploadAssetLogService.getReportDataByYear(Integer.parseInt(startYear), Integer.parseInt(endYear));
	   return list;
	}

	/**
	 * 按月份统计文化馆资产数据的上链数据量在各个月份的分布
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAssetByMonth")
	@ResponseBody
	public List getAssetByMonth(@NonNull String year){
		List list = uploadAssetLogService.getReportDataByMonth(Integer.parseInt(year),Integer.parseInt(year));
		return list;
	}
	/**
	 * 按数据类型统计文化馆资产数据的上链数据量在各种数据类型的分布。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAssetByType")
	@ResponseBody
	public JSONArray getAssetByType(@NonNull String year){
		JSONArray data = uploadAssetLogService.getReportDataByAssetType(Integer.parseInt(year));
		return data;
	}

}
