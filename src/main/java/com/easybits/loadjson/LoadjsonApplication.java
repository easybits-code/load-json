package com.easybits.loadjson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadjsonApplication {

	public static void main(String[] args) {
		loadNeighborhoods();
	}

	public static List<String> loadNeighborhoods() {
		return loadNeighborhoods(new File("src/main/resources/chicago.json"));
	}

	public static List<String> loadNeighborhoods(String filePath) {
		if (filePath == null) {
			System.out.println("Arquivo null nao encontrado!");
			return Collections.emptyList();
		}
		return loadNeighborhoods(new File(filePath));
	}

	public static List<String> loadNeighborhoods(File jsonFile) {
		List<String> list = new ArrayList<>();
		if (jsonFile == null || !jsonFile.exists()) {
			System.out.println("Arquivo " + (jsonFile != null ? jsonFile.getPath() : "null") + " nao encontrado!");
			return list;
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(jsonFile);
			return processNeighborhoods(rootNode);
		} catch (IOException e) {
			e.printStackTrace();
			return list;
		}
	}

	public static List<String> processNeighborhoods(JsonNode rootNode) {
		List<String> list = new ArrayList<>();
		if (rootNode == null) {
			System.out.println("Nenhum bairro encontrado no JSON.");
			return list;
		}

		JsonNode neighborhoods = rootNode.get("neighborhoods");

		if (neighborhoods != null && neighborhoods.isArray()) {
			System.out.println("Bairros encontrados:");
			for (JsonNode node : neighborhoods) {
				String name = node.has("name") && !node.get("name").isNull() ? node.get("name").asText() : "";
				String description = node.has("description") && !node.get("description").isNull() ? node.get("description").asText() : "";
				String formatted = "- " + name + ": " + description;
				System.out.println(formatted);
				list.add(formatted);
			}
		} else {
			System.out.println("Nenhum bairro encontrado no JSON.");
		}
		return list;
	}

}
