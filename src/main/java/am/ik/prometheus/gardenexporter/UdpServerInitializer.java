package am.ik.prometheus.gardenexporter;

import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import org.cloudfoundry.dropsonde.events.EventFactory.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;
import reactor.netty.udp.UdpServer;

import javax.annotation.PreDestroy;


@Component
public class UdpServerInitializer {
    private final static Logger log = LoggerFactory.getLogger(UdpServerInitializer.class);
    private final Connection server;
    private final MeterRegistry meterRegistry;


    public UdpServerInitializer(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        LoopResources loopResources = LoopResources.create("garden-dropsonde-server");
        this.server = UdpServer.create() //
                .port(3457) //
                .runOn(loopResources) //
                .wiretap() //
                .handle((in, out) ->
                        in.receiveObject() // Obtains the inbound Flux.
                                .log("receive")
                                .flatMap(o -> { // Handle the datagram.
                                    if (o instanceof DatagramPacket) {
                                        // Incoming DatagramPacket
                                        DatagramPacket p = (DatagramPacket) o;
                                        ByteBuf buf = p.content();
                                        buf.nioBuffer();
                                        byte[] payload = new byte[buf.readableBytes()];
                                        buf.readBytes(payload);
                                        try {
                                            return Mono.just(Envelope.parseFrom(payload));
                                        } catch (InvalidProtocolBufferException e) {
                                            return Mono.error(e);
                                        }
                                    } else {
                                        return Mono.empty();
                                    }
                                })
                                .log("envelop")
                                .filter(Envelope::hasValueMetric)
                                .map(Envelope::getValueMetric)
                                .doOnNext(valueMetric -> Gauge.builder(valueMetric.getName(), valueMetric::getValue)
                                        .baseUnit(valueMetric.getUnit())
                                        .register(this.meterRegistry))
                                .then()
                                .log("garden-dropsonde-server"))
                .bind()
                .block(); // Blocks and waits the server to finish initialising.;
    }

    @PreDestroy
    void destroy() {
        this.server.disposeNow();
    }
}
