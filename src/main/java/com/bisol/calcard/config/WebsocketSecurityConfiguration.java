package com.bisol.calcard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
        	.simpDestMatchers("/topic/credit-proposal").permitAll()
				// .nullDestMatcher().authenticated()
				// .simpDestMatchers("/topic/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
				.simpDestMatchers("/topic/tracker").permitAll()
            // matches any destination that starts with /topic/
            // (i.e. cannot send messages directly to /topic/)
            // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
            // /topic/messages-user<id>)
            .simpDestMatchers("/topic/**").permitAll()
            // message types other than MESSAGE and SUBSCRIBE
				// .simpTypeMatchers(SimpMessageType.MESSAGE,
				// SimpMessageType.SUBSCRIBE).denyAll()
            // catch all
				// .anyMessage().denyAll();
				.anyMessage().permitAll();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
