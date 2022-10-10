package com.bdc.blockchain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName BlockChainRequest.java
 * @Description 区块链应用请求组装对象
 * @createTime 2020年10月26日 09:55:00
 */
@Getter
@Setter
public class BlockChainRequest {
    /**
     * fcn init or invoke
     */
    private String fcn;
    /**
     * args for transaction split with , ,
     * eg: 1,b,1
     */
    private String args;
}
