/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.converter;


import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.Program;
import org.springframework.stereotype.Component;

@Component
public class ProductDeserializer extends EntityDeserializer<Product> {


    @Override
    public void createEntity() {
        super.setBaseModel(new Product());
    }

    @Override
    public void mapValues() {

        final String code = node.get("code").asText();
        final String alternateitemcode = node.get("alternateitemcode").asText();
        final String manufacturer = node.get("manufacturer").asText();
        final String manufacturercode = node.get("manufacturercode").asText();
        final String manufacturerbarcode = node.get("manufacturerbarcode").asText();
        final String mohbarcode = node.get("mohbarcode").asText();
        final String gtin = node.get("gtin").asText();
        final String type = node.get("type").asText();
        final String primaryname = node.get("primaryname").asText();
        final String fullname = node.get("fullname").asText();
        final String genericname = node.get("genericname").asText();
        final String alternatename = node.get("alternatename").asText();
        final String description = node.get("description").asText();
        final String strength = node.get("strength").asText();
        final Long formid = node.get("formid").asLong();

        final Long dosageunitid = node.get("dosageunitid").asLong();
        final Long productgroupid = node.get("productgroupid").asLong();
        final String dispensingunit = node.get("dispensingunit").asText();
        final Integer dosesperdispensingunit = node.get("dosesperdispensingunit").asInt();
        final Integer packsize = node.get("packsize").asInt();
        final Integer alternatepacksize = node.get("alternatepacksize").asInt();

        final Boolean storerefrigerated = node.get("storerefrigerated").asBoolean();
        final Boolean storeroomtemperature = node.get("storeroomtemperature").asBoolean();
        final Boolean hazardous = node.get("hazardous").asBoolean();
        final Boolean flammable = node.get("flammable").asBoolean();
        final Boolean controlledsubstance = node.get("controlledsubstance").asBoolean();
        final Boolean lightsensitive = node.get("lightsensitive").asBoolean();

        final Boolean approvedbywho = node.get("approvedbywho").asBoolean();
        final Double contraceptivecyp = node.get("contraceptivecyp").asDouble();
        final Double packlength = node.get("packlength").asDouble();
        final Double packwidth = node.get("packwidth").asDouble();
        final Double packheight = node.get("packheight").asDouble();
        final Double packweight = node.get("packweight").asDouble();

        final Integer packspercarton = node.get("packspercarton").asInt();
        final Double cartonlength = node.get("cartonlength").asDouble();
        final Double cartonwidth = node.get("cartonwidth").asDouble();
        final Double cartonheight = node.get("cartonheight").asDouble();
        final Integer cartonsperpallet = node.get("cartonsperpallet").asInt();
        final Integer expectedshelflife = node.get("expectedshelflife").asInt();
        final String specialstorageinstructions = node.get("specialstorageinstructions").asText();
        final String specialtransportinstructions = node.get("specialtransportinstructions").asText();
        final Boolean active = node.get("active").asBoolean();

        final Boolean fullsupply = node.get("fullsupply").asBoolean();
        final Boolean tracer = node.get("tracer").asBoolean();
        final Boolean roundtozero = node.get("roundtozero").asBoolean();
        final Boolean archived = node.get("archived").asBoolean();
        final Integer packroundingthreshold = node.get("packroundingthreshold").asInt();
        final Integer mslpacksize = node.get("mslpacksize").asInt();



        baseModel.setCode(code);
        baseModel.setAlternateItemCode(alternateitemcode);
        baseModel.setManufacturer(manufacturer);
        baseModel.setManufacturerCode(manufacturercode);
        baseModel.setManufacturerBarCode(manufacturerbarcode);
        baseModel.setMohBarCode(mohbarcode);
        baseModel.setGtin(gtin);
        baseModel.setType(type);
        baseModel.setPrimaryName(primaryname);
        baseModel.setFullName(fullname);
        baseModel.setGenericName(genericname);
        baseModel.setAlternateName(alternatename);
        baseModel.setDescription(description);
        baseModel.setDescription(description);
        baseModel.setStrength(strength);
        baseModel.setFormId(formid);
        baseModel.setDosageUnitId(dosageunitid);
        baseModel.setProductGroupId(productgroupid);
        baseModel.setDispensingUnit(dispensingunit);
        baseModel.setDosesPerDispensingUnit(dosesperdispensingunit);
        baseModel.setPackSize(packsize);
        baseModel.setAlternatePackSize(alternatepacksize);
        baseModel.setStoreRefrigerated(storerefrigerated);
        baseModel.setStoreRoomTemperature(storeroomtemperature);
        baseModel.setHazardous(hazardous);
        baseModel.setFlammable(flammable);
        baseModel.setControlledSubstance(controlledsubstance);
        baseModel.setLightSensitive(lightsensitive);
        baseModel.setApprovedByWHO(approvedbywho);
        baseModel.setContraceptiveCYP(contraceptivecyp);
        baseModel.setPackLength(packlength);
        baseModel.setPackWidth(packwidth);
        baseModel.setPackWeight(packweight);
        baseModel.setPacksPerCarton(packspercarton);
        baseModel.setCartonLength(cartonlength);
        baseModel.setCartonWidth(cartonwidth);
        baseModel.setCartonHeight(cartonheight);
        baseModel.setCartonsPerPallet(cartonsperpallet);
        baseModel.setExpectedShelfLife(expectedshelflife);
        baseModel.setSpecialStorageInstructions(specialstorageinstructions);
        baseModel.setSpecialTransportInstructions(specialtransportinstructions);
        baseModel.setActive(active);
        baseModel.setFullSupply(fullsupply);
        baseModel.setTracer(tracer);
        baseModel.setRoundToZero(roundtozero);
        baseModel.setArchived(archived);
        baseModel.setPackRoundingThreshold(packroundingthreshold);
        baseModel.setMslPackSize(mslpacksize);
    }
}
