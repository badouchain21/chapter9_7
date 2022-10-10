package com.bdc.base.web;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import com.badou.brms.cfg.publicity.service.IPublicitySettingService;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.vo.BaseAssetPublicityAttentionVO;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/platform/search")
public class BaseAssetPublicityAction {

    @Autowired
    private IBaseAssetService baseAssetService;
    @Autowired
    private IUploadAssetLogService uploadAssetLogService;
    @Autowired
    private IPublicitySettingService publicitySettingService;

    @PostMapping("/getAmount")
    public BaseAssetPublicityAttentionVO getAmount (){
        return baseAssetService.getBaseAssetPublicityAttention();
    }

    @PostMapping("/getBlockHeight")
    public Integer getBlockHeight () throws IOException {
        return baseAssetService.getBlockHeight();
    }

    @PostMapping("/listJSON")
    public Pagelet listJSON (String keyword, @RequestParam(name = "pageNo") Integer pageNo,@RequestParam(name = "perPageSize") Integer perPageSize,String assetId){
        Pagelet pagelet = new Pagelet(true);
        pagelet.setPageNo(pageNo);
        pagelet.setPerPageSize(perPageSize);
        Pagelet data = null;
        if (StringUtils.isNotEmpty(assetId)){
            data = uploadAssetLogService.findByAssetId(null,assetId,pagelet);
        } else {
            data = uploadAssetLogService.findByKeyword(keyword.trim(),null,pagelet);
        }
        return data;
    }

    /**
     * 获取公示平台配置信息
     * @return
     */
    @GetMapping("/findPublicityInfo")
    public JSONObject findPublicityInfo (){
        JSONObject jsonObject = new JSONObject();
        try {
            List<PublicitySettingEntity> publicitySettingEntities = publicitySettingService.listAll();
            if (Objects.nonNull(publicitySettingEntities) && !publicitySettingEntities.isEmpty()){
                PublicitySettingEntity publicitySettingEntity = publicitySettingEntities.get(0);
                jsonObject.put("title",publicitySettingEntity.getPublicityTitle());
                jsonObject.put("background",publicitySettingEntity.getPublicityBackground());
            }
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return jsonObject;
    }

}
