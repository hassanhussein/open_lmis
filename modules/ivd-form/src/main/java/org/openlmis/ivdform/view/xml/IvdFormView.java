/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.ivdform.view.xml;

import org.exolab.castor.types.DateTime;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.User;
import org.openlmis.demographics.domain.AnnualFacilityEstimateEntry;
import org.openlmis.equipment.domain.EquipmentOperationalStatus;
import org.openlmis.ivdform.domain.DiscardingReason;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.domain.reports.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component("ivdFormInputTool")
public class IvdFormView extends AbstractView {

  private static final String PERIODS = "periods";
  private static final String DISTRICTS = "districts";
  private static final String FACILITIES = "facilities";
  private static final String PRODUCT_NAME = "productName";
  private static final String FACILITY_ID = "facilityId";
  private static final String FACILITY_NAME = "facilityName";
  private static final String FACILITY_DETAILS = "facilityDetails";
  private static final String FACILITY = "facility";
  private static final String DEMOGRAPHICS = "demographics";
  private static final String ESTIMATE = "estimate";
  private static final String DISPLAY_ORDER = "displayOrder";
  private static final String DOSE_ID = "doseId";
  private static final String DOSE_NAME = "doseName";
  private static final String TRACK_MALE = "trackMale";
  private static final String TRACK_FEMALE = "trackFemale";
  private static final String STOCK_STATUS = "stockStatus";
  private static final String PRODUCTS = "products";
  private static final String PRODUCT = "product";
  private static final String EQUIPMENTS = "equipments";
  private static final String EQUIPMENT = "equipment";
  private static final String EQUIPMENT_INVENTORY_ID = "equipmentInventoryId";
  private static final String ENERGY_SOURCE = "energySource";
  private static final String EQUIPMENT_NAME = "equipmentName";
  private static final String MODEL = "model";
  private static final String SERIAL = "serial";
  private static final String ESTIMATE_NAME = "estimateName";
  private static final String ESTIMATE_VALUE = "estimateValue";
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String VALUE = "value";
  private static final String PRODUCT_CODE = "productCode";
  private static final String PRODUCT_CATEGORY = "productCategory";
  private static final String UNIT = "unit";
  private static final String PRODUCT_ID = "productId";
  private static final String DISEASES = "diseases";
  private static final String DISEASE = "disease";
  private static final String DISEASE_ID = "diseaseId";
  private static final String DISEASE_NAME = "diseaseName";
  private static final String VACCINATIONS = "vaccinations";
  private static final String VACCINE = "vaccine";
  private static final String SUPPLEMENTS = "supplements";
  private static final String SUPPLEMENT = "supplement";
  private static final String VITAMIN_ID = "vitaminId";
  private static final String VITAMIN_NAME = "vitaminName";
  private static final String AGE_GROUP_ID = "ageGroupId";
  private static final String AGE_GROUP_NAME = "ageGroupName";
  private static final String MANUFACTURERS = "manufacturers";
  private static final String MANUFACTURER = "manufacturer";
  private static final String MANUFACTURER_ID = "manufacturerId";
  private static final String MANUFACTURER_NAME = "manufacturerName";
  private static final String ADJUSTMENT_REASONS = "adjustmentReasons";
  private static final String REASON = "reason";
  private static final String REASON_ID = "reasonId";
  private static final String REASON_NAME = "reasonName";
  private static final String COLD_CHAIN_OPERATIONAL_STATUSES = "coldChainOperationalStatuses";
  private static final String STATUS = "status";
  private static final String STATUS_ID = "statusId";
  private static final String STATUS_NAME = "statusName";
  private static final String REGION_NAME = "regionName";
  private static final String REGION_ID = "regionId";
  private static final String REGION = "region";
  private static final String REGIONS = "regions";
  private static final String PERIOD_NAME = "periodName";
  private static final String PERIOD_ID = "periodId";
  private static final String PERIOD = "period";
  private static final String ADJUSTMENT_REASONS_US = "adjustment_reasons";
  private static final String OPERATIONAL_STATUS = "operational_status";
  private static final String FACILITY_DETAILS_US = "facility_details";

  Document doc;

  public IvdFormView() {
    setContentType("application/octet-stream");
  }

  @Override
  protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // read template
    doc = readTemplate();
    Element rootNode = doc.getDocumentElement();
    Node data = rootNode.getElementsByTagName("xfa:data").item(0);

    Element report = doc.createElement("ivdReport");

