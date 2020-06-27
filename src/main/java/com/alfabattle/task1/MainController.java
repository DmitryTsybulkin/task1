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
                        .addHeader("cert", "-----BEGIN CERTIFICATE-----MIIFIzCCAwugAwIBAgICEBkwDQYJKoZIhvcNAQELBQAwgakxCzAJBgNVBAYTAlJVMQ8wDQYDVQQIDAbQnNCh0JoxFTATBgNVBAcMDNCc0L7RgdC60LLQsDEcMBoGA1UECgwT0JDQu9GM0YTQsCDQkdCw0L3QujEOMAwGA1UEAwwFYXBpY2ExJTAjBgkqhkiG9w0BCQEWFmFwaXN1cHBvcnRAYWxmYWJhbmsucnUxDzANBggqhQMDgQMBARIBMDEMMAoGBSqFA2QDEgEwMB4XDTE4MDQyMzA2NDIwN1oXDTMxMTIzMTA2NDIwN1owgaAxCzAJBgNVBAYTAlJVMQwwCgYDVQQIDANNU0sxEjAQBgNVBAoMCUFsZmEgQmFuazEWMBQGA1UEAwwNYXBpZGV2ZWxvcGVyczElMCMGCSqGSIb3DQEJARYWYXBpc3VwcG9ydEBhbGZhYmFuay5ydTEPMA0GCCqFAwOBAwEBEgEwMQwwCgYFKoUDZAMSATAxETAPBgorBgEEAYGCaWQBDAEwMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6+wUdr+1nyb6gxg7E6HzzR48rE25js5/fpM5GOoGVFfgT502XdSHXdYGDT3OPsNix2nBfPOppzDZdOJQbk+XPQ6Bj8u8FRkkd6gRSLQQbLLDe/C2IDhGxuFHeQWVvb7PL/w7srAxH0SOVEJgn/8tD5D9FaoFN6NaUk34eb/tojZJpbydhx+eWFtBXtrxEtESbGyTm2X7Q2VG36PqCwQdgdNwf6JUN8dIYotG+4rEJp1xsDqf7U8I5VoT1sE7rAY6fEHuThHtENCd5JLqRiFqVSbSsxXhO5COofkUeXBfnUxD9/auSdwqX+6DdhS6HWcN3P4nBLjlVM0M7P6t8fIQGwIDAQABo1wwWjBABgNVHR8EOTA3MDWgM6Axhi9odHRwOi8vYXBpY2EubW9zY293LmFsZmFpbnRyYS5uZXQvYXBpY2EuY3JsLnBlbTAJBgNVHRMEAjAAMAsGA1UdDwQEAwIF4DANBgkqhkiG9w0BAQsFAAOCAgEANbGOlIxDFxfqigiqWuRpnsg7vgqRbCSy1HpkTs8y2XNdG6jsxA3a42vQCWy74cXmEXrf7m9BQBh3HIck4Ag5azo1+svwJmExhVx3P7RdmP4DuqO1XsWLPJmaeMWFDm71PO2N1vLOtsymnp71JPBV+x6mY9S0ecW37ZdrCUjKgWnxIijneLTF3NIkaNoB6WPdlasPF+KmATv8eMuTZj27e9xLVikTC+5mBtM8mQiWYP4dStcOI7TO810/6PJkekjPYNV5ldzSif+ER3y9U0x/sdaRmnR9vCw7geEKtum3JMv6uqYumaqMeB8ZEUEkZdgLFgGzDc10VuROwqN89qA8wQV7gN2nlQ7NdcrLcyq1NLH5EYP9AjgR47Ure93rOJvrr8w7c+WpYIGMEjtMySrIiRtYtDrxTH/jZPUFoKRphyQ3s4Ja/86L8ONSBFm/F9TAGqrzaMrhHM1nlX8fNl6BLBVFN7iDjvAzTCrIrG/fOh6MI1R1icbjiREWBzcij7lyENqLzQUZQiLxSQLQ7dKd6YkjNmMl+TL4Z7HAyiaJzOuypKX5g6q+KoH10LXUB5pq+8tGI7J3K06lrF28VZd8TPzOgll/N/Gz9Ce8eOoQG+dOS0Omv5Bz1cstue4I0+NuE+vVcQIkQPQTYzGbty5dALrDx1II4VsZnzjmJaqzq98=-----END CERTIFICATE-----")
                        .addHeader("key", "-----BEGIN RSA PRIVATE KEY-----MIIEpAIBAAKCAQEA6+wUdr+1nyb6gxg7E6HzzR48rE25js5/fpM5GOoGVFfgT502XdSHXdYGDT3OPsNix2nBfPOppzDZdOJQbk+XPQ6Bj8u8FRkkd6gRSLQQbLLDe/C2IDhGxuFHeQWVvb7PL/w7srAxH0SOVEJgn/8tD5D9FaoFN6NaUk34eb/tojZJpbydhx+eWFtBXtrxEtESbGyTm2X7Q2VG36PqCwQdgdNwf6JUN8dIYotG+4rEJp1xsDqf7U8I5VoT1sE7rAY6fEHuThHtENCd5JLqRiFqVSbSsxXhO5COofkUeXBfnUxD9/auSdwqX+6DdhS6HWcN3P4nBLjlVM0M7P6t8fIQGwIDAQABAoIBAQDmrzbrTlV3Q5Pcr8rETa2ouZI2IOH6A2yjBa3k8KAzxKFu95h7cx7R4D6WoAKVWFO0DXLGjTMsahWXtttSobyOVSrWwi4sqyL/ZXVRE9CjmMCQ5jQPmTeLrChI6+8ZVAQfNZHiDWpa2pwZQ7ZbfBZBjFIL5u2nE7FznQ4DbmocGvxcDHRhsl3A6Dj/oiDVUjRTA0QKWXC5OvW+QvDp5hevXy+pNlpPPfS3WDq1v71LHE/zXW7aqvfURVoucUgTTcqv3Id03Dmn3giQY8oKJ8wwKwnBiVxaGSvQ+qL4Squd9OpvPqxgVrK3HNv5KQwD0rwIF6iyS/i5JPS3bkaKIp9xAoGBAP2cdg9467sbDcEQ562/5ptQJyn3eJXnt9YXeK/xCd/TfFnoJMGymJDvXDMY6UxTIo1L42GhRK24RvWQCaM4JtKf+w8gSRSOKTAcXE1glGFFJdp6k8bsuiV6XLz+C8LQ1dgAN17vKHtsIu9jBGpJ1laROf77O83HSdtlxiHfYOtFAoGBAO4k9wHRjKXrwmJBbQBKvwa+20GnMnyzlxLE/HdW84wUJndm8dVTA4IUw9ziQuvJPyoYj3NREyB+iqSwWA9SbZVlP5ygZiNdt/U7GMLrkV9aIspjn9+BZg8eE72u77j99wwhQffUlNjBadnty55/hqZEN5bfCU7q2aSs1S24QxPfAoGACZ/BXy7wiyYorHXuxMoufyBYjCAnBFFJyacUXmDgRg0pf+PuWSdk4MMklY/RcqMJAaGqnE7ZY1pF3xW7N+Vum7mUdmt8Onp2l6vL2OfcWaCdIiBasUbUfY2aLVXarmbsZokDkOe0epKM9NamGT56EsjqEYIJL/LrG5dOIITwkZ0CgYB+Zk5Yv8GYW49cGX/idMn5qZm0uirY93WLN9UM/rDcO+HC5Hgdkyc8hwsClXGRtsp47HpPacvuJlwuV48V6tlRP2FJH/W2dcrrNcfJxcDuTGhE5iX4OrVR9tPhjRWeKeZToN9jzIiIl/g3/Lhj0ypD29dpk85b/dkxR68Kyk+IHwKBgQCxklrIcKk0ACLYFV1N8xUN04LoWFa9q4FPxbjnsHTBmr+ThtxLdlLGYsAiW3TGBS3yOPeHiANw36QvVpL/C6MNu42ZKxa2QbnHGWbVnsSmpotImibbmul9MfORkrttG92/GNxwo/OY6x/i9f61+1L/KyZn+CPj7O0oboyKanPA3Q==-----END RSA PRIVATE KEY-----")
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
            @RequestParam(name = "payments", required = false, defaultValue = "false") Boolean payments
    ) {
        return data.stream().filter(atm -> {
            if (payments) {
                return atm.getPayments() && checkLocation(latitude, atm.getLatitude(), longitude, atm.getLongitude());
            } else {
                return !atm.getPayments() && checkLocation(latitude, atm.getLatitude(), longitude, atm.getLongitude());
            }
        }).collect(Collectors.toSet());
    }

    private boolean checkLocation(String sourceLat, String atmLat, String sourceLon, String atmLon) {
        if (Objects.nonNull(sourceLat) && Objects.nonNull(sourceLon)) {
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
            throw new ResourceNotFoundException("404");
        }
    }

}
