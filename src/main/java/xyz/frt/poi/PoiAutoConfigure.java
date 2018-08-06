package xyz.frt.poi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.frt.poi.service.PoiService;
import xyz.frt.poi.service.PoiServiceImpl;
import xyz.frt.poi.service.PoiServiceProperties;

@Configuration
@ConditionalOnClass(PoiService.class)
@EnableConfigurationProperties(PoiServiceProperties.class)
public class PoiAutoConfigure {

	@Autowired
	private PoiServiceProperties properties;

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "xyz.frt.poi", value = "enable", havingValue = "true")
	PoiService poiService() {
		return new PoiServiceImpl(properties.getSheetHeader(), properties.getStartRow(), properties.getStartCol());
	}

}
