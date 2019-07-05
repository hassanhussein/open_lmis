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

package org.openlmis.restapi.processor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

/**
 * {@link ClientHttpRequestInterceptor} to apply a BASIC authorization header.
 *
 * @author Phillip Webb
 * @since 4.3.1
 * @deprecated as of 5.1.1, in favor of {@link BasicAuthenticationInterceptor}
 * which reuses {@link org.springframework.http.HttpHeaders#setBasicAuth},
 * sharing its default charset ISO-8859-1 instead of UTF-8 as used here
 */
@Deprecated
public class BasicAuthorizationInterceptor implements ClientHttpRequestInterceptor {

  private final String username;

  private final String password;


  /**
   * Create a new interceptor which adds a BASIC authorization header
   * for the given username and password.
   * @param username the username to use
   * @param password the password to use
   */
  public BasicAuthorizationInterceptor(String username, String password) {
    Assert.doesNotContain(username, ":", "Username must not contain a colon");
    this.username = (username != null ? username : "");
    this.password = (password != null ? password : "");
  }


  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    String token = Base64Utils.encodeToString(
        (this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8));
    request.getHeaders().add("Authorization", "Basic " + token);
    return execution.execute(request, body);
  }

}

