package com.juan.shop.pay.popular;

import weixin.popular.bean.paymch.MchBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guanhuan_li
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPublickeyResult extends MchBase {

    /** 密钥 */
    private String pub_key;

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }
}
