package org.openlmis.lookupapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Fac_IDNumber",
        "Name",
        "Comm_FacName",
        "Zone",
        "Region_Code",
        "Region",
        "District_Code",
        "District",
        "Council_Code",
        "Council",
        "Ward",
        "Village",
        "FacilityTypeGroupCode",
        "FacilityTypeGroup",
        "FacilityTypeCode",
        "FacilityType",
        "OwnershipGroupCode",
        "OwnershipGroup",
        "OwnershipCode",
        "Ownership",
        "OperatingStatus",
        "Latitude",
        "Longitude",
        "RegistrationStatus",
        "CreatedAt",
        "UpdatedAt",
        "OSchangeOpenedtoClose",
        "OSchangeClosedtoOperational",
        "PostorUpdate"
})

@Data
@Setter
@Getter
@JsonSerialize(include = NON_NULL)
public class HealthFacilityDTO {

    @JsonProperty("Fac_IDNumber")
    public String facIDNumber;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Comm_FacName")
    public String commFacName;
    @JsonProperty("Zone")
    public String zone;

    @JsonProperty("Region_Code")
    public String regionCode;

    @JsonProperty("Region")
    public String region;
    @JsonProperty("District")
    public String district;
    @JsonProperty("Council")
    public String council;
    @JsonProperty("Ward")
    public String ward;
    @JsonProperty("Village")
    public String villageMtaa;
    @JsonProperty("FacilityTypeGroup")
    public String facilityTypeGroup;

    @JsonProperty("FacilityTypeCode")
    private String facilityTypeCode;

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
    public String latitude;
    @JsonProperty("Longitude")
    public String longitude;
    @JsonProperty("RegistrationStatus")
    public String registrationStatus;
    @JsonProperty("CreatedAt")
    public String createdAt;
    @JsonProperty("UpdatedAt")
    public String updatedAt;
    @JsonProperty("OSchangeOpenedtoClose")
    public String oSchangeOpenedtoClose;
    @JsonProperty("OSchangeClosedtoOperational")
    public String oSchangeClosedtoOperational;
    @JsonProperty("PostorUpdate")
    public String postorUpdate;

    public String status;

    @JsonProperty("District_Code")
    public String districtCode;

    @JsonProperty("Council_Code")
    public String councilCode;

    @JsonProperty("FacilityTypeGroupCode")
    public String facilityTypeGroupCode;

    @JsonProperty("OwnershipCode")
    public String ownershipCode;

    public String ilIDNumber;

    private Boolean activatedByMsd;

    private String msdCode;

}