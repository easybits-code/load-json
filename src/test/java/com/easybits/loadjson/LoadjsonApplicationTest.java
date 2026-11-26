package com.easybits.loadjson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadjsonApplicationTest {

	@Test
	void testLoadNeighborhoods_DefaultFile() {
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods();

		assertNotNull(neighborhoods);
		assertEquals(4, neighborhoods.size());
		assertEquals("- River North: Known for its art galleries, vibrant nightlife, and high-end restaurants.", neighborhoods.get(0));
		assertEquals("- Wicker Park: Famous for its independent boutiques, trendy cafes, and lively music scene.", neighborhoods.get(1));
		assertEquals("- Lincoln Park: A leafy residential neighborhood featuring the famous Lincoln Park Zoo and lakefront parks.", neighborhoods.get(2));
		assertEquals("- Pilsen: A cultural hub with a strong Mexican heritage, known for its colorful murals and authentic cuisine.", neighborhoods.get(3));
	}

	@Test
	void testLoadNeighborhoods_WithFile() {
		File file = new File("src/main/resources/chicago.json");
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(file);

		assertNotNull(neighborhoods);
		assertEquals(4, neighborhoods.size());
		assertTrue(neighborhoods.get(0).contains("River North"));
	}

	@Test
	void testLoadNeighborhoods_WithFilePathString() {
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods("src/main/resources/chicago.json");

		assertNotNull(neighborhoods);
		assertEquals(4, neighborhoods.size());
		assertTrue(neighborhoods.get(2).contains("Lincoln Park"));
	}

	@Test
	void testLoadNeighborhoods_NonExistentFile() {
		File missingFile = new File("src/main/resources/does_not_exist.json");
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(missingFile);

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_NullFile() {
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods((File) null);

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_NullFilePathString() {
		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods((String) null);

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_EmptyArray(@TempDir Path tempDir) throws IOException {
		Path jsonFile = tempDir.resolve("empty_array.json");
		Files.writeString(jsonFile, "{\"neighborhoods\": []}");

		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(jsonFile.toFile());

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_MissingNeighborhoodsKey(@TempDir Path tempDir) throws IOException {
		Path jsonFile = tempDir.resolve("missing_key.json");
		Files.writeString(jsonFile, "{\"city\": \"Chicago\", \"population\": 2746388}");

		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(jsonFile.toFile());

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_NeighborhoodsNotAnArray(@TempDir Path tempDir) throws IOException {
		Path jsonFile = tempDir.resolve("not_array.json");
		Files.writeString(jsonFile, "{\"neighborhoods\": \"Not an array string\"}");

		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(jsonFile.toFile());

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_MalformedJson(@TempDir Path tempDir) throws IOException {
		Path jsonFile = tempDir.resolve("malformed.json");
		Files.writeString(jsonFile, "{ \"neighborhoods\": [ invalid json content");

		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(jsonFile.toFile());

		assertNotNull(neighborhoods);
		assertTrue(neighborhoods.isEmpty());
	}

	@Test
	void testLoadNeighborhoods_MissingFieldsInElement(@TempDir Path tempDir) throws IOException {
		Path jsonFile = tempDir.resolve("missing_fields.json");
		String json = "{\n" +
				"  \"neighborhoods\": [\n" +
				"    {\"name\": \"Loop\"},\n" +
				"    {\"description\": \"Financial district\"},\n" +
				"    {}\n" +
				"  ]\n" +
				"}";
		Files.writeString(jsonFile, json);

		List<String> neighborhoods = LoadjsonApplication.loadNeighborhoods(jsonFile.toFile());

		assertNotNull(neighborhoods);
		assertEquals(3, neighborhoods.size());
		assertEquals("- Loop: ", neighborhoods.get(0));
		assertEquals("- : Financial district", neighborhoods.get(1));
		assertEquals("- : ", neighborhoods.get(2));
	}

	@Test
	void testMainMethodExecution() {
		assertDoesNotThrow(() -> LoadjsonApplication.main(new String[0]));
	}

}