    writeDetails(report, (Long) model.get("program_id"), model.get("url").toString(), model.get("year").toString(), (User) model.get("user"));
    VaccineReport vaccineReportTemplate = (VaccineReport) model.get("reportTemplate");
    writePeriods(report, (List<ProcessingPeriod>) model.get(PERIODS));
    writeRegions(report, (List<GeographicZone>) model.get(DISTRICTS));
    writeFacilities(report, (List<Facility>) model.get(FACILITIES));
    writeProducts(report, vaccineReportTemplate);
    writeStockStatus(report, vaccineReportTemplate);
    writeVaccinations(report, vaccineReportTemplate);
    writeDiseases(report, vaccineReportTemplate);
    writeSupplements(report, vaccineReportTemplate);
    writeManufacturers(report, (List<Manufacturer>) model.get(MANUFACTURERS));
    writeAdjustmentReasons(report, (List<DiscardingReason>) model.get(ADJUSTMENT_REASONS_US));
    writeOperationalStatuses(report, (List<EquipmentOperationalStatus>) model.get(OPERATIONAL_STATUS));
    writeDemographicEstimates(report, (List<VaccineReport>) model.get(FACILITY_DETAILS_US));
    writeEquipments(report, (List<VaccineReport>) model.get(FACILITY_DETAILS_US));

    data.appendChild(report);

