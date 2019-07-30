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

package org.openlmis.web.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.openlmis.core.domain.User;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.UserService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.restapi.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.security.rsa.RSAPublicKeyImpl;

import java.security.KeyFactory;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Controller
public class TokenController extends BaseController {

  @Autowired
  private UserService userService;

  @Autowired
  private ConfigurationSettingService configSettings;


  @RequestMapping(value = "/rest-api/nexleaf-token", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getToken(Principal principal) {
    Long userId = loggedInUserId(principal);
    User user = userService.getById(userId);
    try {

      String privateKeyContent = configSettings.getConfigurationStringValue("NEXLEAF_SSO_PRIVATE_KEY");
      String publicKeyContent = configSettings.getConfigurationStringValue("NEXLEAF_SSO_PUBLIC_KEY");

      if (privateKeyContent.length() == 0 || publicKeyContent.length() == 0){
        throw new DataException("Public & Private key not configured");
      }

      KeyFactory kf = KeyFactory.getInstance("RSA");

      PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent.replace(" ","")));
      RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);

      X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent.replace(" ","")));
      RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

      Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
      String token = JWT.create()
          .withIssuer("vims")
          .withSubject(user.getEmail())
          .withIssuedAt(new Date())
          .withClaim("firstname", user.getFirstName())
          .withClaim("lastname", user.getLastName())
          .sign(algorithm);
      return OpenLmisResponse.response("token", token);
    } catch (Exception exception) {

      //Invalid Signing configuration / Couldn't convert Claims.
    }
    return OpenLmisResponse.error("Could not generate token", HttpStatus.BAD_REQUEST);
  }
}
