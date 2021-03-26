package space.ffisherr.clay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.ffisherr.clay.model.ClayInstrument;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    @Value("${clay.integration-service.url}")
    private String integrationServiceUrl;


    @Override
    public List<ClayInstrument> readAllInstruments() {
        final List<ClayInstrument> tempList = Arrays.asList(new ClayInstrument(1L, "AMD"),
                new ClayInstrument(2L, "McDonald's"));
        // FIXME
        // TODO добавить реальное получение данных
        return tempList;
    }
}
