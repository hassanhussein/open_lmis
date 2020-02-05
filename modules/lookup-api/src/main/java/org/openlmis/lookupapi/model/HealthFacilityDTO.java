package org.openlmis.lookupapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Fac_IDNumber",
        "Name",
        "Comm_FacName",
        "Zone",
        "Region",
        "DistrictCode",
        "District",
        "CouncilCode",
        "Council",
        "Ward",
        "VillageMtaa",
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
public class HealthFacilityDTO {

    @JsonProperty("Fac_IDNumber")
    public String facIDNumber;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Comm_FacName")
    public String commFacName;
    @JsonProperty("Zone")
    public String zone;
    @JsonProperty("Region")
    public String region;
    @JsonProperty("District")
    public String district;
    @JsonProperty("Council")
    public String council;
    @JsonProperty("Ward")
    public String ward;
    @JsonProperty("VillageMtaa")
    public String villageMtaa;
    @JsonProperty("FacilityTypeGroup")
    public String facilityTypeGroup;
    @JsonProperty("FacilityType")
    public String facilityType;
    @JsonProperty("OwnershipGroup")
    public String ownershipGroup;
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

    @JsonProperty("DistrictCode")
    public String districtCode;

    @JsonProperty("CouncilCode")
    public String councilCode;

    @JsonProperty("FacilityTypeGroupCode")
    public String facilityTypeGroupCode;

    @JsonProperty("OwnershipCode")
    public String ownershipCode;

    public String ilIDNumber;

}