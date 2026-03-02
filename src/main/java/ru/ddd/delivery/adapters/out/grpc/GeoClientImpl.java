package ru.ddd.delivery.adapters.out.grpc;

import org.springframework.stereotype.Service;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto.GetGeolocationRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import ru.ddd.delivery.ApplicationProperties;
import ru.ddd.delivery.core.GeoClient;
import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.Result;

@Service
public class GeoClientImpl implements GeoClient {
    private final ManagedChannel channel;
    private final GeoGrpc.GeoBlockingStub stub;

    public GeoClientImpl(ApplicationProperties properties) {
        String host = properties.getGrpc().getGeoService().getHost();
        int port = properties.getGrpc().getGeoService().getPort();
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = GeoGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (!channel.isShutdown()) {
            channel.shutdown();
        }
    }
    @Override
    public Result<Location, Error> getLocation(String address) {
        Except.againstNull(address, "address");

        var request = GetGeolocationRequest.newBuilder().setStreet(address).build();;

        var response = stub.getGeolocation(request);

        return Location.create(response.getLocation().getX(), response.getLocation().getY());
    }

}
