package org.openlmis.lookupapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InterfaceAPIService {

 @Autowired
 private InterfaceService service;

 @Scheduled(fixedRate=38800000, initialDelay=5000)
 public void ScheduledMethod() {
     //service.methodOne();
    // service.methodTwo();
     //service.methodThree();
     //service.methodFour();
     //service.methodFive();
 }

}
