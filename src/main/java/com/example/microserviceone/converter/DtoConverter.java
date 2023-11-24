package com.example.microserviceone.converter;

import com.example.microserviceone.dto.ServiceOneDto;
import com.example.microserviceone.entity.ServiceOneEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public ServiceOneDto entiyToDto(ServiceOneEntity serviceOneEntity ){

        ServiceOneDto serviceOneDto = new ServiceOneDto();

        serviceOneDto.setId(serviceOneEntity.getId());
        serviceOneDto.setServiceName(serviceOneEntity.getServiceName());
        serviceOneDto.setRequestTime(serviceOneEntity.getRequestTime());
        serviceOneDto.setResponseTime(serviceOneEntity.getResponseTime());
        serviceOneDto.setStatusCode(serviceOneEntity.getStatusCode());
        serviceOneDto.setTimeTaken(serviceOneEntity.getTimeTaken());
        serviceOneDto.setRequestURI(serviceOneEntity.getRequestURI());
        serviceOneDto.setRequestMethod(serviceOneEntity.getRequestMethod());
        serviceOneDto.setRequestHeaderName(serviceOneEntity.getRequestHeaderName());
        serviceOneDto.setContentType(serviceOneEntity.getContentType());
        serviceOneDto.setQueryParam(serviceOneEntity.getQueryParam());
        serviceOneDto.setRequestID(serviceOneEntity.getRequestID());
        serviceOneDto.setHostName(serviceOneEntity.getHostName());
        serviceOneDto.setResponse(serviceOneEntity.getResponse());
        serviceOneDto.setErrorTrace(serviceOneEntity.getErrorTrace());

        return serviceOneDto;
    }

    public List<ServiceOneDto> entitiesToDtos(List<ServiceOneEntity> serviceOneEntities) {
        return serviceOneEntities.stream()
                .map(this::entiyToDto)
                .collect(Collectors.toList());
    }

    public ServiceOneEntity dtoToEntity(ServiceOneDto serviceOneDto){

        ServiceOneEntity serviceOneEntity = new ServiceOneEntity();

        serviceOneEntity.setId(serviceOneDto.getId());
        serviceOneEntity.setServiceName(serviceOneDto.getServiceName());
        serviceOneEntity.setResponseTime(serviceOneDto.getResponseTime());
        serviceOneEntity.setResponseTime(serviceOneDto.getResponseTime());
        serviceOneEntity.setStatusCode(serviceOneDto.getStatusCode());
        serviceOneEntity.setTimeTaken(serviceOneDto.getTimeTaken());
        serviceOneEntity.setRequestURI(serviceOneDto.getRequestURI());
        serviceOneEntity.setRequestMethod(serviceOneDto.getRequestMethod());
        serviceOneEntity.setRequestHeaderName(serviceOneDto.getRequestHeaderName());
        serviceOneEntity.setContentType(serviceOneDto.getContentType());
        serviceOneEntity.setQueryParam(serviceOneDto.getQueryParam());
        serviceOneEntity.setRequestID(serviceOneDto.getRequestID());
        serviceOneEntity.setHostName(serviceOneDto.getHostName());
        serviceOneEntity.setResponse(serviceOneDto.getResponse());
        serviceOneEntity.setErrorTrace(serviceOneDto.getErrorTrace());

        return serviceOneEntity;
    }

    public List<ServiceOneEntity> dtoToEntity(List<ServiceOneDto> serviceOneDtos) {
        return serviceOneDtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
