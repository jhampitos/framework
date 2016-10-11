package org.demoiselle.jee.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.demoiselle.jee.configuration.extractor.ConfigurationValueExtractor;
import org.demoiselle.jee.configuration.extractor.impl.ConfigurationEnumValueExtractor;
import org.demoiselle.jee.configuration.model.ConfigEnum;
import org.demoiselle.jee.configuration.model.ConfigModel;
import org.demoiselle.jee.configuration.util.UtilTest;
import org.junit.Test;

public class EnumExtractorTest {
	
	private final String FILE_PREFIX = "app";
	private final UtilTest utilTest = new UtilTest();
	
	private ConfigModel configModel = new ConfigModel();
	private String PREFIX = "";
	
	private String FILE_PATH_PROPERTIES = "";
	private String FILE_PATH_XML = "";
	
	private ConfigurationValueExtractor conf = new ConfigurationEnumValueExtractor();
	
	public EnumExtractorTest() throws IOException{
		FILE_PATH_PROPERTIES = utilTest.createPropertiesFile(FILE_PREFIX);
		FILE_PATH_XML = utilTest.createXMLFile(FILE_PREFIX);
		utilTest.createSystemVariables();
	}
	
	@Test
	public void extractEnumFromProperties() throws Exception{
		Configuration configuration = utilTest.buildConfiguration(PropertiesConfiguration.class, FILE_PATH_PROPERTIES);
		
		Object value = conf.getValue(PREFIX, UtilTest.CONFIG_ENUM_FIELD, configModel.getClass().getDeclaredField(UtilTest.CONFIG_ENUM_FIELD), configuration);
		
		assertEquals(value.getClass(), ConfigEnum.class);
		assertEquals(value, UtilTest.CONFIG_ENUM_VALUE);
	}
	
	@Test
	public void extractEnumFromXML() throws Exception{
		Configuration configuration = utilTest.buildConfiguration(XMLConfiguration.class, FILE_PATH_XML);
		
		Object value = conf.getValue(PREFIX, UtilTest.CONFIG_ENUM_FIELD, configModel.getClass().getDeclaredField(UtilTest.CONFIG_ENUM_FIELD), configuration);
		
		assertEquals(value.getClass(), ConfigEnum.class);
		assertEquals(value, UtilTest.CONFIG_ENUM_VALUE);
	}
	
	@Test
	public void extractEnumFromSystemVariable() throws ConfigurationException, Exception {
		BasicConfigurationBuilder<? extends Configuration> builder = new BasicConfigurationBuilder<>(SystemConfiguration.class);

		Object value = conf.getValue(PREFIX, UtilTest.CONFIG_ENUM_FIELD, configModel.getClass().getDeclaredField(UtilTest.CONFIG_ENUM_FIELD), builder.getConfiguration());
		
		assertEquals(value, UtilTest.CONFIG_ENUM_VALUE);
	}
	
	@Test
	public void extractStringShouldBeSupportString() throws NoSuchFieldException, SecurityException, NoSuchMethodException{
		final Method method = configModel.getClass().getDeclaredMethod("get" + StringUtils.capitalize(UtilTest.CONFIG_ENUM_FIELD));
		
		assertEquals(method.getReturnType(), ConfigEnum.class);
		assertTrue(conf.isSupported(configModel.getClass().getDeclaredField(UtilTest.CONFIG_ENUM_FIELD)));
	}

}
