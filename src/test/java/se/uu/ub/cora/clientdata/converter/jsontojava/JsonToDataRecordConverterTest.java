/*
 * Copyright 2015, 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.clientdata.converter.jsontojava;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.Assert;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParseException;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class JsonToDataRecordConverterTest {

	private JsonToDataConverterFactory factory;

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Record data must contain key: record")
	public void testNotARecordString() throws Exception {
		createClientDataRecordForJsonString("{\"not\":\"record\"}");
	}

	private ClientDataRecord createClientDataRecordForJsonString(String json) {
		OrgJsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(json);
		JsonToDataConverterFactory factory2 = new JsonToDataConverterFactoryImp();
		factory = new JsonToDataConverterFactoryOnlyGroupConverterSpy();
		JsonToDataRecordConverter jsonToDataConverter = JsonToDataRecordConverter
				.forJsonObjectUsingConverterFactory(((JsonObject) jsonValue), factory);
		return jsonToDataConverter.toInstance();
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Not an object")
	public void testRecordIsNotAnObject() throws Exception {
		createClientDataRecordForJsonString("{\"record\":\"record\"}");
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Record data must contain child with key: data")
	public void testRecordDoesNotContainData() throws Exception {
		String json = "{\"record\":{";
		json += "\"notData\":\"notData\"";
		json += "}}";
		createClientDataRecordForJsonString(json);
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Not an object")
	public void testRecordDataNotAnObject() throws Exception {
		String json = "{\"record\":{";
		json += "\"data\":\"notData\"";
		json += ",\"actionLinks\":\"noActionLink\"";
		json += "}}";
		createClientDataRecordForJsonString(json);
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Record data must contain only key: record")
	public void testRecordExtraKey() throws Exception {
		String json = "{\"record\":{";
		json += "\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += ",\"actionLinks\":\"noActionLink\"";
		json += "}";
		json += ",\"someExtraKey\":\"someExtraData\"";
		json += "}";
		createClientDataRecordForJsonString(json);
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Error parsing jsonObject: Group data must contain key: children")
	public void testRecordDataNotADataGroup() throws Exception {
		String json = "{\"record\":{";
		json += "\"data\":{";
		json += "\"name\":\"groupNameInData\"";
		json += "}";
		json += ",\"actionLinks\":{\"read\":\"noActionLink\"}";
		json += "}}";
		createClientDataRecordForJsonString(json);
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Record data must contain child with key: actionLinks")
	public void testRecordNoActionLinks() throws Exception {
		String json = "{\"record\":{";
		json += "\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += "}}";
		createClientDataRecordForJsonString(json);
	}

	@Test(expectedExceptions = JsonParseException.class, expectedExceptionsMessageRegExp = ""
			+ "Error parsing jsonRecord: Record data must contain only keys: data and actionLinks")
	public void testRecordExtraKeyOnSecondLevel() throws Exception {
		String json = "{\"record\":{";
		json += "\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += ",\"actionLinks\":\"noActionLink\"";
		json += ",\"someExtraKey\":\"someExtraData\"";
		json += "}}";
		createClientDataRecordForJsonString(json);
	}

	@Test
	public void providedFactoryIsUsedForDataGroup() throws Exception {
		String json = "{\"record\":{\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += ", \"actionLinks\":{";
		json += " \"read\":{";
		json += " \"requestMethod\":\"GET\",";
		json += " \"rel\":\"read\",";
		json += " \"url\":\"https://cora.example.org/somesystem/rest/record/somerecordtype/somerecordid\",";
		json += " \"accept\":\"application/vnd.uub.record+json\"";
		json += "}";
		json += "}";
		json += "}";
		json += "}";
		createClientDataRecordForJsonString(json);
		JsonToDataConverterFactoryOnlyGroupConverterSpy factorySpy = (JsonToDataConverterFactoryOnlyGroupConverterSpy) factory;
		JsonObject jsonValueSentToFactory = factorySpy.jsonObjects.get(0);
		assertEquals(jsonValueSentToFactory.getValueAsJsonString("name").getStringValue(), "groupNameInData");
	}

	@Test
	public void providedFactoryIsUsedForActionLinks() throws Exception {
		String json = "{\"record\":{\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += ", \"actionLinks\":{";
		json += " \"read\":{";
		json += " \"requestMethod\":\"GET\",";
		json += " \"rel\":\"read\",";
		json += " \"url\":\"https://cora.example.org/somesystem/rest/record/somerecordtype/somerecordid\",";
		json += " \"accept\":\"application/vnd.uub.record+json\"";
		json += "}";
		json += "}";
		json += "}";
		json += "}";
		createClientDataRecordForJsonString(json);
		JsonToDataConverterFactoryOnlyGroupConverterSpy factorySpy = (JsonToDataConverterFactoryOnlyGroupConverterSpy) factory;
		JsonObject jsonValueSentToFactory = factorySpy.jsonObjects.get(1);
		JsonObject readLink = jsonValueSentToFactory.getValueAsJsonObject("read");
		assertEquals(readLink.getValueAsJsonString("requestMethod").getStringValue(), "GET");
		assertEquals(readLink.getValueAsJsonString("rel").getStringValue(), "read");
		assertEquals(readLink.getValueAsJsonString("url").getStringValue(), "https://cora.example.org/somesystem/rest/record/somerecordtype/somerecordid");
		assertEquals(readLink.getValueAsJsonString("accept").getStringValue(), "application/vnd.uub.record+json");
	}

//	private ClientDataRecord createClientDataRecordForJsonStringUsingSpy(String json) {
//		OrgJsonParser jsonParser = new OrgJsonParser();
//		JsonValue jsonValue = jsonParser.parseString(json);
//		// JsonToDataConverterFactory factory = new JsonToDataConverterFactorySpy();
//		JsonToDataConverterFactory factory = new JsonToDataConverterFactoryOnlyGroupConverterSpy();
//		JsonToDataRecordConverter jsonToDataConverter = JsonToDataRecordConverter
//				.forJsonObjectUsingConverterFactory(((JsonObject) jsonValue), factory);
//		return jsonToDataConverter.toInstance();
//	}

	@Test
	public void testToClass() {
		String json = "{\"record\":{\"data\":{";
		json += "\"name\":\"groupNameInData\", \"children\":[]";
		json += "}";
		json += ", \"actionLinks\":{";
		json += " \"read\":{";
		json += " \"requestMethod\":\"GET\",";
		json += " \"rel\":\"read\",";
		json += " \"url\":\"https://cora.example.org/somesystem/rest/record/somerecordtype/somerecordid\",";
		json += " \"accept\":\"application/vnd.uub.record+json\"";
		json += "}";
		json += "}";
		json += "}";
		json += "}";
		ClientDataRecord clientDataRecord = createClientDataRecordForJsonString(json);
		ClientDataGroup clientDataGroup = clientDataRecord.getClientDataGroup();
		assertEquals(clientDataGroup.getNameInData(), "groupNameInData");
	}

}
