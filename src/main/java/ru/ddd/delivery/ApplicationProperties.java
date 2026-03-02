package ru.ddd.delivery;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private final Grpc grpc = new Grpc();

    public Grpc getGrpc() {
        return grpc;
    }

    public static class Grpc {
        private final GeoService geoService = new GeoService();

        public GeoService getGeoService() {
            return geoService;
        }

        public static class GeoService {
            private String host;
            private int port;

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }
        }
    }
}