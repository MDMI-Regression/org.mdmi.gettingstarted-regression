package org.mdmi.gettingstarted.tests;
/*******************************************************************************
 * Copyright (c) 2023 seanmuir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mdmi.rt.service.web.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author seanmuir
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class TestCDA2FHIR {

	@Autowired
	private TestRestTemplate template;

	@BeforeAll
	public static void setEnvironment() {
		System.setProperty("mdmi.maps", "src/main/resources/maps");
		System.setProperty("LOGGING_LEVEL_MDMI","TRACE");
	}

	private String runTransformation(String source, String target, String message,String extension) throws Exception {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("source", source);
		map.add("target", target);
		map.add("message", new FileSystemResource(Paths.get(message)));
		ResponseEntity<String> response = template.postForEntity("/mdmi/transformation", map, String.class);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		Path sourcePath = Paths.get(message);
		String testName = FilenameUtils.removeExtension(sourcePath.getFileName().toString());

		Path testPath = Paths.get("target/test-output/" + testName);
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}

		Path path = Paths.get("target/test-output/" + testName + "/" + testName + "." +extension);
		byte[] strToBytes = response.getBody().getBytes();

		Files.write(path, strToBytes);

		// System.out.println(response.getBody());
		return response.getBody();
	}

	@Test
	public void testFHIR2M2L() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/nullflavortest").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation("FHIRR4JSON.MasterBundle", "CDAR2.ContinuityOfCareDocument", document,"csv");			 
		}
	}
	
	@Test
	public void testCDA2FHIR2() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/source/cda2").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation( "CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundle", document,"json");			 
		}
	}
	
	@Test
	public void testCDA2FHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/cda/CDATests/CompleteCCD").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation("CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundleReference", document,"json");			 
		}
	}
	
	@Test
	public void testCDA2FHIRQualifierIssue() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/cda/CDATests/QualifierIssue").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation("CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundleReference", document,"json");			 
		}
	}
	
	@Test
	public void testMedicationAdministration() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/source/MedicaationAdministration").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation( "CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundle", document,"json");			 
		}
	}
	
	@Test
	public void testResultsSectionPQ() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/source/PQTest").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation( "CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundle", document,"json");			 
		}
	}
	
	
	@Test
	public void testCDA2FHIRMantis000130() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/Mantis0000130").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (String document: documents) {					
				runTransformation( "CDAR2.ContinuityOfCareDocument","FHIRR4JSON.MasterBundle", document,"xml");			 
		}
	}

}
