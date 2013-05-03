/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.core.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.core.domain.FacilityApprovedProduct;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.FacilityApprovedProductRepository;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;
import static org.openlmis.core.builder.FacilityApprovedProductBuilder.*;


@RunWith(MockitoJUnitRunner.class)
public class FacilityApprovedProductServiceTest {

  @Mock
  private FacilityApprovedProductRepository facilityApprovedProductRepository;

  @Mock
  private ProgramService programService;

  @Mock
  private ProductService productService;

  @Mock
  private ProgramProductService programProductService;

  @Mock
  private FacilityService facilityService;

  @Rule
  public ExpectedException expectedException = none();

  FacilityApprovedProductService facilityApprovedProductService;

  @Before
  public void setup() {
    facilityApprovedProductService = new FacilityApprovedProductService(facilityApprovedProductRepository, programService, productService, programProductService, facilityService);
  }


  @Test
  public void shouldSaveFacilityApprovedProduct() throws Exception {

    FacilityApprovedProduct facilityApprovedProduct = make(a(defaultFacilityApprovedProduct));

    Long programId = 45L;
    Long productId = 10L;
    Long programProductId = 100L;

    when(programService.getIdForCode(defaultProgramCode)).thenReturn(programId);
    when(productService.getIdForCode(defaultProductCode)).thenReturn(productId);
    when(programProductService.getIdByProgramIdAndProductId(programId, productId)).thenReturn(100L);
    when(facilityService.getFacilityTypeByCode(facilityApprovedProduct.getFacilityType())).thenReturn(new FacilityType());

    facilityApprovedProductService.save(facilityApprovedProduct);

    verify(programService).getIdForCode(defaultProgramCode);
    verify(productService).getIdForCode(defaultProductCode);
    verify(programProductService).getIdByProgramIdAndProductId(programId, productId);
    verify(facilityApprovedProductRepository).insert(facilityApprovedProduct);

    assertThat(facilityApprovedProduct.getProgramProduct().getProgram().getId(), is(programId));
    assertThat(facilityApprovedProduct.getProgramProduct().getProduct().getId(), is(productId));
    assertThat(facilityApprovedProduct.getProgramProduct().getId(), is(programProductId));
  }

  @Test
  public void shouldNotSaveFacilityApprovedProductAndThrowAnExceptionWhenProgramDoesNotExist() throws Exception {
    FacilityApprovedProduct facilityApprovedProduct = make(a(defaultFacilityApprovedProduct));

    doThrow(new DataException("abc")).when(programService).getIdForCode(defaultProgramCode);

    expectedException.expect(DataException.class);
    expectedException.expectMessage("abc");

    facilityApprovedProductService.save(facilityApprovedProduct);
    verify(programService).getIdForCode(defaultProgramCode);

    verify(facilityApprovedProductRepository, never()).insert(facilityApprovedProduct);
  }

  @Test
  public void shouldNotSaveFacilityApprovedProductAndThrowAnExceptionWhenProductDoesNotExist() throws Exception {
    FacilityApprovedProduct facilityApprovedProduct = make(a(defaultFacilityApprovedProduct));

    doThrow(new DataException("abc")).when(productService).getIdForCode(defaultProductCode);

    expectedException.expect(DataException.class);
    expectedException.expectMessage("abc");

    facilityApprovedProductService.save(facilityApprovedProduct);

    verify(productService).getIdForCode(defaultProgramCode);
    verify(facilityApprovedProductRepository, never()).insert(facilityApprovedProduct);
  }

  @Test
  public void shouldNotSaveFacilityApprovedProductAndThrowAnExceptionWhenProgramProductDoesNotExist() throws Exception {
    FacilityApprovedProduct facilityApprovedProduct = make(a(defaultFacilityApprovedProduct));

    Long programId = 1L;
    Long productId = 2L;

    when(programService.getIdForCode(defaultProgramCode)).thenReturn(programId);
    when(productService.getIdForCode(defaultProductCode)).thenReturn(productId);

    doThrow(new DataException("abc")).when(programProductService).getIdByProgramIdAndProductId(programId, productId);

    expectedException.expect(DataException.class);
    expectedException.expectMessage("abc");

    facilityApprovedProductService.save(facilityApprovedProduct);

    verify(programProductService).getIdByProgramIdAndProductId(programId, productId);
    verify(facilityApprovedProductRepository, never()).insert(facilityApprovedProduct);
  }
}
