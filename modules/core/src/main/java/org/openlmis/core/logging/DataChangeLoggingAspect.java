
/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openlmis.core.event.DataChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;


@Aspect
@Component
public class DataChangeLoggingAspect {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private String message= "\"messages\":";


    @Around("@annotation(org.openlmis.core.logging.Loggable)")
    public Object myAdvice(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        System.out.println("Executing myAdvice!!");
        Object result = jp.proceed(jp.getArgs());
        logRequest(jp);
        return result;
    }

    private void logRequest(ProceedingJoinPoint jp) throws JsonProcessingException {
        String serviceName = ((MethodSignature) jp.getSignature()).getMethod()
                .getAnnotation(Loggable.class).action().action;
        String[] argNames = ((MethodSignature) jp.getSignature()).getParameterNames();
        Object[] values = jp.getArgs();
        if (argNames.length != 0) {
            for (int i = 0; i < argNames.length; i++) {
                Object obj= values[i];
                ObjectMapper mapper = new ObjectMapper();
                String objectType = obj.getClass().getName();
                String valueString = mapper.writeValueAsString(obj);
                DataChangeEvent customSpringEvent = new DataChangeEvent(this,this.message+valueString);
                applicationEventPublisher.publishEvent(customSpringEvent);

            }
        }
    }



}
