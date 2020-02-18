package org.openlmis.lookupapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

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
public class HealthFacilityDTO  extends BaseModel implements Importable {

    @ImportField(name = "Facility Number")
    @JsonProperty("Fac_IDNumber")
    public String facIDNumber;

    @ImportField(name = "Facility Name")
    @JsonProperty("Name")
    public String name;

    @ImportField(name = "Common Name")
    @JsonProperty("Comm_FacName")
    public String commFacName;

    @ImportField(name = "Zone")
    @JsonProperty("Zone")
    public String zone;

    @ImportField(name = "Region Code")
    @JsonProperty("Region_Code")
    public String regionCode;

    @ImportField(name = "Region")
    @JsonProperty("Region")
    public String region;

    @ImportField(name = "District")
    @JsonProperty("District")
    public String district;

    @ImportField(name = "Council")
    @JsonProperty("Council")
    public String council;

    @ImportField(name = "Ward")
    @JsonProperty("Ward")
    public String ward;

    @ImportField(name = "Village")
    @JsonProperty("Village")
    public String villageMtaa;

    @ImportField(name = "Region Code")
    @JsonProperty("FacilityTypeGroup")
    public String facilityTypeGroup;

    @ImportField(name = "Facility Type Code")
    @JsonProperty("FacilityTypeCode")
    private String facilityTypeCode;

    @ImportField(name = "Facility Type")
    @JsonProperty("FacilityType")
    public String facilityType;

    @ImportField(name = "Ownership Group")
    @JsonProperty("OwnershipGroup")
    public String ownershipGroup;

    @ImportField(name = "Ownership Group Code")
    @JsonProperty("OwnershipGroupCode")
    public String ownershipGroupCode;

    @ImportField(name = "Ownership")
    @JsonProperty("Ownership")
    public String ownership;

    @ImportField(name = "Operating Status")
    @JsonProperty("OperatingStatus")
    public String operatingStatus;

    @ImportField(name = "Latitude")
    @JsonProperty("Latitude")
    public String latitude;

    @ImportField(name = "Longitude")
    @JsonProperty("Longitude")
    public String longitude;

    @ImportField(name = "Registration Status")
    @JsonProperty("RegistrationStatus")
    public String registrationStatus;

    @ImportField(name = "CreatedAt")
    @JsonProperty("CreatedAt")
    public String createdAt;

    @ImportField(name = "UpdatedAt")
    @JsonProperty("UpdatedAt")
    public String updatedAt;

    @ImportField(name = "OSchangeOpenedtoClose")
    @JsonProperty("OSchangeOpenedtoClose")
    public String oSchangeOpenedtoClose;


    @ImportField(name = "OSchangeClosedtoOperational")
    @JsonProperty("OSchangeClosedtoOperational")
    public String oSchangeClosedtoOperational;


    @ImportField(name = "PostorUpdate")
    @JsonProperty("PostorUpdate")
    public String postorUpdate;

    public String status;

    @ImportField(name = "PostorUpdate")
    @JsonProperty("District_Code")
    public String districtCode;

    @ImportField(name = "Council Code")
    @JsonProperty("Council_Code")
    public String councilCode;

    @ImportField(name = "FacilityType Group Code")
    @JsonProperty("FacilityTypeGroupCode")
    public String facilityTypeGroupCode;

    @ImportField(name = "Ownership Code")
    @JsonProperty("OwnershipCode")
    public String ownershipCode;

    public String ilIDNumber;

    private Boolean activatedByMsd;

    private String msdCode;

}