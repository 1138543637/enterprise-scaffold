package cn.sxu.enterprise.module.mine.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.StringUtils;

@Configuration
@EnableIntegration
@EnableConfigurationProperties(MineMqttProperties.class)
@ConditionalOnProperty(prefix = "enterprise.mine.mqtt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MineMqttConfig {

    @Bean
    public MessageChannel mineMqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mineMqttInbound(
            MineMqttProperties properties,
            @Qualifier("mineMqttInputChannel") MessageChannel mineMqttInputChannel) {
        DefaultMqttPahoClientFactory clientFactory = createClientFactory(properties);
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(properties.getClientId(), clientFactory, properties.getTopic());
        adapter.setCompletionTimeout(properties.getCompletionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(properties.getQos());
        adapter.setOutputChannel(mineMqttInputChannel);
        return adapter;
    }

    @Bean
    public MessageChannel mineMqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mineMqttOutboundChannel")
    public MessageHandler mineMqttOutbound(MineMqttProperties properties) {
        DefaultMqttPahoClientFactory clientFactory = createClientFactory(properties);
        MqttPahoMessageHandler handler =
                new MqttPahoMessageHandler(properties.getClientId() + "-publisher", clientFactory);
        handler.setAsync(true);
        handler.setDefaultTopic(properties.getTopic());
        handler.setDefaultQos(properties.getQos());
        return handler;
    }

    private DefaultMqttPahoClientFactory createClientFactory(MineMqttProperties properties) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{properties.getBrokerUrl()});
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(30);

        if (StringUtils.hasText(properties.getUsername())) {
            options.setUserName(properties.getUsername());
        }
        if (StringUtils.hasText(properties.getPassword())) {
            options.setPassword(properties.getPassword().toCharArray());
        }

        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(options);
        return clientFactory;
    }
}
