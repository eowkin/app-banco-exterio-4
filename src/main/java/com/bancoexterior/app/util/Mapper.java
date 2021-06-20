package com.bancoexterior.app.util;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class Mapper {

	ModelMapper mapperUtiliti = new ModelMapper();
	
	ObjectMapper mapeador = new ObjectMapper();
	
	GsonBuilder gsonBuilder =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public <T> T jsonToClass(String json, Class<T> clazz) throws IOException {
        return mapeador.readValue(json, clazz);
    }
	
	
	public <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
	
	public <T> List<T> fromJsonAsList(String json, Class<T[]> clazz) {
        return Arrays.asList(new Gson().fromJson(json, clazz));
    }
	
	/**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public <D, T> D map(T entity, Class<D> outClass) {
        return mapperUtiliti.map(entity, outClass);
    }
    
    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }
}
