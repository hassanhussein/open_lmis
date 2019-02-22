package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Fac_IDNumber",
        "Name",
        "Comm_FacName",
        "Zone",
        "Region",
        "District_Code",
        "District",
        "Council",
        "Ward",
        "VillageMtaa",
        "FacilityTypeGroup",
        "FacilityType",
        "OwnershipCode",
        "OwnershipGroup",
        "OwnershipGroupCode",
        "Ownership",
        "OperatingStatus",
        "Latitude",
        "Longitude",
        "RegistrationStatus",
        "CreatedAt",
        "UpdatedAt",
        "OSchangeOpenedtoClose",
        "OSchangeClosedtoOperational",
        "PostorUpdate",
        "IL_TransactionIDNumber"
})

@Data
@Setter
@Getter
public class HFRFacilityDTO {

    @JsonProperty("Fac_IDNumber")
    public String code;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Comm_FacName")
    public String description;
    @JsonProperty("Zone")
    public String zone;
    @JsonProperty("Region")
    public String region;
    @JsonProperty("District")
    public String district;
    @JsonProperty("Council")
    public String council;

    @JsonProperty("District_Code")
    public String districtCode;

    @JsonProperty("Ward")
    public String ward;
    @JsonProperty("VillageMtaa")
    public String villageMtaa;
    @JsonProperty("FacilityTypeGroup")
    public String facilityTypeGroup;
    @JsonProperty("FacilityTypeCode")
    public String facilityTypeCode;
    @JsonProperty("OwnershipCode")
    public String ownershipCode;
    @JsonProperty("FacilityType")
    public String facilityType;
    @JsonProperty("OwnershipGroup")
    public String ownershipGroup;
    @JsonProperty("OwnershipGroupCode")
    public String ownershipGroupCode;
    @JsonProperty("Ownership")
    public String ownership;
    @JsonProperty("OperatingStatus")
    public String operatingStatus;
    @JsonProperty("Latitude")
    public Double latitude;
    @JsonProperty("Longitude")
    public Double longitude;
    @JsonProperty("RegistrationStatus")
    public String registrationStatus;
    @JsonProperty("CreatedAt")
    public Date createdDate;
    @JsonProperty("UpdatedAt")
    public Date updatedDate;
    @JsonProperty("OSchangeOpenedtoClose")
    public String oSchangeOpenedtoClose;
    @JsonProperty("OSchangeClosedtoOperational")
    public String oSchangeClosedtoOperational;
    @JsonProperty("PostorUpdate")
    public String postorUpdate;
    @JsonProperty("IL_TransactionIDNumber")
    public String IlIDNumber;

    public String status;

}