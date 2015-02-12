package com.riambsoft.core.service;

import java.util.Map;

import org.junit.Test;

import com.riambsoft.framework.core.web.JSONParser;
import com.riambsoft.framework.core.web.ParserException;

public class TestStoreData {

	@Test
	public void test1() throws ParserException {

		String bean = "{'metaData':{'paramNames':{'start':'start','limit':'limit','sort':'sort','dir':'dir'},'idProperty':'id','root':'users','fields':[{'name':'id','type':'number'},'code','name','remark'],'successProperty':'success','totalProperty':'total','limit':20,'sortInfo':{'sort':'code','dir':'ASC'}},'xaction':'read'}";

		JSONParser p = new JSONParser();

		Map map = (Map) p.marshal(bean, Map.class);

		StoreData data = new StoreData(map);

		System.out.println(data.getStart());

		System.out.println(data.getLimit());
	}

}
