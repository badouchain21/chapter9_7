package com.badou.utils;

import com.badou.designer.jdbc.datasource.ConfigFileReader;
import com.badou.tools.common.cfg.PropertiesConfigLocalLoader;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName SqlTypeLoader.java
 * @Description TODO
 * @createTime 2020年12月08日 14:36:00
 */
@Slf4j
@Component
public class SqlTypeLoader {
    private Document doc = null;

    /**
     * load doc from sql-type.xml
     */
    public void loadDoc(){
        try {
            if(doc == null) {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("mos/sql-type.xml");
                SAXReader reader = new SAXReader();
                doc = reader.read(in);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }


    public  String getType(String dbResource, String type) throws DocumentException {
        if(doc==null){
            loadDoc();
        }
        Element root = doc.getRootElement();
        Element contactElem = root.element(dbResource).element("sqltojava").element(type).element("classpath");
        return contactElem.getStringValue().trim();
    }
}