    response.setHeader("Content-Transfer-Encoding", "binary");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + "ivd-reporting-form.xdp\"");
    writeResponse(response, doc);
  }

  private void createElement(Element parent, String name, String value) {
    Element childElement = doc.createElement(name);
    childElement.setTextContent(value);
    parent.appendChild(childElement);
  }

  private void writeResponse(HttpServletResponse response, Document doc) throws IOException, TransformerException {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "no");

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(response.getWriter());
    try {
      transformer.transform(source, result);
    } catch (javax.xml.transform.TransformerException exp) {
      logger.warn("Transformer Warning: ", exp);
    }
  }

  private Document readTemplate() throws IOException, ParserConfigurationException, SAXException {
    Resource resource = new ClassPathResource("form_template.xml");
    InputStream stream = resource.getInputStream();
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    return dBuilder.parse(stream);
  }

  private void writeDetails(Element report, Long programId, String url, String year, User user) {

    Element detailsElement = doc.createElement("details");

    createElement(detailsElement, "programId", programId.toString());
    createElement(detailsElement, "baseUrl", url);
    createElement(detailsElement, "url", String.format("%s/rest-api/ivd-from/pdf-submit", url));
    createElement(detailsElement, "generatedDate", new DateTime().toDate().toString());
    createElement(detailsElement, "year", year);
    createElement(detailsElement, "username", user.getUserName());
    createElement(detailsElement, "userFirstName", user.getFirstName());
    createElement(detailsElement, "userLastName", user.getLastName());
    report.appendChild(detailsElement);
  }

  private void writePeriods(Element report, List<ProcessingPeriod> periods) {

    Element periodsElement = doc.createElement(PERIODS);
    Collections.sort(periods, Comparator.comparing(ProcessingPeriod::getStartDate));
    for (ProcessingPeriod period : periods) {
      Element processingPeriodElement = doc.createElement(PERIOD);


      createElement(processingPeriodElement, PERIOD_ID, period.getId().toString());
      createElement(processingPeriodElement, PERIOD_NAME, period.getName());

      periodsElement.appendChild(processingPeriodElement);
    }
    report.appendChild(periodsElement);
  }

  private void writeRegions(Element report, List<GeographicZone> zones) {

    Element periodsElement = doc.createElement(REGIONS);
    for (GeographicZone zone : zones) {
      Element regionElement = doc.createElement(REGION);
      createElement(regionElement, REGION_ID, zone.getId().toString());
      createElement(regionElement, REGION_NAME, zone.getName());

      periodsElement.appendChild(regionElement);
    }
    report.appendChild(periodsElement);
  }

  private void writeFacilities(Element report, List<Facility> facilities) {

    Element periodsElement = doc.createElement(FACILITIES);
    for (Facility facility : facilities) {
      Element facilityElement = doc.createElement(FACILITY);

      createElement(facilityElement, REGION_ID, facility.getGeographicZone().getId().toString());
      createElement(facilityElement, FACILITY_ID, facility.getId().toString());
      createElement(facilityElement, FACILITY_NAME, facility.getName());

      periodsElement.appendChild(facilityElement);
    }
    report.appendChild(periodsElement);
  }

  private void writeEquipments(Element report, List<VaccineReport> reports) {

    Element equipmentRoot = doc.createElement(EQUIPMENTS);
    for (VaccineReport reportT : reports) {

      for (ColdChainLineItem li : reportT.getColdChainLineItems()) {
        Element equipment = doc.createElement(EQUIPMENT);
        createElement(equipment, FACILITY_ID, reportT.getFacility().getId().toString());
        createElement(equipment, EQUIPMENT_INVENTORY_ID, li.getEquipmentInventoryId().toString());
        createElement(equipment, ENERGY_SOURCE, li.getEnergySource());
        createElement(equipment, EQUIPMENT_NAME, li.getEquipmentName());
        createElement(equipment, MODEL, li.getModel());
        createElement(equipment, SERIAL, (li.getSerial() == null) ? "-" : li.getSerial());

        equipmentRoot.appendChild(equipment);
      }
    }
    report.appendChild(equipmentRoot);
  }

  private void writeDemographicEstimates(Element report, List<VaccineReport> reports) {

    Element demographicEstimatesRoot = doc.createElement(DEMOGRAPHICS);
    for (VaccineReport reportT : reports) {
      for (AnnualFacilityEstimateEntry entry : reportT.getFacilityDemographicEstimates()) {
        Element estimate = doc.createElement(ESTIMATE);
        createElement(estimate, FACILITY_ID, reportT.getFacility().getId().toString());
        createElement(estimate, ESTIMATE_NAME, entry.getCategory().getName());
        createElement(estimate, ESTIMATE_VALUE, entry.getValue().toString());
        demographicEstimatesRoot.appendChild(estimate);
      }
    }
    report.appendChild(demographicEstimatesRoot);
  }

  private void writeFacilityDetails(Element report, List<VaccineReport> reports) {

    Element periodsElement = doc.createElement(FACILITY_DETAILS);
    for (VaccineReport reportT : reports) {
      Element facilityElement = doc.createElement(FACILITY);

      createElement(facilityElement, FACILITY_ID, reportT.getFacility().getId().toString());
      createElement(facilityElement, FACILITY_NAME, reportT.getFacility().getName());

      Element demographics = doc.createElement(DEMOGRAPHICS);
      facilityElement.appendChild(demographics);
      for (AnnualFacilityEstimateEntry entry : reportT.getFacilityDemographicEstimates()) {
        Element estimate = doc.createElement(ESTIMATE);
        createElement(estimate, ID, entry.getDemographicEstimateId().toString());
        createElement(estimate, NAME, entry.getCategory().getName());
        createElement(estimate, VALUE, entry.getValue().toString());
        demographics.appendChild(estimate);
      }
      Element equipments = doc.createElement(EQUIPMENTS);
      facilityElement.appendChild(equipments);
      for (ColdChainLineItem li : reportT.getColdChainLineItems()) {
        Element equipment = doc.createElement(EQUIPMENT);

        createElement(equipment, EQUIPMENT_INVENTORY_ID, li.getEquipmentInventoryId().toString());
        createElement(equipment, ENERGY_SOURCE, li.getEnergySource());
        createElement(equipment, EQUIPMENT_NAME, li.getEquipmentName());
        createElement(equipment, MODEL, li.getModel());
        createElement(equipment, SERIAL, li.getSerial());

        equipments.appendChild(equipment);
      }
      facilityElement.appendChild(equipments);
      periodsElement.appendChild(facilityElement);
    }
    report.appendChild(periodsElement);
  }


  private void writeProducts(Element report, VaccineReport template) {
    Element productsElement = doc.createElement(PRODUCTS);
    for (LogisticsLineItem li : template.getLogisticsLineItems()) {
      Element productElement = doc.createElement(PRODUCT);
      createElement(productElement, PRODUCT_ID, li.getProductId().toString());
      createElement(productElement, PRODUCT_NAME, li.getProductName());
      productsElement.appendChild(productElement);
    }
    report.appendChild(productsElement);
  }

  private void writeVaccinations(Element report, VaccineReport template) {
    Element productsElement = doc.createElement(VACCINATIONS);
    Integer displayOrder = 1;
    for (VaccineCoverageItem li : template.getCoverageLineItems()) {
      Element productElement = doc.createElement(VACCINE);

      createElement(productElement, PRODUCT_ID, li.getProductId().toString());
      createElement(productElement, PRODUCT_NAME, li.getProductName());
      createElement(productElement, DOSE_ID, li.getDoseId().toString());
      createElement(productElement, DOSE_NAME, li.getDisplayName());
      createElement(productElement, DISPLAY_ORDER, displayOrder.toString());
      createElement(productElement, TRACK_MALE, li.getTrackMale().toString());
      createElement(productElement, TRACK_FEMALE, li.getTrackFemale().toString());
      displayOrder++;
      productsElement.appendChild(productElement);
    }
    report.appendChild(productsElement);
  }

  private void writeStockStatus(Element report, VaccineReport template) {
    Element stockStatus = doc.createElement(STOCK_STATUS);

    Element productsElement = doc.createElement(PRODUCTS);
    for (LogisticsLineItem li : template.getLogisticsLineItems()) {
      Element productElement = doc.createElement(PRODUCT);

      createElement(productElement, PRODUCT_ID, li.getProductId().toString());
      createElement(productElement, PRODUCT_NAME, li.getProductName());
      createElement(productElement, PRODUCT_CODE, li.getProductCode());
      createElement(productElement, PRODUCT_CATEGORY, li.getProductCategory());
      createElement(productElement, DISPLAY_ORDER, li.getDisplayOrder().toString());
      createElement(productElement, UNIT, li.getDosageUnit());

      productsElement.appendChild(productElement);
    }
    stockStatus.appendChild(productsElement);
    report.appendChild(stockStatus);
  }

  private void writeDiseases(Element report, VaccineReport template) {
    Element diseasesElement = doc.createElement(DISEASES);

    for (DiseaseLineItem li : template.getDiseaseLineItems()) {
      Element diseaseElement = doc.createElement(DISEASE);
      createElement(diseaseElement, DISEASE_ID, li.getDiseaseId().toString());
      createElement(diseaseElement, DISEASE_NAME, li.getDiseaseName());
      createElement(diseaseElement, DISPLAY_ORDER, li.getDisplayOrder().toString());

      diseasesElement.appendChild(diseaseElement);
    }
    report.appendChild(diseasesElement);
  }

  private void writeSupplements(Element report, VaccineReport template) {
    Element supplementsElement = doc.createElement(SUPPLEMENTS);
    for (VitaminSupplementationLineItem li : template.getVitaminSupplementationLineItems()) {
      Element supplementElement = doc.createElement(SUPPLEMENT);
      createElement(supplementElement, VITAMIN_ID, li.getVaccineVitaminId().toString());
      createElement(supplementElement, VITAMIN_NAME, li.getVitaminName());
      createElement(supplementElement, AGE_GROUP_ID, li.getVitaminAgeGroupId().toString());
      createElement(supplementElement, AGE_GROUP_NAME, li.getAgeGroup());
      createElement(supplementElement, DISPLAY_ORDER, li.getDisplayOrder().toString());
      supplementsElement.appendChild(supplementElement);
    }
    report.appendChild(supplementsElement);
  }

  private void writeManufacturers(Element report, List<Manufacturer> manufacturers) {
    Element manufacturersElement = doc.createElement(MANUFACTURERS);
    for (Manufacturer manufacturer : manufacturers) {
      Element manufacturerElement = doc.createElement(MANUFACTURER);
      createElement(manufacturerElement, MANUFACTURER_ID, manufacturer.getId().toString());
      createElement(manufacturerElement, MANUFACTURER_NAME, manufacturer.getName());
      manufacturersElement.appendChild(manufacturerElement);
    }
    report.appendChild(manufacturersElement);
  }

  private void writeAdjustmentReasons(Element report, List<DiscardingReason> reasons) {
    Element reasonsElement = doc.createElement(ADJUSTMENT_REASONS);
    for (DiscardingReason reason : reasons) {
      Element reasonElement = doc.createElement(REASON);
      createElement(reasonElement, REASON_ID, reason.getId().toString());
      createElement(reasonElement, REASON_NAME, reason.getName());
      reasonsElement.appendChild(reasonElement);
    }
    report.appendChild(reasonsElement);
  }

  private void writeOperationalStatuses(Element report, List<EquipmentOperationalStatus> equipmentOperationalStatuses) {
    Element reasonsElement = doc.createElement(COLD_CHAIN_OPERATIONAL_STATUSES);
    for (EquipmentOperationalStatus status : equipmentOperationalStatuses) {
      Element statusElement = doc.createElement(STATUS);
      createElement(statusElement, STATUS_ID, status.getId().toString());
      createElement(statusElement, STATUS_NAME, status.getName());
      reasonsElement.appendChild(statusElement);
    }
    report.appendChild(reasonsElement);
  }

}
