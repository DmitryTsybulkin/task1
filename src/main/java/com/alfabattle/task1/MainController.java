package com.alfabattle.task1;

import com.alfabattle.task1.common.General;
import com.alfabattle.task1.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class MainController {

    private static final String KEY = "f3026f7a-190d-4e4f-8719-6b79d3fbe713";
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private Set<AtmResponse> data;

    @PostConstruct
    public void initAtmStore() {
        // не работает, проблемы с сертификатами...
        Call call = client.newCall(
                new Request.Builder()
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("x-ibm-client-id", KEY)
                        .addHeader("cert", "-----BEGIN CERTIFICATE-----=-----END CERTIFICATE-----")
                        .addHeader("key", "-----BEGIN RSA PRIVATE KEY----==-----END RSA PRIVATE KEY-----")
                        .url("https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms")
                        .build()
        );
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                throw new RuntimeException(
                System.out.println(
                        String.format("Не удалось получить данные с api.alfabank.ru: %s", e.getLocalizedMessage())
                );
//                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.code());
            }
        });

        try {
            this.data = new ObjectMapper().readValue(new File("data.json"), new TypeReference<General>(){})
                    .getData().getAtms()
                    .stream()
                    .map(atm -> AtmResponse.builder()
                            .city(atm.getAddress().getCity())
                            .deviceId(atm.getDeviceId())
                            .latitude(atm.getCoordinates().getLatitude())
                            .longitude(atm.getCoordinates().getLongitude())
                            .location(atm.getAddress().getLocation())
                            .payments(atm.getServices().getPayments().equals("Y"))
                            .build())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "Get nearest atm", httpMethod = "GET")
    @GetMapping("/atms/nearest")
    public Set<AtmResponse> getNearestAtm(
            @RequestParam(name = "latitude", required = false) String latitude,
            @RequestParam(name = "longitude", required = false) String longitude,
            @RequestParam(name = "payments", required = false, defaultValue = "false") String payments
    ) {
        System.out.println("Payment: " + payments);
        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);
        return data.stream().filter(atm -> {
//            if (payments) {
//                return atm.getPayments() && checkLocation(latitude, atm.getLatitude(), longitude, atm.getLongitude());
//            } else {
//                return !atm.getPayments() && checkLocation(latitude, atm.getLatitude(), longitude, atm.getLongitude());
//            }
            return true;
        }).collect(Collectors.toSet());
    }

    private boolean checkLocation(String sourceLat, String atmLat, String sourceLon, String atmLon) {
        if (Objects.nonNull(sourceLat) && Objects.nonNull(sourceLon) && Objects.nonNull(atmLat) && Objects.nonNull(atmLon)) {
            return Math.abs(Double.parseDouble(atmLat) - Double.parseDouble(sourceLat)) < 2 &&
                    Math.abs(Double.parseDouble(atmLon) - Double.parseDouble(sourceLon)) < 2;
        } else {
            return true;
        }
    }

    @ApiOperation(value = "Get nearest-with-alfik", httpMethod = "GET")
    @GetMapping("/atms/nearest-with-alfik")
    public Set<AtmResponse> getNearestAtmsWithMoney(
            @RequestParam(name = "alfik", required = false) Integer alfik,
            @RequestParam(name = "latitude", required = false) String latitude,
            @RequestParam(name = "longitude", required = false) Boolean longitude
    ) {
        return new HashSet<>();
    }

    @ApiOperation(value = "Get atm by id", httpMethod = "GET")
    @GetMapping("/atms/{id}")
    public AtmResponse getById(
            @PathVariable(name = "id") Long id
    ) {
        Optional<AtmResponse> first = data.stream().filter(atm -> atm.getDeviceId().equals(id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new ResourceNotFoundException("atm not found");
        }
    }

}
