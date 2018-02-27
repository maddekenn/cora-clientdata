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

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.json.parser.JsonParseException;

public class JsonToDataConverterFactoryTest {
	private JsonToDataConverterFactory jsonToDataConverterFactory;

	@BeforeMethod
	public void beforeMethod() {
		jsonToDataConverterFactory = new JsonToDataConverterFactoryImp();
	}

	@Test
	public void testFactorOnJsonStringDataGroupEmptyChildren() {
		String json = "{\"name\":\"groupNameInData\", \"children\":[]}";
		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonString(json);
		assertTrue(jsonToDataConverter instanceof JsonToDataGroupConverter);
	}

	@Test
	public void testFactorOnJsonStringDataGroupAtomicChild() {
		String json = "{\"name\":\"id\", \"children\":[{\"id2\":\"value\"}]}";
		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonString(json);
		assertTrue(jsonToDataConverter instanceof JsonToDataGroupConverter);
	}

	@Test
	public void testFactorOnJsonStringDataAtomic() {
		String json = "{\"name\":\"atomicNameInData\",\"value\":\"atomicValue\"}";
		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonString(json);
		assertTrue(jsonToDataConverter instanceof JsonToDataAtomicConverter);
	}

	@Test
	public void testFactorOnJsonStringDataAttribute() {
		String json = "{\"attributeNameInData\":\"attributeValue\"}";
		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonString(json);
		assertTrue(jsonToDataConverter instanceof JsonToDataAttributeConverter);
	}

	@Test
	public void testFactorOnJsonStringDataRecordLink() {
		String json = "{\"name\":\"link\", \"children\":[{\"name\": \"linkedRecordId\", \"value\": \"myLinkedRecordId\"} ]}";

		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonString(json);
		assertTrue(jsonToDataConverter instanceof JsonToDataGroupConverter);
	}

	@Test(expectedExceptions = JsonParseException.class)
	public void testFactorOnJsonObjectNullJson() {
		jsonToDataConverterFactory.createForJsonString(null);
	}

	@Test(expectedExceptions = JsonParseException.class)
	public void testClassCreatorGroupNotAGroup() {
		String json = "[{\"id\":{\"id2\":\"value\"}}]";
		jsonToDataConverterFactory.createForJsonString(json);
	}

}
