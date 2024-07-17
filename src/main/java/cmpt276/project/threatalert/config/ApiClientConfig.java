package cmpt276.project.threatalert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.auth.*;

@Configuration
public class ApiClientConfig {

    @Bean
    public ApiClient apiClient() {

        ApiClient defaultClient = com.cloudmersive.client.invoker.Configuration.getDefaultApiClient();
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey("827356e0-de55-4ff5-b4c2-fc77a06c3a28");
        return defaultClient;

    }
    
}
