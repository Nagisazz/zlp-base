package com.nagisazz.base.autoconfig;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.nagisazz.base.util.SpringBeanUtil;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nagisazz.base.config.rest.RestErrorHandler;
import com.nagisazz.base.property.RestTemplateConfig;
import com.nagisazz.base.property.SystemConfig;
import com.nagisazz.base.util.RestHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan({"com.nagisazz.base"})
@EnableConfigurationProperties({SystemConfig.class, RestTemplateConfig.class})
@ConditionalOnClass(SpringBeanUtil.class)
public class BaseAutoConfiguration {

    /**
     * spring 工具类
     *
     * @return SpringBeanUtil
     */
    @Bean
    @ConditionalOnMissingBean(SpringBeanUtil.class)
    public SpringBeanUtil springBeanUtil() {
        return new SpringBeanUtil();
    }

    /**
     * rest工厂类
     *
     * @return 工厂类
     */
    @Bean
    @ConditionalOnMissingBean({ClientHttpRequestFactory.class})
    public ClientHttpRequestFactory requestFactory(RestTemplateConfig config) {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(config.getHttpClient().getMaxTotal());
        manager.setDefaultMaxPerRoute(config.getHttpClient().getMaxPreRoute());
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().setConnectionManager(manager).setRetryHandler(
                        new DefaultHttpRequestRetryHandler(config.getHttpClient().getRetryCount(), true)).build());
        factory.setConnectTimeout(config.getConnectTimeout());
        factory.setReadTimeout(config.getReadTimeout());
        factory.setConnectionRequestTimeout(config.getHttpClient().getWaitTimeout());
        return factory;
    }

    /**
     * 构建RestTemplate实例
     *
     * @param factory rest工厂方法
     * @return 实例
     */
    @Bean
    @ConditionalOnClass(FastJsonHttpMessageConverter.class)
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new RestErrorHandler());
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        converterList.removeIf(e -> e instanceof StringHttpMessageConverter);
        converterList.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        FastJsonConfig conf = new FastJsonConfig();
        conf.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(conf);
        converterList.add(0, converter);
        return restTemplate;
    }

    /**
     * 没有FastJsonHttpMessageConverter的restTemplate
     *
     * @param factory
     * @return
     */
    @Bean("baseRestTemplate")
    @ConditionalOnMissingBean(name = "baseRestTemplate")
    public RestTemplate baseRestTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new RestErrorHandler());
        restTemplate.getMessageConverters().removeIf(e -> e instanceof StringHttpMessageConverter);
        restTemplate.getMessageConverters().add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    /**
     * 请求帮助类
     *
     * @param restTemplate restTemplate
     * @return IStorageService
     */
    @Bean
    @ConditionalOnMissingBean(RestHelper.class)
    public RestHelper restHelper(RestTemplate restTemplate, RestTemplate baseRestTemplate) {
        return new RestHelper(restTemplate, baseRestTemplate);
    }
}
