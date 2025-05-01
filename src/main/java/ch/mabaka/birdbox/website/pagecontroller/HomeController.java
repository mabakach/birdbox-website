
package ch.mabaka.birdbox.website.pagecontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cache.annotation.EnableCaching;
import java.util.Map;

@Controller
@EnableCaching
public class HomeController {

    private final WebClient webClient;

    @Value("${birdbox.stream.url}")
    private String streamUrl;

    @Value("${birdbox.stream.status.url}")
    private String streamStatusUrl;

    @Value("${birdbox.stream.notavailable.image.url}")
    private String streamNotAvailableImageUrl;

    public HomeController() {
        this.webClient = WebClient.create();
    }

    @Cacheable(value = "streamStatus", key = "'status'", unless = "#result == null")
    private boolean isStreamAvailable() {
        return webClient.get()
                .uri(streamStatusUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Boolean) response.get("stream-available"))
                .block();
    }

    @GetMapping("/")
    public String index(final Model model) {
        String currentStreamUrl = isStreamAvailable() ? streamUrl : streamNotAvailableImageUrl;
        model.addAttribute("streamUrl", currentStreamUrl);
        return "index";
    }
}
