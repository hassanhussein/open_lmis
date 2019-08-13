package org.openlmis.restapi.service;



import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.StaticReferenceDataService;
import org.openlmis.restapi.domain.SDIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;



@Service
public class RestSDService {

    @Autowired
    private StaticReferenceDataService staticReferenceDataService;

    @Autowired
    private MessageService messageService;


//    @Autowired
//    private SubscriberNotificationMapper subscriberMapper;


    public String getArticlesBySearchKeyword(String query) {
        try {
            HttpClient client = new HttpClient();

            GetMethod method = new GetMethod(staticReferenceDataService.getPropertyValue("jira.tz.url") + "/rest/servicedeskapi/" +
                    "servicedesk/" + staticReferenceDataService.getPropertyValue("jira.tz.service-desk.id") + "/knowledgebase/article?query=" + query.replace(" ", "%"));

            method.setRequestHeader("Accept", "application/json");
            method.setRequestHeader("Authorization", "Basic " + staticReferenceDataService.getPropertyValue("jira.tz.authorization.key"));
            method.setRequestHeader("X-ExperimentalApi", "opt-in");
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();

            return new String(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String createIssue(SDIssue sdIssue) {
        String bodyData = new JSONObject()
                .put("raiseOnBehalfOf", sdIssue.getRaiseOnBehalfOf())
                .put("serviceDeskId", staticReferenceDataService.getPropertyValue("jira.tz.service-desk.id"))
                .put("requestTypeId", staticReferenceDataService.getPropertyValue("jira.tz.service-desk.request-type-id"))
                .put("requestFieldValues", new JSONObject()
                        .put("summary", sdIssue.getSummary())
                        .put("description", sdIssue.getDescription())).toString();

        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(staticReferenceDataService.getPropertyValue("jira.tz.url") + "/rest/servicedeskapi/request");

        method.setRequestHeader("Accept", "application/json");
        method.setRequestHeader("Content-Type", "application/json");
        method.setRequestHeader("Authorization", "Basic " + staticReferenceDataService.getPropertyValue("jira.tz.authorization.key"));
        method.setRequestHeader("X-ExperimentalApi", "opt-in");
        method.setRequestBody(bodyData);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            client.executeMethod(method);
            InputStream responseBodyAsStream = method.getResponseBodyAsStream();
            String response = IOUtils.toString(responseBodyAsStream, StandardCharsets.UTF_8);

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addNotificationSubscribers(String chatId, Integer rnrId, String label)
    {
//         subscriberMapper.insert(chatId,  rnrId, label);
    }


}
