package org.openlmis.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Properties {
    @JsonProperty("LBL")
    public String lBL;
    @JsonProperty("FIP")
    public String fIP;
    @JsonProperty("MMT_ID")
    public String mMT_ID;
    @JsonProperty("SHORT__FRM")
    public String sHORT__FRM;
    @JsonProperty("LONG_FRM")
    public String lONG_FRM;
    @JsonProperty("ADM0")
    public String aDM0;
    @JsonProperty("ADM1")
    public String aDM1;
    @JsonProperty("ADM2")
    public String aDM2;
    @JsonProperty("ADM3")
    public String aDM3;
    @JsonProperty("ADM4")
    public String aDM4;
    @JsonProperty("ADM5")
    public String aDM5;
    @JsonProperty("STL-0")
    public int sTL0;
    @JsonProperty("STL-1")
    public int sTL1;
    @JsonProperty("STL-2")
    public int sTL2;
    @JsonProperty("STL-3")
    public String sTL3;
    @JsonProperty("STL-4")
    public String sTL4;
    @JsonProperty("STL-5")
    public String sTL5;
    @JsonProperty("hc-key")
    public String hcKey;
    public String name;
}



